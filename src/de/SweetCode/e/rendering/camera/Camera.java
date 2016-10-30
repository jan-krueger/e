package de.SweetCode.e.rendering.camera;

import de.SweetCode.e.GameComponent;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.BoundingBox;
import de.SweetCode.e.math.Location;

import java.awt.event.KeyEvent;

public class Camera implements GameComponent {

    private Location location;
    private BoundingBox box;

    public Camera(Location location, BoundingBox box) {
        this.location = location;
        this.box = box;
    }

    public Location getLocation() {
        return this.location;
    }

    @Override
    public void update(InputEntry input, long delta) {

        input.getKeyEntries()
                .forEach(e -> {

                    switch (e.getKeyCode()) {

                        case KeyEvent.VK_W:
                            this.location.add(0, -0.001 * delta);
                        break;

                        case KeyEvent.VK_S:
                            this.location.add(0, 0.001 * delta);
                            break;

                        case KeyEvent.VK_A:
                            this.location.add(-0.001 * delta, 0);
                        break;

                        case KeyEvent.VK_D:
                            this.location.add(0.001 * delta, 0);
                        break;

                    }

                });

    }

    @Override
    public boolean isActive() {
        return true;
    }

}
