package de.SweetCode.e.resources.dialogue;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

public class Dialogue<T> {

    private DialogueNode<T> startNode;

    public Dialogue(DialogueNode<T> startNode) {
        this.startNode = startNode;
    }

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
