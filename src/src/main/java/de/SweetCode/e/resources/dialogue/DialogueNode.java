package de.SweetCode.e.resources.dialogue;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DialogueNode<T> {

    private final T identifier;

    private String dialogueText;
    private List<DialogueOptionPointer<DialogueNode>> optionPointers = new LinkedList<>();

    public DialogueNode(T identifier, String dialogueText, List<DialogueOptionPointer<DialogueNode>> optionPointers) {
        this.identifier = identifier;
        this.dialogueText = dialogueText;
        this.optionPointers = optionPointers;
    }

    /**
     * Gets the unique identifier of this node.
     * @return
     */
    public T getIdentifier() {
        return this.identifier;
    }

    /**
     * Returns all available options.
     *
     * @return
     */
    public List<DialogueOptionPointer<DialogueNode>> getOptionPointers() {
        return this.optionPointers
                .stream()
                .filter(e -> e.isValid(this))
                .collect(Collectors.toList());
    }

    /**
     * Sets the pointers for this node.
     * @param pointers
     */
    public void setPointers(List<DialogueOptionPointer<DialogueNode>> pointers) {
        this.optionPointers = pointers;
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("identifier", this.identifier)
                .append("dialogue", this.dialogueText)
                .append("optionPointers", this.optionPointers)
            .build();
    }

    @Override
    public boolean equals(Object obj) {

        if(obj == null) {
            return false;
        }

        if(!(obj instanceof DialogueNode)) {
            return false;
        }

        DialogueNode node = (DialogueNode) obj;

        return (node == this || node.getIdentifier().equals(this.getIdentifier()));
    }

    /**
     * Creates a dummy dialogue node, only with an ideintifier.
     *
     * @param identifier
     * @param <T>
     * @return
     */
    public static <T> DialogueNode<T> dummy(T identifier) {
        return new DialogueNode<>(identifier, null, null);
    }


}
