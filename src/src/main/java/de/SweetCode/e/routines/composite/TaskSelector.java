package de.SweetCode.e.routines.composite;

import de.SweetCode.e.routines.Task;
import de.SweetCode.e.routines.TaskStatus;

import java.util.Arrays;
import java.util.List;

/**
 * A selector is a task that runs each of its child behaviors in turn. It will return immediately with a success
 * status code when one of its children runs successfully. As long as its children are failing, it will keep on trying.
 * If it runs out of children completely, it will return a failure status code
 */
public class TaskSelector extends Task {

    /**
     * The index of the currently running child.
     */
    private int currentIndex = 0;

    public TaskSelector() {}

    public TaskSelector(String name) {
        this(name, Arrays.asList());
    }

    public TaskSelector(String name, List<Task> tasks) {
        super(name);

        tasks.forEach(this::addChild);
    }

    /**
     * @return Returns a reference to the currently running {@link Task child}.
     */
    public Task getCurrent() {
        return this.getChild(this.currentIndex);
    }

    /**
     * @return Returns a index of the currently running child. Always a positive integer.
     */
    public int getCurrentIndex() {
        return this.currentIndex;
    }

    /**
     * Sets the current index.
     * @param currentIndex Sets the index of the currently running child.
     */
    protected void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    @Override
    public void reset() {

        super.reset();
        this.currentIndex = 0;

    }

    @Override
    public void run() {

        // Run the current children
        this.getCurrent().start();

    }

    @Override
    public void child(TaskStatus taskStatus, Task task) {

        switch (taskStatus) {

            case SUCCEEDED:

                // If one child succeeded, we are done :)
                this.success();

            break;

            case FAILED:

                // If it failed and we still have some let's we will try the next child.
                if(this.currentIndex++ < this.getChildren().size()) {
                    this.run();
                }
                // or, if all children failed to execute there task we will also fail.
                else {
                    this.fail();
                }

            break;

        }

    }

}
