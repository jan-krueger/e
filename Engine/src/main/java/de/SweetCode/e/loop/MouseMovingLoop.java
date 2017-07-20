package de.SweetCode.e.loop;

import de.SweetCode.e.E;
import de.SweetCode.e.input.entries.MouseMovingEntry;

import java.awt.*;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public class MouseMovingLoop extends Loop {

    private final Queue<MouseMovingEntry> mouseMovingEntries = new LinkedTransferQueue<>();

    private Point lastLocation;

    public MouseMovingLoop(long optimalIterationTime) {
        super("Mouse Moving Listener", optimalIterationTime);
    }

    /**
     * <p>
     *    A queue containing all mouse movements.
     * </p>
     *
     * @return The queue containing the movements, never null.
     */
    public Queue<MouseMovingEntry> getMouseMovingEntries() {
        return this.mouseMovingEntries;
    }

    @Override
    public void tick(long updateLength) {

        Point current = MouseInfo.getPointerInfo().getLocation();

        if(this.lastLocation == null) {
            this.lastLocation = current;
            return;
        }

        //--- Only if moving, if the mouse is still at the same position it didn't move since the last check
        if(this.lastLocation.getX() == current.getX() && this.lastLocation.getY() == current.getY()) {
            return;
        }

        //--- Translate to window coordinates
        Point screen = E.getE().getScreen().getLocation();
        Point windowLocation = new Point(
                (int) (current.getX() - screen.getX()),
                (int) (current.getY() - screen.getY())
        );

        //--- Set the current location to the last location & add a new entry
        this.lastLocation = current;
        this.mouseMovingEntries.add(
            MouseMovingEntry.Builder.create()
                .locationOnScreen(current)
                .point(windowLocation)
            .build()
        );

    }

}
