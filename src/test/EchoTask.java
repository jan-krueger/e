package test;

import de.SweetCode.e.routines.Task;

public class EchoTask extends Task {

    private String message;

    public EchoTask(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        System.out.println(this.message);
        success();
    }

}
