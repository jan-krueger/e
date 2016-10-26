package test;

import de.SweetCode.e.routines.Task;
import de.SweetCode.e.routines.TaskTreeBuilder;
import de.SweetCode.e.routines.composite.TaskLoop;

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

        /*Random random = new Random();

        Task b = TaskTreeBuilder.create(new TaskRandomSelector<>("start"))
                .filter(e -> (5 > 3))
                .child(new EchoTask("Go to the store"))
                .randomSelector("GoHomeSelector") // Go home
                    .sequence("DoHomeworkSequence") // do homework
                        .filter(e -> random.nextBoolean())
                        .child(new EchoTask("Open & read books"))
                        .child(new EchoTask("Create some valuable notices"))
                        .child(new EchoTask("Done with homeworks"))
                    .end()
                    .child(new EchoTask("Take a shower"))
                    .randomSelector("WatchTVSelector") // Watch TV
                         .child(new EchoTask("Watch old TV shows"))
                         .child(new EchoTask("Watch Netflix"))
                         .child(new EchoTask("Watch Amazon Prime"))
                         .child(new EchoTask("Watch Twitch"))
                     .end()
                .end()
            .build();

        System.out.println(b);
        b.start();*/

        Task c = TaskTreeBuilder.create(new TaskLoop<>("start", 5))
                    .child(new EchoTask("Task"))
                .build();
        c.start();

    }

}
