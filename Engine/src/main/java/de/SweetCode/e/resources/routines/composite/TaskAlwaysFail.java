package de.SweetCode.e.resources.routines.composite;

import de.SweetCode.e.resources.routines.Task;
import de.SweetCode.e.resources.routines.TaskDecorator;
import de.SweetCode.e.resources.routines.TaskStatus;

/**
 *
 * The TaskAlwaysFail will fail no matter the wrapped tasks fails or succeeds.
 *
 */
public class TaskAlwaysFail extends TaskDecorator {

    public TaskAlwaysFail() {
        super();
    }

    public TaskAlwaysFail(String name) {
        this(name, null);
    }

    public TaskAlwaysFail(String name, Task task) {
        super(name, task);
    }

    @Override
    public void child(TaskStatus taskStatus, Task task) {

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
