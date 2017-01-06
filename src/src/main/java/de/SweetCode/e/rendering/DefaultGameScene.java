package de.SweetCode.e.rendering;

import de.SweetCode.e.E;
import de.SweetCode.e.Settings;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.rendering.layers.Layers;

import java.awt.*;
import java.util.Random;

/**
 * The default game scene to show the developer if the developer didn't any of his own
 * scenes yet.
 */
public class DefaultGameScene extends GameScene {

    public DefaultGameScene() {}

    @Override
    public void render(Layers layers) {

        Settings settings = E.getE().getSettings();
        ILocation location = new ILocation(settings.getWidth() / 2, settings.getHeight() / 2);

        Graphics2D g = layers.first().g();

        String headline = "Welcome to e.";
        String sub = "1.6021E−19";


        g.setBackground(Color.WHITE);

        Random random = E.getE().getRandom(false);
        g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));

        g.drawString(
                headline,
                location.getX() - g.getFontMetrics().stringWidth(headline) / 2,
                location.getY() - g.getFontMetrics().getHeight() / 2
        );

        g.drawString(
                sub,
                location.getX() - g.getFontMetrics().stringWidth(sub) / 2,
                location.getY() + g.getFontMetrics().getHeight() / 2
        );

    }

    @Override
    public void update(InputEntry input, long delta) {}

    @Override
    public boolean isActive() {
        return true;
    }

}
