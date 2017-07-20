package de.SweetCode.e.utils.log;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.sql.Timestamp;
import java.util.Date;

public class LogEntry {

    private final String message;
    private final Class clazz;
    private final Timestamp timestamp = new Timestamp(new Date().getTime());

    /**
     * @param clazz The clazz we are logging from.
     * @param message Message to appear in the log.
     */
    public LogEntry(Class clazz, String message) {
        this.clazz = clazz;
        this.message = message;
    }

    /**
     * @return The clazz which logged this message.
     */
    public Class getClazz() {
        return this.clazz;
    }

    /**
     * @return Gives the message of the entry.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @return Gives the time when the log entry was created.
     */
    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("source", this.clazz.getSimpleName())
                .append("message", this.message)
                .append("timestamp", this.timestamp.toString())
            .build();
    }

    public static class Builder {

        private Class clazz;
        private LogPrefix prefix;
        private String message;

        public Builder(Class clazz) {
            this.clazz = clazz;
        }

        public static Builder create(Class clazz) {
            return new Builder(clazz);
        }

        public Builder prefix(LogPrefix prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder message(String format, Object... objects) {
            this.message = String.format(format, objects);
            return this;
        }

        public LogEntry build() {
            return new LogEntry(this.clazz, String.format("[%s] %s", this.prefix.prefix(), this.message));
        }

    }

}
