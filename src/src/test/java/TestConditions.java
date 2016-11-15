import de.SweetCode.e.resources.dialogue.DialogueNode;
import de.SweetCode.e.resources.dialogue.DialogueOptionPointer;
import de.SweetCode.e.resources.dialogue.condition.DialogueCondition;
import de.SweetCode.e.resources.dialogue.condition.DialogueConditionOption;
import de.SweetCode.e.resources.dialogue.condition.DialogueConditions;

public class TestConditions implements DialogueConditions {

    @DialogueCondition(id = "isItACoolName")
    public boolean isItACoolName(@DialogueConditionOption(fieldName = "from") DialogueNode<String> fromNode, @DialogueConditionOption(fieldName = "option") DialogueOptionPointer<DialogueNode<String>> optionText) {

        // If the option calling this condition belongs to the "Start"-node return true...
        if(fromNode.getIdentifier().equals("Start")) {
            return true;
        }

        return false;
    }

    @DialogueCondition(id = "random")
    public boolean random() {
        // only 10% true
        System.out.println("rad");
        return Math.random() <= 0.1F;
    }


}
