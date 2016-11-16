package de.SweetCode.e.resources.dialogue.condition;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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
     * Checks if the condition is fulfilled.
     * @param fields A map with all fields and their current values to pass them to condition method.
     * @return
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

}
