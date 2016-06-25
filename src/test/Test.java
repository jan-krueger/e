package test;

import de.SweetCode.e.E;
import de.SweetCode.e.Settings;
import de.SweetCode.e.utils.Version;

import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) {

        E e = new E(new Settings() {

            @Override
            public String getName() {
                return "E";
            }

            @Override
            public Version getVersion() {
                return new Version(1, 0, 1, Version.ReleaseTag.ALPHA);
            }

            @Override
            public TimeUnit getDeltaUnit() {
                return TimeUnit.NANOSECONDS;
            }

            @Override
            public int getWidth() {
                return 1280;
            }

            @Override
            public int getHeight() {
                return 720;
            }

            @Override
            public int getTargetFPS() {
                return 25;
            }

            @Override
            public int getLogCapacity() {
                return 1000;
            }

            @Override
            public boolean isDecorated() {
                return true;
            }

            @Override
            public boolean isResizable() {
                return true;
            }

        });

        e.addScene(new TestScene());
        e.show(TestScene.class);
        e.run();
    }

}
