package test;

import de.SweetCode.e.entity.Entity;
import de.SweetCode.e.routines.Task;
import de.SweetCode.e.routines.TaskTreeBuilder;
import de.SweetCode.e.routines.composite.TaskSequence;

public class Test {

    public static void main(String[] args) {

        /*Task a = new TaskSequence(new LinkedList<>(Arrays.asList(
                new TestTask(),
                new MoveTask()
        )));

        TaskSelector taskSelector = new TaskSequence(new LinkedList<>(Arrays.asList(
           new MoveTask(),
           new TestTask(),
           a
        )));

        taskSelector.start();
        */


        Task<Entity> b = TaskTreeBuilder.<Entity>create(new TaskSequence<>())
                    .child(new MoveTask())
                 .randomSelector()
                     .child(new TestTask())
                     .child(new HelloTask())
                .back()
                    .randomSequenece()
                    .child(new TestTask())
                    .child(new MoveTask())
                    .child(new HelloTask())
                    .child(new HelloTask())
                .build();
        System.out.println(b);
        b.start();

    }

}
