import de.SweetCode.e.E;
import de.SweetCode.e.GameComponent;
import de.SweetCode.e.input.InputEntry;

public class Test {

    public static void main(String[] args) {

        E e = new E();


        for(int i = 0; i < 1000; i++) e.addComponent(new GameComponent() {
            @Override
            public void update(InputEntry inputEntry, long delta) {

            }

            @Override
            public boolean isActive() {
                return true;
            }
        });

        e.run();

    }

}