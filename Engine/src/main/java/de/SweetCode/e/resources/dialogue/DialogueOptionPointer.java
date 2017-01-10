package de.SweetCode.e.resources.dialogue;

import de.SweetCode.e.resources.dialogue.condition.DialogueConditionWrapper;
import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DialogueOptionPointer is a wrapper class to store the option pointer information.
 *
 * @param <T> The type of the pointer.
 */
public class DialogueOptionPointer<T> {

    private T pointer;

    private String optionText;

    private List<DialogueConditionWrapper> dialogueConditionWrappers;

    /**
     * @param optionText The option text.
     * @param pointer The pointer the option is pointing at.
     * @param dialogueConditionWrappers The list of all wrapped conditions.
     */
    public DialogueOptionPointer(String optionText, T pointer, List<DialogueConditionWrapper> dialogueConditionWrappers) {
        this.optionText = optionText;
        this.pointer = pointer;
        this.dialogueConditionWrappers = dialogueConditionWrappers;
    }

    /**
     * @return Gives the object the pointer is pointing at.
     */
    public T getPointer() {
        return this.pointer;
    }

    /**
     * @return Gives the option text.
     */
    public String getOptionText() {
        return this.optionText;
    }

    /**
     * @return Gives all related dialogue conditions.
     */
    public List<DialogueConditionWrapper> getDialogueConditionWrappers() {
        return this.dialogueConditionWrappers;
    }

    /**
     * Checks all connected dialogue conditions.
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
