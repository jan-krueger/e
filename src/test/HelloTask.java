package test;

import de.SweetCode.e.entity.Entity;
import de.SweetCode.e.routines.Task;
import de.SweetCode.e.routines.TaskStatus;

public class HelloTask extends Task<Entity> {

    public HelloTask() {
        super("HelloTask");
    }


    @Override
    public void child(TaskStatus taskStatus, Task<Entity> task) {


    }

    @Override
    public void run() {

        System.out.println("Hello #1");
        success();

    }

}
