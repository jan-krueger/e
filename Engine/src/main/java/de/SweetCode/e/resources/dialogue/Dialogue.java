package de.SweetCode.e.resources.dialogue;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

/**
 * <p>
 * Represents a dialogue by providing access to the start node.
 * </p>
 *
 * @param <T> The type of the {@link DialogueNode} identifier.
 */
public class Dialogue<T> {

    private DialogueNode<T> startNode;

    /**
     * <p>
     *    Creates a new Dialogue.
     * </p>
     *
     * @param startNode The start node of the dialogue.
     */
    public Dialogue(DialogueNode<T> startNode) {
        this.startNode = startNode;
    }

    /**
     * <p>
     *    Gives the {@link DialogueNode} of the dialogue.
     * </p>
     *
     * @return Returns the start node.
     */
    public DialogueNode<T> getStartNode() {
        return this.startNode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("startNode", this.startNode)
            .build();
    }

}
