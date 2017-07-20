package de.SweetCode.e.resources.dialogue.condition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *    Can be attached to method parameters which have the {@link DialogueCondition} annotation. It gives the engine behind
 *    the scene a clue which data it should provide to your validation method. You can than provide the fieldName and it will
 *    automatically provide the values if called:
 * </p>
 * <ul>
 *     <li><i>from</i> is a {@link de.SweetCode.e.resources.dialogue.DialogueNode} and represents the instance which calls
 *     the validation rule</li>
 *     <li><i>to</i> is a {@link de.SweetCode.e.resources.dialogue.DialogueNode} and represents the instance at which the
 *     pointer is pointing at.</li>
 *     <li><i>option</i> is a {@link de.SweetCode.e.resources.dialogue.DialogueOptionPointer} representing the option of
 *     which this condition is part of.</li>
 * </ul>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface DialogueConditionOption {

    String fieldName();

}
