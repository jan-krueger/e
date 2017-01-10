package de.SweetCode.e.resources.dialogue.condition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *    Can be attached to a method which then defines a validation rule and gets called to validate it.
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DialogueCondition {

    /**
     * <p>
     *    Gives a unique identifier/name of the validation rule which can be used in the {@link de.SweetCode.e.resources.dialogue.DialogueParser}
     *    to identify conditions in the provided parsing value.
     * </p>
     *
     * @return Returns the identifier.
     */
    String id();

}
