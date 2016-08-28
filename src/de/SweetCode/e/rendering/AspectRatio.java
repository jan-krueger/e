package de.SweetCode.e.rendering;

import de.SweetCode.e.math.BoundingBox;

import java.awt.*;

public class AspectRatio {

    private Dimension render;
    private Dimension window;

    public AspectRatio(Dimension render, Dimension window) {
        this.render = render;
        this.window = window;
    }

    public BoundingBox getOptimal() {

        return new BoundingBox(
                (window.getWidth() - render.getWidth()) / 2,
                (window.getHeight() - render.getHeight()) / 2,
                -1,
                -1
        );

        /*int optimalWindowWidth = (int) (window.getHeight() * (setScene.getWidth() / setScene.getHeight()));
        int optimalWindowHeight = (int) (window.getWidth() * (setScene.getHeight() / setScene.getWidth()));

        if(optimalWindowWidth > window.getWidth()) {

            // width-constrained display - top and bottom black bars

            double minX = 0;
            double maxX = window.getWidth();

            double emptySpace = window.getHeight() - optimalWindowHeight;
            double halfSpace = (emptySpace / 2D);

            double minY = halfSpace;
            double maxY = halfSpace + optimalWindowHeight;

            return new BoundingBox(new Location(minX, minY), new Location(maxX, maxY));

        } else {
            // height-constrained display - left and right black bars
            double minY = 0;
            double maxY = window.getHeight();

            double emptySpace = window.getWidth() - optimalWindowWidth;
            double halfSpace = (emptySpace / 2D);

            double minX = halfSpace;
            double maxX = halfSpace * 2;

            return new BoundingBox(new Location(minX, minY), new Location(maxX, maxY));

        }*/

    }

}
