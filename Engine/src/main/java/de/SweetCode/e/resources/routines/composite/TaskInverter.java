package de.SweetCode.e.resources.routines.composite;

import de.SweetCode.e.resources.routines.Task;
import de.SweetCode.e.resources.routines.TaskDecorator;
import de.SweetCode.e.resources.routines.TaskStatus;

/**
 * The TaskInverter task succeeds if the wrapped class fails and fails if it succeeds.
 *
 */
public class TaskInverter extends TaskDecorator {

    public TaskInverter() {}

    public TaskInverter(String name) {
        this(name, null);
    }

    public TaskInverter(String name, Task child) {
        super(name, child);
    }

    @Override
    public void child(TaskStatus taskStatus, Task task) {

        switch (taskStatus) {

            // If the child succeeds, we will fail...
            case SUCCEEDED:
                super.child(TaskStatus.FAILED, task);
                break;

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
