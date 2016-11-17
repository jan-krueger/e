package de.SweetCode.e.resources.dialogue;

import com.sun.istack.internal.Nullable;
import de.SweetCode.e.resources.dialogue.condition.DialogueConditions;

/**
 * The dialogue parser.
 *
 * @param <P> The type of the given input.
 * @param <N> The type of a node that will be used to validate it.
 * @param <I> The type of the identifier used to identify the first node.
 */
public interface DialogueParser<P, N, I> {

    /**
     * Validates a node.
     *
     * @param node
     * @return
     */
    boolean isValidNode(N node);

    /**
     * Is parsing the input.
     * @param input
     * @return
     */
    Dialogue parse(I startIdentifier, P input, @Nullable DialogueConditions conditions);

    /**
     * Is parsing the input.
     * @param input
     * @return
     */
    default Dialogue parse(I startIdentifier, P input)  {
        return this.parse(startIdentifier, input, null);
    }

}
