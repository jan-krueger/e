package de.SweetCode.e.resources.routines.composite;

import de.SweetCode.e.resources.routines.Task;
import de.SweetCode.e.resources.routines.TaskDecorator;
import de.SweetCode.e.resources.routines.TaskStatus;

/**
 * The TaskAlwaysSucceed task will always succeed no matter the wrapped task succeeds or fails.
 *
 */
public class TaskAlwaysSucceed extends TaskDecorator {

    public TaskAlwaysSucceed() {}

    public TaskAlwaysSucceed(String name) {
        this(name, null);
    }

    public TaskAlwaysSucceed(String name, Task child) {
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
