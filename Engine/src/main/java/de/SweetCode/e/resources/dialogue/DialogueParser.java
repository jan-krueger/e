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
     * @param node The node to check.
     * @return true, if it is a valid node, otherwise false.
     */
    boolean isValidNode(N node);

    /**
     * Is parsing the input.
     *
     * @param startIdentifier The identifier of the start node.
     * @param input The input to parse.
     * @param conditions A {@link DialogueConditions} reference with all conditions that can be used in the given dialogue.
     * @return Always a new {@link Dialogue} if it was successful, otherwise the root of the dialogue is null.
     */
    Dialogue parse(I startIdentifier, P input, @Nullable DialogueConditions conditions);

}
