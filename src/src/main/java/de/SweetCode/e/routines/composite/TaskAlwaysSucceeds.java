package de.SweetCode.e.routines.composite;

import de.SweetCode.e.routines.Task;
import de.SweetCode.e.routines.TaskDecorator;
import de.SweetCode.e.routines.TaskStatus;

/**
 * The TaskAlwaysSucceeds task will always succeed no matter the wrapped task succeeds or fails.
 *
 * @param <T>
 */
public class TaskAlwaysSucceeds<T> extends TaskDecorator<T> {

    public TaskAlwaysSucceeds() {}

    public TaskAlwaysSucceeds(String name) {
        this(name, null);
    }

    public TaskAlwaysSucceeds(String name, Task child) {
        super(name, child);
    }

    @Override
    public void child(TaskStatus taskStatus, Task task) {

        switch (taskStatus) {

            // If the child fails, we will succeed...
            case FAILED:
                super.child(TaskStatus.SUCCEEDED, task);
            break;

            // otherwise, just pass it to the super class.
            default:
                super.child(taskStatus, task);
            break;

        }

    }

}
