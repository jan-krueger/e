package de.SweetCode.e.input.combinations;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class InputNode {

    private final int keyCode;
    private final List<InputNode> children = new LinkedList<>();

    private InputNode parent;


    public InputNode(int keyCode) {
        this.keyCode = keyCode;
    }

    public InputNode getParent() {
        return this.parent;
    }

    public Optional<InputNode> getChildByKeyCode(int keyCode) {
        return this.children.stream().filter(e -> e.getKeyCode() == keyCode).findFirst();
    }

    public void addChild(InputNode child) {

        assert !this.children.contains(child);

        this.children.add(child);
        child.setParent(this);
    }

    public void setParent(InputNode parent) {
        this.parent = parent;
    }

    public List<InputNode> getChildren() {
        return this.children;
    }

    public int getKeyCode() {
        return this.keyCode;
    }


}
