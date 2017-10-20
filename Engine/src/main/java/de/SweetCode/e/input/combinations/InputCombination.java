package de.SweetCode.e.input.combinations;

import de.SweetCode.e.E;
import de.SweetCode.e.GameComponent;
import de.SweetCode.e.Settings;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.input.entries.KeyEntry;

import java.util.Optional;

public class InputCombination implements GameComponent {

    //---
    private final String name;
    private final int timeLimit;
    private final InputNode root;

    //--- State values; used only internal to determine if the combination got triggered or not.
    private InputNode currentState;
    private int timeLeft = 0;
    private boolean parent = true;

    /**
     * <p>
     *    Creates a new instance of an InputCombination or better known as "shortcuts". You cam specify a certain set
     *    of keys, and we will check if we hit them or not.
     * </p>
     *
     * @param name The name of the combination.
     * @param timeLimit The time limit in which the combination has to appear. The unit is in defined by {@link Settings#getDeltaUnit()}
     */
    public InputCombination(String name, InputNode root, int timeLimit) {
        this.name = name;
        this.timeLimit = timeLimit;
        this.root = root;
        this.currentState = this.root;
        this.timeLeft = timeLimit;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void update(InputEntry inputEntry, long delta) {
        if(!(this.currentState.getParent() == null)) {
            this.timeLeft -= delta;

            if(this.timeLeft <= 0) {
                this.reset();
            }
        }

        for(KeyEntry e : inputEntry.getKeyEntries()) {

            //--- Check current parent
            if(this.parent) {
                if(this.currentState.getKeyCode() == e.getKeyCode()) {
                    this.parent = false;
                } else {
                    this.reset();
                    break;
                }
            }
            //--- Check parent's children
            else {
                Optional<InputNode> node = this.currentState.getChildByKeyCode(e.getKeyCode());
                if (node.isPresent()) {
                    this.currentState = node.get();
                } else {
                    this.reset();
                }
            }
        }

        if(this.currentState.getChildren().isEmpty()) {
            E.getE().getEventHandler().trigger(new InputCombinationEvent(this), false);
            this.reset();
        }
    }

    private void reset() {
        this.parent = true;
        this.currentState = this.root;
        this.timeLeft = this.timeLimit;
    }


    @Override
    public boolean isActive() {
        return true;
    }


}
