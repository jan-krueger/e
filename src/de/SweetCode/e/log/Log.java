package de.SweetCode.e.log;

import java.util.Queue;
import java.util.stream.Stream;

public class Log {

    private final Queue<LogEntry> entries;

    /**
     * The log has a limited capacity. If the log hits the limit it deletes the oldest
     * entry in the log.
     * @param capacity
     */
    public Log(int capacity) {
        this.entries = new LifeCycleQueue<>(LogEntry[].class, capacity);
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

}
