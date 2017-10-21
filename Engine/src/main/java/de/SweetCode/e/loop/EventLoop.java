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

        while (iterator.hasNext()) {
            //@TODO Is this just a cheap @HACK or is that considered okay? The point is that
            // this seems to be kinda to minimalistic for this task but  rewriting the trigger method
            // would just be redundant.
            this.eventHandler.trigger(iterator.next(), false);
            iterator.remove();
        }

    }

}
