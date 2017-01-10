import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.Priority;
import de.SweetCode.e.rendering.layers.Layers;

public class Test {

    public static void main(String[] args) {

        E e = new E();
        e.addScene(new A(), Priority.NORMAL);
        e.addScene(new B(), Priority.LOW);
        e.addScene(new C(), Priority.HIGH);
        e.run();

    }

    static class A extends GameScene {

        @Override
        public void update(InputEntry inputEntry, long delta) {

        }

        @Override
        public boolean isActive() {
            return false;
        }

        @Override
        public void render(Layers layers) {

        }
    }

    static class B extends GameScene {

        @Override
        public void update(InputEntry inputEntry, long delta) {

        }

        @Override
        public boolean isActive() {
            return false;
        }

        @Override
        public void render(Layers layers) {

        }
    }

    static class C extends GameScene {

        @Override
        public void update(InputEntry inputEntry, long delta) {

        }

        @Override
        public boolean isActive() {
            return false;
        }

        @Override
        public void render(Layers layers) {

        }
    }

}