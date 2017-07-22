import de.SweetCode.e.E;
import de.SweetCode.e.Settings;
import de.SweetCode.e.resources.file.HotSwapFile;

public class Test  {

    public static void main(String[] args) {
        E e = new E(new Settings() {
            @Override
            public boolean isHotSwapEnabled() {
                return true;
            }

            @Override
            public boolean isDebugging() {
                return true;
            }
        });

        new HotSwapFile("C:\\Users\\Yonas\\Downloads\\A.txt");


        e.run();
    }


}