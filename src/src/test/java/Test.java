import de.SweetCode.e.E;
import de.SweetCode.e.GameComponent;
import de.SweetCode.e.Settings;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.utils.Platform;

public class Test {

    public static void main(String[] args) {

        System.out.println(Platform.getPlatform().name());
        E e = new E(new Settings() {
            @Override
            public int getTargetFPS() {
                return 10;
            }
        });
        e.addScene(new TestScene());
        e.show(TestScene.class);
        e.addComponent(new GameComponent() {
            @Override
            public void update(InputEntry inputEntry, long delta) {
                System.out.println("update game-component");
            }

            @Override
            public boolean isActive() {
                return true;
            }
        });
        e.run();

    }

}