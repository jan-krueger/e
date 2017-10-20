package de.SweetCode.e.event;

public class EventCancellable extends Event {

    private boolean isCancelled = false;

    public EventCancellable() {}

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

}
