import de.SweetCode.e.E;
import de.SweetCode.e.Settings;
import de.SweetCode.e.utils.Platform;

public class Test {

    public static void main(String[] args) {

        System.out.println(Platform.getPlatform().name());

        E e = new E(new Settings() {
            @Override
            public int getTargetFPS() {
                return 200;
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

        e.addScene(new TestScene());
        e.show(TestScene.class);

        e.run();

    }

}
