package de.SweetCode.e.resources.dialogue.condition;

import de.SweetCode.e.E;
import de.SweetCode.e.utils.log.LogEntry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * A class to wrap all dialogue conditions.
 */
public class DialogueConditionWrapper {

    private final DialogueConditions instance;

    private final String id;
    private final Method method;

    private final List<String> fieldNames;

    /**
     * @param instance The instance of the dialogue conditions.
     * @param id The unique id of the condition.
     * @param method The method of the condition to invoke.
     * @param fields The fields the method expects.
     */
    public DialogueConditionWrapper(DialogueConditions instance, String id, Method method, List<String> fields) {
        this.instance = instance;

        this.id = id;
        this.method = method;

        this.fieldNames = fields;
    }

    /**
     * Gives the identifier of the condition.
     * @return The unique id of the condition.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Checks if the condition is fulfilled.
     * @param fields A map with all fields and their current values to pass them to condition method.
     * @return Returns true of all conditions are fulfilled, otherwise false.
     */
    public boolean isFulfilled(Map<String, Object> fields) {

        // build arguments
        Object[] arguments = new Object[this.fieldNames.size()];
        for(int i = 0; i < this.fieldNames.size(); i++) {
            arguments[i] = fields.get(this.fieldNames.get(i));
        }

        boolean result = false;

        try {
            result = (boolean) this.method.invoke(this.instance, arguments);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * Parsing all conditions from the instance.
     * @param dialogueConditions A wrapper class containing all DialogueConditions that can be used in the context.
     * @return Returns a map with the parsed conditions. The key is the ID of the {@link DialogueCondition} with an
     *          leading at (@) symbol. The value is a wrapper and helper object of the condition method.
     */
    public static Map<String, DialogueConditionWrapper> getConditionWrappers(DialogueConditions dialogueConditions) {

        Map<String, DialogueConditionWrapper> conditionWrapper = new HashMap<>();

        if(dialogueConditions == null) {
            return conditionWrapper;
        }

        Method[] methods = dialogueConditions.getClass().getMethods();
        Arrays.stream(methods)
                .filter(m -> m.isAnnotationPresent(DialogueCondition.class))
                .forEach(m -> {

                    //well... the method is supposed to return a boolean
                    if(!(m.getReturnType().isAssignableFrom(boolean.class))) {
                        System.out.println(m.getReturnType().getName());
                        E.getE().getLog().log(
                                LogEntry.Builder.create()
                                        .message("The DialogueConditions method %s does not return a boolean.", m.getName())
                                        .build()
                        );
                        return;
                    }

                    DialogueCondition idAnnotation = m.getAnnotation(DialogueCondition.class);
                    List<String> fields = new LinkedList<>();

                    for(Annotation[] aArray : m.getParameterAnnotations()) {
                        for(Annotation annotation : aArray) {

                            //is DialogueConditionOption?
                            if(annotation.annotationType().equals(DialogueConditionOption.class)) {
                                fields.add(((DialogueConditionOption) annotation).fieldName());
                            }

                        }
                    }

                    // if the length is not equal, than we didn't get for all parameters a field type
                    if(!(m.getParameterTypes().length == fields.size())) {
                        E.getE().getLog().log(
                                LogEntry.Builder.create()
                                        .message("The DialogueConditions method %s has not enough annotations.", m.getName())
                                        .build()
                        );
                        return;
                    }

                    // Condition name storted with an @ - to make the lookup easier
                    conditionWrapper.put(
                            ("@" + idAnnotation.id()),
                            new DialogueConditionWrapper(dialogueConditions, idAnnotation.id(), m, fields)
                    );

                });

        return conditionWrapper;

    }

}
