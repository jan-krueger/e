import de.SweetCode.e.E;
import de.SweetCode.e.Settings;
import de.SweetCode.e.resources.dialogue.Dialogue;
import de.SweetCode.e.resources.dialogue.DialogueNode;
import de.SweetCode.e.resources.dialogue.parser.YarnJsonParser;
import de.SweetCode.e.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Test {

    public static void main(String[] args) {

        E e = new E(new Settings() {
            @Override
            public int getTargetFPS() {
                return 25;
            }
        });

        YarnJsonParser parser = new YarnJsonParser();
        Dialogue<String> dialogue = null;
        try {
            dialogue = parser.parse("Start", StringUtils.join(Files.readAllLines(new File("C:\\Users\\Yonas\\Downloads\\file.json").toPath()), ""), new TestConditions());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        DialogueNode<String> start = dialogue.getStartNode();
        System.out.println(start.getOptionPointers());

        /*TextureLoader loader = new DynamicTextureLoader(new File("C:\\Users\\Yonas\\Downloads\\test.png"), 481, 464, 10000);


        e.addComponent(loader);
        loader.load();

        e.addScene(new TestScene(loader));
        e.show(TestScene.class);

        e.run();*/

    }

}
