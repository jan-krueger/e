package de.SweetCode.e.input.combinations;

import de.SweetCode.e.event.Event;

public class InputCombinationEvent extends Event {

    private InputCombination inputCombination;

    public InputCombinationEvent(InputCombination inputCombination) {
        this.inputCombination = inputCombination;
    }

    public InputCombination getInputCombination() {
        return this.inputCombination;
    }

}
