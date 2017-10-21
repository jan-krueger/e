package de.SweetCode.e.input.combinations;

import de.SweetCode.e.utils.Assert;

public class InputTreeBuilder {

    private final String name;

    private final InputNode root = new InputNode(-1);
    private InputNode current = root;

    public InputTreeBuilder(String name) {
        this.name = name;
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
        return new InputCombination(this.name, this.root, 2000);
    }

    public static InputTreeBuilder create(String name) {
        return new InputTreeBuilder(name);
    }

}
