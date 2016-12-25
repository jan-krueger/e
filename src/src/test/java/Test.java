import de.SweetCode.e.E;
import de.SweetCode.e.Settings;
import de.SweetCode.e.resources.textures.DynamicTextureLoader;
import de.SweetCode.e.resources.textures.TextureLoader;

import java.io.File;

public class Test {

    public static void main(String[] args) {

        E e = new E(new Settings() {
            @Override
            public int getTargetFPS() {
                return 25;
            }
        });
        /*
        YarnJsonParser parser = new YarnJsonParser();
        Dialogue<String> dialogue = null;
        try {
            dialogue = parser.parse("Start", StringUtils.join(Files.readAllLines(new File("C:\\Users\\Yonas\\Downloads\\file.json").toPath()), ""), new TestConditions());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        DialogueNode<String> start = dialogue.getStartNode();
        System.out.println(start.getOptionPointers());*/

        TextureLoader loader = new DynamicTextureLoader(new File("C:\\Users\\Yonas\\Downloads\\test.png"), 481, 464, 10000);

        e.addComponent(loader);
        loader.load();

        e.addScene(new TestScene(loader));
        e.show(TestScene.class);

        e.run();

    }

}
