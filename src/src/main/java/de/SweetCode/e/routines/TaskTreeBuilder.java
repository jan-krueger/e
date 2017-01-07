package de.SweetCode.e.routines;

import de.SweetCode.e.routines.composite.*;
import de.SweetCode.e.utils.Assert;

import java.util.function.Predicate;

public class TaskTreeBuilder {

    private final Task root;
    private Task currentNode;

    public TaskTreeBuilder(Task root) {
        Assert.assertNotNull("The root cannot be null.", root);

        this.root = root;
        this.currentNode = root;
    }

    /**
     * Adds a random selector.
     * @return
     */
    public TaskTreeBuilder randomSelector(String name) {
        this.addChild(new TaskRandomSelector(name));
        return this;
    }

    /**
     * Adds a random sequence.
     * @return
     */
    public TaskTreeBuilder randomSequenece(String name) {
        this.addChild(new TaskRandomSequence(name));
        return this;
    }

    /**
     * Adds a selector.
     * @return
     */
    public TaskTreeBuilder selector(String name) {
        this.addChild(new TaskSelector(name));
        return this;
    }

    /**
     * Adds a sequence.
     * @return
     */
    public TaskTreeBuilder sequence(String name) {
        this.addChild(new TaskSequence(name));
        return this;
    }

    /**
     * Adds a always fail task.
     * @return
     */
    public TaskTreeBuilder alwaysFail(String name) {
        this.addChild(new TaskAlwaysFail(name));
        return this;
    }

    /**
     * Adds a always succeed task.
     * @return
     */
    public TaskTreeBuilder alwaysSucceed(String name) {
        this.addChild(new TaskAlwaysSucceeds(name));
        return this;
    }

    /**
     * Adds a until fails task.
     * @return
     */
    public TaskTreeBuilder untilFails(String name) {
        this.addChild(new TaskUntilFails(name));
        return this;
    }

    /**
     * Adds a until succeeds task.
     * @return
     */
    public TaskTreeBuilder untilSucceeds(String name) {
        this.addChild(new TaskUntilSucceeds(name));
        return this;
    }

    /**
     * Adds a loop task.
     * @return
     */
    public TaskTreeBuilder loop(String name, int n) {
        this.addChild(new TaskLoop(name, n));
        return this;
    }

    /**
     * Adss a child.
     * @param child
     */
    public TaskTreeBuilder child(Task child) {
        this.currentNode.addChild(child);
        child.setParent(this.currentNode);
        return this;
    }

    /**
     * Adds a predicate.
     * @param predicate
     * @return
     */
    public TaskTreeBuilder filter(Predicate<Task> predicate) {
        this.currentNode.addFilter(predicate);
        return this;
    }

    /**
     * Goes one step end in the tree.
     * @return
     */
    public TaskTreeBuilder end() {

        Assert.assertNotNull("You can't go further end.", this.currentNode.getParent());
        this.currentNode = this.currentNode.getParent();

        return this;
    }

    /**
     * @return Builds the task tree and returns its {@link Task root}.
     */
    public Task build() {
        return this.root;
    }

    private void addChild(Task child) {
        this.currentNode.addChild(child);
        this.currentNode = child;
    }

    public static TaskTreeBuilder create(Task root) {
        return new TaskTreeBuilder(root);
    }

}
