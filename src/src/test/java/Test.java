import de.SweetCode.e.E;
import de.SweetCode.e.GameComponent;
import de.SweetCode.e.Settings;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.Priority;

public class Test {

    public static void main(String[] args) {

        E e = new E(new Settings() {
            @Override
            public boolean isParallelizingUpdate() {
                return false;
            }
        });


        for(int i = 0; i < 10; i++) e.addComponent(new GameComponent() {
            @Override
            public void update(InputEntry inputEntry, long delta) {

                for(int i = 0; i < 10000000; i++) {
                    double x = Math.pow(10, 100) / Math.acos(2);
                }

            }

            @Override
            public boolean isActive() {
                return true;
            }
        }, Priority.values()[E.getE().getRandom(false).nextInt(Priority.values().length)]);

        e.run();

    }

}