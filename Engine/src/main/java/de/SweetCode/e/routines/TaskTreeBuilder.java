package de.SweetCode.e.routines;

import de.SweetCode.e.routines.composite.*;
import de.SweetCode.e.utils.Assert;

import java.util.function.Predicate;

public class TaskTreeBuilder {

    private final Task root;
    private Task currentNode;

    private TaskTreeBuilder(Task root) {
        Assert.assertNotNull("The root cannot be null.", root);

        this.root = root;
        this.currentNode = root;
    }

    /**
     * Adds a new {@link TaskRandomSelector}.
     *
     * @param name The name of the {@link TaskRandomSelector}.
     * @return The reference to the used builder.
     */
    public TaskTreeBuilder randomSelector(String name) {
        this.addChild(new TaskRandomSelector(name));
        return this;
    }

    /**
     * Adds a {@link TaskRandomSequence}.
     *
     * @param name The name of the {@link TaskRandomSequence}.
     * @return The reference to the used builder.
     */
    public TaskTreeBuilder randomSequenece(String name) {
        this.addChild(new TaskRandomSequence(name));
        return this;
    }

    /**
     * Adds a {@link TaskSelector}.
     *
     * @param name The name of the {@link TaskSelector}.
     * @return The reference to the used builder.
     */
    public TaskTreeBuilder selector(String name) {
        this.addChild(new TaskSelector(name));
        return this;
    }

    /**
     * Adds a {@link TaskSequence}.
     *
     * @param name The name of the {@link TaskSequence}.
     * @return The reference to the used builder.
     */
    public TaskTreeBuilder sequence(String name) {
        this.addChild(new TaskSequence(name));
        return this;
    }

    /**
     * Adds a {@link TaskAlwaysFail}.
     *
     * @param name The name of the {@link TaskAlwaysFail}.
     * @return The reference to the used builder.
     */
    public TaskTreeBuilder alwaysFail(String name) {
        this.addChild(new TaskAlwaysFail(name));
        return this;
    }

    /**
     * Adds a {@link TaskAlwaysSucceed}.
     *
     * @param name The name of the {@link TaskAlwaysSucceed}.
     * @return The reference to the used builder.
     */
    public TaskTreeBuilder alwaysSucceed(String name) {
        this.addChild(new TaskAlwaysSucceed(name));
        return this;
    }

    /**
     * Adds a {@link TaskUntilFails}.
     *
     * @param name The name of the {@link TaskUntilFails}.
     * @return The reference to the used builder.
     */
    public TaskTreeBuilder untilFails(String name) {
        this.addChild(new TaskUntilFails(name));
        return this;
    }

    /**
     * Adds a {@link TaskUntilSucceeds}.
     *
     * @param name The name of the {@link TaskUntilSucceeds}.
     * @return The reference to the used builder.
     */
    public TaskTreeBuilder untilSucceeds(String name) {
        this.addChild(new TaskUntilSucceeds(name));
        return this;
    }

    /**
     * Adds a {@link TaskLoop}.
     *
     * @param name The name of the {@link TaskLoop}.
     * @param n The amount of iterations.
     * @return The reference to the used builder.
     */
    public TaskTreeBuilder loop(String name, int n) {
        this.addChild(new TaskLoop(name, n));
        return this;
    }

    /**
     * Adds a {@link Task}.
     * @param child The child to add.
     * @return The reference to the used builder.
     */
    public TaskTreeBuilder child(Task child) {
        this.currentNode.addChild(child);
        child.setParent(this.currentNode);
        return this;
    }

    /**
     * Adds a {@link Predicate} as filter.
     * @param predicate The predicate.
     * @return The reference to the used builder.
     */
    public TaskTreeBuilder filter(Predicate<Task> predicate) {
        this.currentNode.addFilter(predicate);
        return this;
    }

    /**
     * Goes one step end in the tree.
     * @return The reference to the used builder.
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
