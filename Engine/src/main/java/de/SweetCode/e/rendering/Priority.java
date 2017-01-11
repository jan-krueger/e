package de.SweetCode.e.rendering;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

/**
 * <p>
 * An enumeration to pre-define the priorities engine-wide.
 * </p>
 */
public enum Priority {

    /**
     * <p>
     *     Lowest priority. Called after {@link Priority#LOW} and {@link Priority#NORMAL}.
     * </p>
     */
    LOW(-1),

    /**
     * <p>
     *     Normal priority. Called before {@link Priority#LOW}, but after {@link Priority#HIGH}.
     * </p>
     */
    NORMAL(0),

    /**
     * <p>
     *     Highest priority. Called before {@link Priority#LOW} and {@link Priority#NORMAL}.
     * </p>
     */
    HIGH(1);

    private final int priority;

    Priority(int priority) {
        this.priority = priority;
    }

    /**
     * <p>
     *    Returns the numeric value of the priority. This makes it easier to compare them.
     * </p>
     *
     * @return The numeric value.
     */
    public int getPriority() {
        return this.priority;
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("name", this.name())
                .append("value", this.priority)
            .build();
    }

}
