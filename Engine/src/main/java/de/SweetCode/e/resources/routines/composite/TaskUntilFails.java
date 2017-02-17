package de.SweetCode.e.resources.routines.composite;

import de.SweetCode.e.resources.routines.Task;
import de.SweetCode.e.resources.routines.TaskDecorator;
import de.SweetCode.e.resources.routines.TaskStatus;

public class TaskUntilFails extends TaskDecorator {

    public TaskUntilFails() {
        super();
    }

    public TaskUntilFails(String name) {
        this(name, null);
    }

    public TaskUntilFails(String name, Task task) {
        super(name, task);
    }

    @Override
    public void child(TaskStatus taskStatus, Task task) {

        switch (taskStatus) {

            case SUCCEEDED:
                this.getChild(0).start();
                break;

            default:
                super.child(taskStatus, task);
                break;

        }

    }

}
