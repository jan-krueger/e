package de.SweetCode.e.loop;

import de.SweetCode.e.E;
import de.SweetCode.e.event.Event;
import de.SweetCode.e.event.EventHandler;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class EventLoop extends Loop {

    private final EventHandler eventHandler;

    public EventLoop(EventHandler eventHandler, long optimalIterationTime) {
        super("EventLoop", optimalIterationTime);

        this.eventHandler = eventHandler;
    }

    @Override
    public void tick(long updateLength) {

        Iterator<Event> iterator = this.eventHandler.getQueuedEvents().iterator();

        long maxTime = E.getE().getSettings().getMaxEventHandlerProcessTime();
        long now = System.nanoTime();

        while (iterator.hasNext() && (maxTime > 0)) {
            //@TODO Is this just a cheap @HACK or is that considered okay? The point is that
            // this seems to be kinda to minimalistic for this task but  rewriting the trigger method
            // would just be redundant.
            this.eventHandler.trigger(iterator.next(), false);
            iterator.remove();

            //--- Time
            maxTime -= E.getE().getSettings().getDeltaUnit().convert((System.nanoTime() - now), TimeUnit.NANOSECONDS);
            now = System.nanoTime();
        }

    }

}
