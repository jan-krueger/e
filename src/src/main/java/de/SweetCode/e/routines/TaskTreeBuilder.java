package de.SweetCode.e.routines;

import de.SweetCode.e.routines.composite.*;
import de.SweetCode.e.utils.Assert;

import java.util.function.Predicate;

public class TaskTreeBuilder<T> {

    private final Task<T> root;
    private Task<T> currentNode;

    public TaskTreeBuilder(Task<T> root) {
        Assert.assertNotNull("The root cannot be null.", root);

        this.root = root;
        this.currentNode = root;
    }

    /**
     * Adds a random selector.
     * @return
     */
    public TaskTreeBuilder<T> randomSelector(String name) {
        this.addChild(new TaskRandomSelector<>(name));
        return this;
    }

    /**
     * Adds a random sequence.
     * @return
     */
    public TaskTreeBuilder<T> randomSequenece(String name) {
        this.addChild(new TaskRandomSequence<>(name));
        return this;
    }

    /**
     * Adds a selector.
     * @return
     */
    public TaskTreeBuilder<T> selector(String name) {
        this.addChild(new TaskSelector<>(name));
        return this;
    }

    /**
     * Adds a sequence.
     * @return
     */
    public TaskTreeBuilder<T> sequence(String name) {
        this.addChild(new TaskSequence<>(name));
        return this;
    }

    /**
     * Adds a always fail task.
     * @return
     */
    public TaskTreeBuilder<T> alwaysFail(String name) {
        this.addChild(new TaskAlwaysFail<>(name));
        return this;
    }

    /**
     * Adds a always succeed task.
     * @return
     */
    public TaskTreeBuilder<T> alwaysSucceed(String name) {
        this.addChild(new TaskAlwaysSucceeds<>(name));
        return this;
    }

    /**
     * Adds a until fails task.
     * @return
     */
    public TaskTreeBuilder<T> untilFails(String name) {
        this.addChild(new TaskUntilFails<>(name));
        return this;
    }

    /**
     * Adds a until succeeds task.
     * @return
     */
    public TaskTreeBuilder<T> untilSucceeds(String name) {
        this.addChild(new TaskUntilSucceeds<>(name));
        return this;
    }

    /**
     * Adds a loop task.
     * @return
     */
    public TaskTreeBuilder<T> loop(String name, int n) {
        this.addChild(new TaskLoop<>(name, n));
        return this;
    }

    /**
     * Adss a child.
     * @param child
     */
    public TaskTreeBuilder<T> child(Task<T> child) {
        this.currentNode.addChild(child);
        child.setParent(this.currentNode);
        return this;
    }

    /**
     * Adds a predicate.
     * @param predicate
     * @return
     */
    public TaskTreeBuilder<T> filter(Predicate<Task<T>> predicate) {
        this.currentNode.addFilter(predicate);
        return this;
    }

    /**
     * Goes one step end in the tree.
     * @return
     */
    public TaskTreeBuilder<T> end() {

        Assert.assertNotNull("You can't go further end.", this.currentNode.getParent());
        this.currentNode = this.currentNode.getParent();

        return this;
    }

    /**
     * Builds the task tree.
     * @return
     */
    public Task<T> build() {
        return this.root;
    }

    private void addChild(Task<T> child) {
        this.currentNode.addChild(child);
        this.currentNode = child;
    }

    public static <T> TaskTreeBuilder<T> create(Task<T> root) {
        return new TaskTreeBuilder<>(root);
    }

}
