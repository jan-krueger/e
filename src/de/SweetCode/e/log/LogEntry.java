package de.SweetCode.e.log;

import de.SweetCode.e.utils.ToStringBuilder;

import java.util.Date;
import java.sql.Timestamp;

public class LogEntry {

    private final String message;
    private final Timestamp timestamp = new Timestamp(new Date().getTime());

    public LogEntry(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append(this)
                .toString();
    }

    public static class Builder {

        private String message;

        public Builder() {}

        public static Builder create() {
            return new Builder();
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public LogEntry build() {
            return new LogEntry(this.message);
        }

    }

}
