import de.SweetCode.e.resources.dialogue.DialogueNode;
import de.SweetCode.e.resources.dialogue.condition.DialogueCondition;
import de.SweetCode.e.resources.dialogue.condition.DialogueConditionOption;
import de.SweetCode.e.resources.dialogue.condition.DialogueConditions;

public class TestConditions implements DialogueConditions {

    @DialogueCondition(id = "isItACoolName")
    public boolean isItACoolName(@DialogueConditionOption(fieldName = "from") DialogueNode<String> fromNode) {

        if(fromNode.getIdentifier().equals("Start")) {
            return Math.random() >= 0.5;
        }

        return Math.random() >= 0.8;
    }

    @DialogueCondition(id = "random")
    public boolean random(
        @DialogueConditionOption(fieldName = "from") DialogueNode<String> fromNode
    ) {
        // only 10% true
        System.out.println("rad");
        return Math.random() <= 0.1F;
    }


}
