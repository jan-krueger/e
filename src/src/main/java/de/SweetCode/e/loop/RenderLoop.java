package de.SweetCode.e.loop;

import de.SweetCode.e.E;
import de.SweetCode.e.EScreen;

public class RenderLoop extends Loop {

    private final EScreen screen;

    public RenderLoop(EScreen screen, long optimalTime) {
        super("Render Loop", optimalTime);

        this.screen  = screen;
    }

    @Override
    public void tick(long updateLength) {

        E.getE().getScenes().forEach((k, v) -> {

            if (v.getGameScene().isActive()) {
                this.screen.invalidate();
                this.screen.repaint();
            }

        });

    }


}
