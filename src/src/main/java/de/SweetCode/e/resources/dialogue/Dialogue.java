package de.SweetCode.e.resources.dialogue;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

/**
 * Represents one dialogue.
 * @param <T>
 */
public class Dialogue<T> {

    private DialogueNode<T> startNode;

    /**
     * @param startNode The start node of the dialogue.
     */
    public Dialogue(DialogueNode<T> startNode) {
        this.startNode = startNode;
    }

    /**
     * Returns the start node of the dialogue.
     * @return
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
