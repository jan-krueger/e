package de.SweetCode.e.routines.composite;

import de.SweetCode.e.routines.Task;
import de.SweetCode.e.routines.TaskDecorator;
import de.SweetCode.e.routines.TaskStatus;

/**
 *
 * The TaskAlwaysFail will fail no matter the wrapped tasks fails or succeeds.
 *
 * @param <T>
 */
public class TaskAlwaysFail<T> extends TaskDecorator<T> {

    public TaskAlwaysFail() {
        super();
    }

    public TaskAlwaysFail(String name) {
        this(name, null);
    }

    public TaskAlwaysFail(String name, Task<T> task) {
        super(name, task);
    }

    @Override
    public void child(TaskStatus taskStatus, Task<T> task) {

        switch (taskStatus) {

            // If the child succeeds, we will fail...
            case SUCCEEDED:
                super.child(TaskStatus.FAILED, task);
            break;

            // otherwise, just pass it to the super class.
            default:
                super.child(taskStatus, task);
            break;

        }

    }

}
