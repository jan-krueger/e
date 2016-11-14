import de.SweetCode.e.E;
import de.SweetCode.e.Settings;
import de.SweetCode.e.resources.textures.DynamicTextureLoader;
import de.SweetCode.e.resources.textures.TextureLoader;

import java.io.File;

public class Test {

    public static void main(String[] args) {


        TextureLoader loader = new DynamicTextureLoader(new File("C:\\Users\\Yonas\\Downloads\\test.png"), 481, 464, 10000);

        E e = new E(new Settings() {
            @Override
            public int getTargetFPS() {
                return 25;
            }
        });
        e.addComponent(loader);
        loader.load();

        e.addScene(new TestScene(loader));
        e.show(TestScene.class);

        e.run();

    }

}
