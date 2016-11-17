package de.SweetCode.e.utils.log;

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
     * @param capacity
     */
    public Log(int capacity) {
        this.entries = new LifeCycleQueue<>(capacity);
    }

    /**
     * Logs a new entry.
     * @param entry
     */
    public void log(LogEntry entry) {
        this.entries.offer(entry);
    }

    /**
     * A stream of all log entries.
     * @return
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
