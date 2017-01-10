package de.SweetCode.e.resources.dialogue;

import de.SweetCode.e.resources.dialogue.condition.DialogueConditionWrapper;
import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * DialogueOptionPointer represents one option of a {@link DialogueNode}.
 * </p>
 */
public class DialogueOptionPointer {

    private String optionText;

    private DialogueNode pointer;

    private List<DialogueConditionWrapper> dialogueConditionWrappers;

    /**
     * <p>
     *    Creates a new DialogueOptionPointer.
     * </p>
     *
     * @param optionText The option text.
     * @param pointer The pointer the option is pointing at.
     * @param dialogueConditionWrappers The list of all wrapped conditions.
     */
    public DialogueOptionPointer(String optionText, DialogueNode pointer, List<DialogueConditionWrapper> dialogueConditionWrappers) {
        this.optionText = optionText;
        this.pointer = pointer;
        this.dialogueConditionWrappers = dialogueConditionWrappers;
    }

    /**
     * <p>
     *    Gives the object at which the pointer is pointing at.
     * </p>
     *
     * @return Returns the pointer.
     */
    public DialogueNode getPointer() {
        return this.pointer;
    }

    /**
     * <p>
     *    Gives the option text representing the DialogueOptionPointer.
     * </p>
     *
     * @return Gives the option text.
     */
    public String getOptionText() {
        return this.optionText;
    }

    /**
     * <p>
     *    Returns a list of {@link DialogueConditionWrapper DialogueConditionWrappers} which represent all conditions which
     *    have to be fulfilled, if the option should be available.
     * </p>
     *
     * @return Gives all related dialogue conditions.
     */
    public List<DialogueConditionWrapper> getDialogueConditionWrappers() {
        return this.dialogueConditionWrappers;
    }

    /**
     * <p>
     *    Calls all related {@link DialogueConditionWrapper DialogueConditionWrappers} to check if the option should be
     *    visible to the end-user.
     * </p>
     *
     * @param caller The DialogueNode calling this method.
     * @return true, if all conditions are fulfilled, otherwise false.
     */
    public boolean isValid(DialogueNode caller) {
        Map<String, Object> options = new HashMap<>();
        options.put("from", caller);
        options.put("to", this.pointer);
        options.put("option", this);

        return this.dialogueConditionWrappers
                .stream()
                .filter(e -> e.isFulfilled(options))
                .count() == this.dialogueConditionWrappers.size();
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("optionText", this.optionText)
            .build();
    }
}
