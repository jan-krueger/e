package test;

import de.SweetCode.e.routines.Task;
import de.SweetCode.e.routines.TaskTreeBuilder;
import de.SweetCode.e.routines.composite.TaskRandomSelector;

import java.util.Random;

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

        Random random = new Random();

        Task b = TaskTreeBuilder.create(new TaskRandomSelector<>())
                .filter(e -> (5 > 3))
                .child(new EchoTask("Go to the store"))
                .randomSelector() // Go home
                    .sequence() // do homework
                        .filter(e -> random.nextBoolean())
                        .child(new EchoTask("Open & read books"))
                        .child(new EchoTask("Create some valuable notices"))
                        .child(new EchoTask("Done with homeworks"))
                        .back()
                    .child(new EchoTask("Take a shower"))
                    .randomSelector() // Watch TV
                         .child(new EchoTask("Watch old TV shows"))
                         .child(new EchoTask("Watch Netflix"))
                         .child(new EchoTask("Watch Amazon Prime"))
                         .child(new EchoTask("Watch Twitch"))
                         .back()
                    .back()
                .build();

        System.out.println(b);
        b.start();

    }

}
