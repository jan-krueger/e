package de.SweetCode.e.resources.dialogue;

import com.sun.istack.internal.Nullable;
import de.SweetCode.e.resources.dialogue.condition.DialogueConditions;

/**
 * <p>
 *    The dialogue parser interface represents a dialogue parser to make it easier to handle dialogues of various standards.
 * </p>
 *
 * @param <P> The type of the given input.
 * @param <I> The type of the identifier used to identify the first node.
 */
public interface DialogueParser<P, I> {

    /**
     * <p>
     *    This method parses a Dialogue and creates a new {@link Dialogue} by creating all cross-references and representing
     *    all conditions correctly in the structure.
     * </p>
     *
     * @param startIdentifier The identifier of the start node.
     * @param input The input to parse.
     * @param conditions A {@link DialogueConditions} reference with all conditions that can be used in the given dialogue.
     * @return Always a new {@link Dialogue} if it was successful, otherwise the root of the dialogue is null.
     */
    Dialogue parse(I startIdentifier, P input, @Nullable DialogueConditions conditions);

}
