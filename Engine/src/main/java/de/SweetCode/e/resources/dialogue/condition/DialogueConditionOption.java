package de.SweetCode.e.resources.dialogue.condition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A dialogue condition option defines what fields
 * the method expects as parameters.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface DialogueConditionOption {

    String fieldName();

}
