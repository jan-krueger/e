package test;

import de.SweetCode.e.E;
import de.SweetCode.e.Settings;
import de.SweetCode.e.utils.Version;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) {

        E e = new E(new Settings() {

            @Override
            public String getName() {
                return "E - Music";
            }

            @Override
            public Version getVersion() {
                return new Version(1, 0, 1, Version.ReleaseTag.ALPHA);
            }

            @Override
            public TimeUnit getDeltaUnit() {
                return TimeUnit.MILLISECONDS;
            }

            @Override
            public boolean roundDelta() {
                return true;
            }

            @Override
            public int getWidth() {
                return 1920;
            }

            @Override
            public int getHeight() {
                return 1080;
            }

            @Override
            public int getTargetFPS() {
                return Integer.MAX_VALUE;
            }

            @Override
            public int getLogCapacity() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isDecorated() {
                return true;
            }

            @Override
            public boolean isResizable() {
                return false;
            }

            @Override
            public Map<RenderingHints.Key, Object> getRenderingHints() {
                return new HashMap<RenderingHints.Key, Object>() {{

                    this.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                }};
            }

        });


        e.addScene(new TestScene());
        e.show(TestScene.class);
        e.run();
    }

}
