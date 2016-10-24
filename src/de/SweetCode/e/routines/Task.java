package de.SweetCode.e.routines;

import de.SweetCode.e.RunComponent;
import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.util.LinkedList;
import java.util.List;

public abstract class Task<T> implements RunComponent {

    /**
     * The name of the task.
     */
    private final String name;

    /**
     * It's the parent parent of the task.
     */
    private Task parent;

    /**
     * It's current taskStatus.
     */
    private TaskStatus taskStatus = TaskStatus.FRESH;

    /**
     * This list keeps track of all children.
     */
    private List<Task<T>> children = new LinkedList<>();

    public Task() {
        this(null);
    }

    public Task(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the name, null when no name provided.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the current status of the task.
     * @return
     */
    public TaskStatus getTaskStatus() {
        return this.taskStatus;
    }

    /**
     * This method returns a stream of all children.
     *
     * @return
     */
    public List<Task<T>> getChildren() {
        return this.children;
    }

    /**
     * Returns a child by its index.
     * @param index
     * @return
     */
    public Task<T> getChild(int index) {
        return this.children.get(index);
    }

    /**
     * This method will add a child to the list of this task's children.
     *
     * @param child
     */
    public void addChild(Task<T> child) {
        //@TODO Check if one of the parents has this child's instance already as child/parent, if this is the case
        // the child cannot be added again, because e.g. this would cause an endless loop in the cancel method.

        child.setParent(this);
        this.children.add(child);
    }

    /**
     * Returns its children.
     * @return
     */
    public Task<T> getParent() {
        return this.parent;
    }

    /**
     * Sets the parent of the task.
     * @param parent
     */
    public void setParent(Task<T> parent) {
        this.parent = parent;
    }

    /**
     * Checks if the task is the given taskStatus.
     * @param taskStatus The taskStatus to check against.
     * @return true if the task is in the given taskStatus, otherwise false.
     */
    public boolean is(TaskStatus taskStatus) {
        return this.taskStatus == taskStatus;
    }

    /**
     * This method will be called to request a rerun & wants to keep running.
     */
    public final void running() {

        if(!(this.parent == null)) {
            this.taskStatus = TaskStatus.RUNNING;
            this.parent.child(this.taskStatus, this);
        }

    }

    /**
     * This method will be called to inform its parent that it succeeded.
     */
    public final void success() {

        this.taskStatus = TaskStatus.SUCCEEDED;

        if(!(this.parent == null)) {
            this.parent.child(this.taskStatus, this);
        }

    }

    /**
     * This method will be called to inform its parent that it failed.
     */
    public final void fail() {

        this.taskStatus = TaskStatus.FAILED;

        if(!(this.parent == null)) {
            this.parent.child(this.taskStatus, this);
        }

    }

    /**
     * This task and all it's children will be cancelled if the current task is running.
     */
    public final void cancel() {

        this.taskStatus = TaskStatus.CANCELLED;

        if(this.is(TaskStatus.RUNNING)) {
            this.children.stream().forEach(Task::cancel);
        }

    }

    /**
     * Starts the task and calls the run method, if the current status is running you won't be able to call this method.
     * If it is any other mode than FRESH the reset method will be called.
     */
    public void start() {

        // We don't wanna interrupt running tasks.
        if(this.is(TaskStatus.RUNNING)) {
            return;
        }

        // Reset
        if(!(this.is(TaskStatus.FRESH))) {
            this.reset();
        }

        this.run();

    }

    /**
     * Resets this task to make it restart from scratch on next run.
     */
    public void reset() {

        if(this.is(TaskStatus.RUNNING)) {
            this.cancel();
        }

        this.children.stream().forEach(Task::reset);
        this.taskStatus = TaskStatus.FRESH;

    }

    /**
     * This metod will be called if the task:
     *  - RUNNING when one of the ancestors of this task needs to run again.
     *  - SUCCEEDED when one of the children of this task succeeds.
     *  - FAILED when one of the children of this task fails.
     *
     * @param taskStatus The taskStatus of the task.
     * @param task The task that called this method.
     */
    public void child(TaskStatus taskStatus, Task<T> task) {}

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("name", this.name)
                .append("taskStatus", this.taskStatus.name())
                .append("parent", (this.parent == null ? null : this.parent.getName()))
                .append("children", this.children)
            .build();
    }

}
