package de.SweetCode.e.input.entries;

import de.SweetCode.e.input.InputType;
import de.SweetCode.e.input.combinations.InputCombination;

public class InputCombinationEntry implements InputType {

    private final InputCombination inputCombination;

    public InputCombinationEntry(InputCombination inputCombination) {
        this.inputCombination = inputCombination;
    }

    public InputCombination getInputCombination() {
        return this.inputCombination;
    }

}
