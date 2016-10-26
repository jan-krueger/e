package test;

import de.SweetCode.e.routines.Task;

import java.util.Random;

public class EchoTask extends Task {

    private final static Random random = new Random();
    private String message;

    public EchoTask(String message) {
        super("EchoTask");
        this.message = message;
    }

    @Override
    public void run() {

        if(random.nextDouble() < 0.99D) {
            success();
        } else {
            fail();
        }


    }

}
