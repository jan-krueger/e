import de.SweetCode.e.E;
import de.SweetCode.e.Settings;

public class Test {

    public static void main(String[] args) {

        E e = new E(new Settings() {
            @Override
            public boolean isResizable() {
                return true;
            }

            @Override
            public boolean isDecorated() {
                return true;
            }

            @Override
            public boolean fixAspectRatio() {
                return true;
            }
        });
        e.run();

    }

}