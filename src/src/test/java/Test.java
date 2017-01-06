import de.SweetCode.e.E;
import de.SweetCode.e.GameComponent;
import de.SweetCode.e.Settings;
import de.SweetCode.e.input.InputEntry;

public class Test {

    public static void main(String[] args) {

        E e = new E(new Settings() {
            @Override
            public boolean isParallelizingUpdate() {
                return false;
            }
        });
        e.run();

    }

}