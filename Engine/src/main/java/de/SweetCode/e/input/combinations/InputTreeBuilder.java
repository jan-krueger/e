package de.SweetCode.e.input.combinations;

import de.SweetCode.e.utils.Assert;

public class InputTreeBuilder {

    private final InputNode root;
    private InputNode current;


    public InputTreeBuilder(int rootKeyCode) {
        this.root = new InputNode(rootKeyCode);
        this.current = this.root;
    }

    public InputTreeBuilder child(int keyCode) {

        InputNode tmp = new InputNode(keyCode);
        this.current.addChild(tmp);
        this.current = tmp;

        return this;
    }

    public InputTreeBuilder end() {

        Assert.assertNotNull("You cannot go further back. You have reached the root of the tree.");
        this.current = this.current.getParent();

        return this;
    }

    public InputCombination build() {
        //@TODO replace placeholder values
        return new InputCombination("Test Name", this.root, 2000);
    }

    public static InputTreeBuilder create(int rootKeyCode) {
        return new InputTreeBuilder(rootKeyCode);
    }

}
