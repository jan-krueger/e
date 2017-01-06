import de.SweetCode.e.E;
import de.SweetCode.e.GameComponent;
import de.SweetCode.e.input.InputEntry;

public class Test {

    public static void main(String[] args) {

        E e = new E();
        e.addComponent(new GameComponent() {
            @Override
            public void update(InputEntry inputEntry, long delta) {

                System.out.println(delta);

            }

            @Override
            public boolean isActive() {
                return true;
            }
        });
        e.run();

    }

}