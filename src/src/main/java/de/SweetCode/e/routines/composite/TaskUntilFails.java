package de.SweetCode.e.routines.composite;

import de.SweetCode.e.routines.Task;
import de.SweetCode.e.routines.TaskDecorator;
import de.SweetCode.e.routines.TaskStatus;

public class TaskUntilFails<T> extends TaskDecorator<T> {

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
