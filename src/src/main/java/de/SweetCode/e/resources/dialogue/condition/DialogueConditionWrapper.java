package de.SweetCode.e.resources.dialogue.condition;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class DialogueConditionWrapper {

    private final DialogueConditions instance;

    private final String id;
    private final Method method;

    private final List<String> fieldNames;

    public DialogueConditionWrapper(DialogueConditions instance, String id, Method method, Class<?>[] parameters, List<String> fields) {
        this.instance = instance;

        this.id = id;
        this.method = method;

        this.fieldNames = fields;
    }

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
