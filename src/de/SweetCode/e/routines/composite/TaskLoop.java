package de.SweetCode.e.routines.composite;

import de.SweetCode.e.routines.Task;
import de.SweetCode.e.routines.TaskDecorator;
import de.SweetCode.e.routines.TaskStatus;
import de.SweetCode.e.utils.Assert;

public class TaskLoop<T> extends TaskDecorator<T> {

    private final int n;
    private int x = 0;

    public TaskLoop(int n) {
        this(null, n);
    }

    public TaskLoop(String name, int n) {
        this(name, null, n);
    }

    public TaskLoop(String name, Task<T> task, int n) {

        super(name, task);

        Assert.assertTrue("n cannot be less than 1.", n > 0);
        this.n = n;

    }

    @Override
    public void reset() {
        super.reset();

        this.x = 0;
    }

    @Override
    public void child(TaskStatus taskStatus, Task<T> task) {

        this.x++;

        if(this.x >= this.n) {
            this.success();
        } else {
            this.getChild(0).start();
        }

    }

}
