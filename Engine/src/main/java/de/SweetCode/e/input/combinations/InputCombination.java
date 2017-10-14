package de.SweetCode.e.input.combinations;

import de.SweetCode.e.GameComponent;
import de.SweetCode.e.Settings;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.input.entries.KeyEntry;

public class InputCombination implements GameComponent {

    //---
    private final String name;
    private final int timeLimit;
    private final int[] states;

    //---
    private int currentState = 0;
    private int timeLeft = 0;

    /**
     * <p>
     *    Creates a new instance of an InputCombination or better known as "shortcuts". You cam specify a certain set
     *    of keys, and we will check if we hit them or not.
     * </p>
     *
     * @param name The name of the combination.
     * @param timeLimit The time limit in which the combination has to appear. The unit is in defined by {@link Settings#getDeltaUnit()}
     * @param events
     */
    public InputCombination(String name, int timeLimit, int... events) {
        this.name = name;
        this.states = events;
        this.timeLimit = timeLimit;
        this.timeLeft = timeLimit;
    }

    @Override
    public void update(InputEntry inputEntry, long delta) {
        if(this.currentState > 0) {
            this.timeLeft -= delta;

            if(this.timeLeft <= 0) {
                this.reset();
            }
        }

        for(KeyEntry e : inputEntry.getKeyEntries()) {

            if(e.getKeyCode() == this.states[this.currentState]) {
                this.currentState++;

                if(this.currentState == this.states.length) {
                    System.out.println("Combination done");
                }
            } else {
                this.reset();
            }

        }
    }

    private void reset() {
        this.currentState = 0;
        this.timeLeft = this.timeLimit;
    }


    @Override
    public boolean isActive() {
        return true;
    }


}
