package de.SweetCode.e.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventHolder implements Comparable<EventHolder> {

    private final EventListener listener;
    private final Method method;
    private final Subscribe subscribe;

    public EventHolder(EventListener listener, Method method, Subscribe subscribe) {
        this.listener = listener;
        this.method = method;
        this.subscribe = subscribe;
    }

    public void execute(Event event) {
        try {
            this.method.invoke(this.listener, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            //@TODO Handle... or do whatever is appropriate.
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(EventHolder o) {

        if(this.subscribe.priority().getPriority() < o.subscribe.priority().getPriority()) {
            return 1;
        } else if(this.subscribe.priority().getPriority() > o.subscribe.priority().getPriority()) {
            return 0;
        }


        return 0;

    }
}
