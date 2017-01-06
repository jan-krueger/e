import de.SweetCode.e.E;
import de.SweetCode.e.Settings;

public class Test {

    public static void main(String[] args) {

        E e = new E(new Settings() {
            @Override
            public boolean isParallelizingUpdate() {
                return false;
            }

            @Override
            public int getTargetFPS() {
                return 1;
            }
        });
        e.run();

    }

}