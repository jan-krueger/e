package de.SweetCode.e.resources.dialogue;

import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * Represents one node or one part of the dialogue.
 * </p>
 *
 * @param <T> The type of the identifier.
 */
public class DialogueNode<T> {

    private final T identifier;

    private String dialogueText;
    private List<DialogueOptionPointer> optionPointers = new LinkedList<>();

    /**
     * <p>
     *    Creates a new DialogueNode.
     * </p>
     *
     * @param identifier The identifier of the node.
     * @param dialogueText The dialogue text.
     * @param optionPointers All related options.
     */
    public DialogueNode(T identifier, String dialogueText, List<DialogueOptionPointer> optionPointers) {
        this.identifier = identifier;
        this.dialogueText = dialogueText;
        this.optionPointers = optionPointers;
    }

    /**
     * <p>
     *    Returns the identifier of the node.
     * </p>
     *
     * @return Gives the identifier.
     */
    public T getIdentifier() {
        return this.identifier;
    }

    /**
     * <p>
     *    Gives a list of all available {@link DialogueOptionPointer DialogueOptionPointers}. The availability will be
     *    tested by calling {@link DialogueOptionPointer#isValid(DialogueNode)} and providing as caller {@link DialogueNode caller}
     *    <i>this</i>.
     * </p>
     *
     * @return Gives all available options.
     */
    public List<DialogueOptionPointer> getOptionPointers() {
        return this.optionPointers
                .stream()
                .filter(e -> e.isValid(this))
                .collect(Collectors.toList());
    }

    /**
     * <p>
     *    This sets the {@link DialogueNode#optionPointers} reference of the object to the provided list.
     * </p>
     *
     * @param pointers A list of all {@link DialogueOptionPointer DialogueOptionPointers}.
     */
    public void setPointers(List<DialogueOptionPointer> pointers) {
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
     * <p>
     *    Creates a new dummy node. It only sets the identifier. The dialogue text and the option pointers get set to null.
     * </p>
     *
     * @param identifier The identifier of the dummy.
     * @param <T> The type of the identifier.
     * @return A new {@link DialogueNode} dummy instance.
     */
    public static <T> DialogueNode<T> dummy(T identifier) {
        return new DialogueNode<>(identifier, null, null);
    }


}
