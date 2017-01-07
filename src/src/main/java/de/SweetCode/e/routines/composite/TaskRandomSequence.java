package de.SweetCode.e.routines.composite;

import de.SweetCode.e.routines.Task;
import de.SweetCode.e.routines.TaskStatus;

import java.util.LinkedList;

/**
 * A TaskRandomSequence is a sequence task's variant that runs its children in a random order until one of it fails.
 */
public class TaskRandomSequence extends TaskRandomSelector {

    public TaskRandomSequence() {}

    public TaskRandomSequence(String name) {
        this(name, new LinkedList<>());
    }

    public TaskRandomSequence(String name, LinkedList<Task> tasks) {
        super(name, tasks);
    }

    @Override
    public void child(TaskStatus taskStatus, Task task) {

        switch (taskStatus) {

            case SUCCEEDED:
                // If the last children succeeded we can run the next child.
                if(!(this.getAvailableIndices().isEmpty()) ){
                    this.setCurrentIndex(this.getRandomIndex());
                    this.run();
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
