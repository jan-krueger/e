package de.SweetCode.e.routines.composite;

import de.SweetCode.e.routines.Task;
import de.SweetCode.e.routines.TaskStatus;

import java.util.LinkedList;

/**
 * A sequence is a task that runs each of its child behaviors in turn. It will return immediately with a failure
 * status code when one of its children fails. As long as its children are succeeding, it will keep on running the tasks.
 * If it runs out of children completely, it will return a succeeded status code
 * @param <T>
 */
public class TaskSequence<T> extends TaskSelector<T> {

    public TaskSequence() {}

    public TaskSequence(String name) {
        this(name, new LinkedList<>());
    }

    public TaskSequence(String name, LinkedList<Task<T>> tasks) {
        super(name, tasks);
    }

    @Override
    public void child(TaskStatus taskStatus, Task<T> task) {

        switch (taskStatus) {

            case SUCCEEDED:

                // If the last children succeeded we can run the next child start to run.
                if(this.getCurrentIndex() + 1 < this.getChildren().size()) {
                    this.setCurrentIndex(this.getCurrentIndex() + 1);
                    this.start();
                }
                // If we are done, we successfully completed all child tasks.
                else {
                    this.success();
                }

            break;

            case FAILED:

                // If one tasks fails then we are done.
                this.fail();

            break;

        }

    }

}
