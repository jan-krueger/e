package de.SweetCode.e.routines.composite;

import de.SweetCode.e.routines.Task;
import de.SweetCode.e.routines.TaskStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A TaskRandomSelector is a selector task's variant that runs its children in a random order until one of it succeeds.
 * @param <T>
 */
public class TaskRandomSelector<T> extends TaskSelector<T> {

    private List<Integer> availableIndices = new ArrayList<>();

    public TaskRandomSelector() {}

    public TaskRandomSelector(List<Task<T>> tasks) {
        super(tasks);
    }

    /**
     * Returns the list of all available indices.
     * @return
     */
    protected List<Integer> getAvailableIndices() {
        return this.availableIndices;
    }

    /**
     * Returns a random index and removes it from the available indices.
     * @return
     */
    protected int getRandomIndex() {
        return this.availableIndices.remove(0);
    }

    @Override
    public void start() {

        IntStream.range(0, this.getChildren().size())
                .forEach(this.availableIndices::add);
        Collections.shuffle(this.availableIndices);
        this.setCurrentIndex(this.getRandomIndex());

        super.start();

    }

    @Override
    public void reset() {

        super.reset();
        this.setCurrentIndex(this.getRandomIndex());

    }

    @Override
    public void child(TaskStatus taskStatus, Task<T> task) {

        switch (taskStatus) {

            case SUCCEEDED:

                // If one child succeeded, we are done :)
                this.success();

                break;

            case FAILED:

                // If it failed and we still have some indices left we can go on.
                if(!(this.availableIndices.isEmpty())) {
                    this.setCurrentIndex(this.getRandomIndex());
                    this.run();
                }
                // or, if all children failed to execute there task we will also fail.
                else {
                    this.fail();
                }

                break;

        }

    }

}
