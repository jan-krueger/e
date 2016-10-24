package test;

import de.SweetCode.e.entity.Entity;
import de.SweetCode.e.routines.Task;

public class MoveTask extends Task<Entity> {

    public MoveTask() {
        super("Move Task");
    }

    @Override
    public void run() {

        System.out.println("call");
        success();

    }

}
