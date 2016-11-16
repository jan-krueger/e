package de.SweetCode.e.rendering;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

/**
 * Priority is a general class used in all
 * cases where the developer can or should
 * provide a priority value.
 */
public enum Priority {

    LOW(-1),
    NORMAL(0),
    HIGH(1);

    private final int priority;

    Priority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("value", this.priority)
            .build();
    }

}
