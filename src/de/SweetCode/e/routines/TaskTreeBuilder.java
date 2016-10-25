package de.SweetCode.e.routines;

import de.SweetCode.e.routines.composite.TaskRandomSelector;
import de.SweetCode.e.routines.composite.TaskRandomSequence;
import de.SweetCode.e.routines.composite.TaskSelector;
import de.SweetCode.e.routines.composite.TaskSequence;
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
    public TaskTreeBuilder<T> randomSelector() {
        this.addChild(new TaskRandomSelector<>());
        return this;
    }

    /**
     * Adds a random sequence.
     * @return
     */
    public TaskTreeBuilder<T> randomSequenece() {
        this.addChild(new TaskRandomSequence<>());
        return this;
    }

    /**
     * Adds a selector.
     * @return
     */
    public TaskTreeBuilder<T> selector() {
        this.addChild(new TaskSelector<>());
        return this;
    }

    /**
     * Adds a sequence.
     * @return
     */
    public TaskTreeBuilder<T> sequence() {
        this.addChild(new TaskSequence<>());
        return this;
    }

    /**
     * Adss a child.
     * @param child
     */
    public TaskTreeBuilder<T> child(Task<T> child) {
        this.currentNode.addChild(child);
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
     * Goes one step back in the tree.
     * @return
     */
    public TaskTreeBuilder<T> back() {

        Assert.assertNotNull("You can't go further back.", this.currentNode.getParent());
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
