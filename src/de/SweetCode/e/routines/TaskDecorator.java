package de.SweetCode.e.routines;

public abstract class TaskDecorator<T> extends Task<T> {

    /**
     * The child task wrapped by this decorator.
     */
    private Task<T> child;

    public TaskDecorator() {}

    public TaskDecorator(String name) {
        this(name, null);
    }

    public TaskDecorator(String name, Task<T> child) {
        super(name);

        this.child = child;
    }

    @Override
    public int getChildAmount() {
        return (this.child == null ? 0 : 1);
    }

    @Override
    public Task<T> getChild(int index) {

        if(this.child == null) {
            throw new IndexOutOfBoundsException("The task has no child.");
        }

        if(!(index == 0)) {
            throw new IndexOutOfBoundsException("Index must be 0.");
        }

        return this.child;
    }

    @Override
    public void addChild(Task<T> child) {

        if(!(this.child == null)) {
            throw new IllegalStateException("A decorator task cannot have more than one child.");
        }

        this.child = child;

    }

    @Override
    public void run() {

        if(this.child.is(TaskStatus.RUNNING)) {
            this.child.run();
        } else {
            this.child.start();
        }

    }

    @Override
    public void child(TaskStatus taskStatus, Task<T> task) {

        switch (taskStatus) {

            case RUNNING:
                this.running();
            break;

            case FAILED:
                this.fail();
            break;

            case SUCCEEDED:
                this.success();
            break;

        }

    }
}
