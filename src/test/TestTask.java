package test;

import de.SweetCode.e.entity.Entity;
import de.SweetCode.e.routines.Task;

public class TestTask extends Task<Entity> {

    public TestTask() {
        super("Test Task");
    }

    @Override
    public void run() {

        System.out.println("TestTask");
        success();

    }


}
