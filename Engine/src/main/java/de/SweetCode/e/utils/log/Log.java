package de.SweetCode.e.utils.log;

import de.SweetCode.e.E;
import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.util.Queue;
import java.util.stream.Stream;

/**
 * Log allows you to keep track of various events.
 */
public class Log {

    private final Queue<LogEntry> entries;

    /**
     * The log has a limited capacity. If the log hits the limit it deletes the oldest
     * entry in the log.
     * @param capacity The maximum of elements that can be in the log.
     */
    public Log(int capacity) {
        this.entries = new LifeCycleQueue<>(capacity);
    }

    /**
     * Logs a new entry.
     * @param entry The {@link LogEntry} to add.
     */
    public void log(LogEntry entry) {
        this.entries.offer(entry);

        if(E.getE().getSettings().isDebugging()) {
            System.out.println(entry.toString());
        }
    }

    /**
     * @return A {@link Stream} of all {@link LogEntry log entries}.
     */
    public Stream<LogEntry> getLog() {
        return this.entries.stream();
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("entries", this.entries)
            .build();
    }

}
