package de.SweetCode.e.input;

import de.SweetCode.e.E;
import de.SweetCode.e.utils.ToStringBuilder;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.concurrent.LinkedTransferQueue;
import java.util.stream.Stream;

public class Input extends KeyAdapter {

    private final Queue<KeyEntry> keyQueue = new LinkedTransferQueue<>();
    private final Queue<MouseEntry> mouseQueue = new LinkedTransferQueue<>();

    public Input() {
        this.register();
    }

    /**
     * Adds the listener.
     */
    private void register() {

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {

            if(e.getID() == KeyEvent.KEY_PRESSED) {
                this.keyQueue.add(
                    KeyEntry.Builder.create()
                            .keyCode(e.getKeyCode())
                            .extendedKeyCode(e.getExtendedKeyCode())
                            .character(e.getKeyChar())
                            .keyLocation(e.getKeyLocation())
                            .isActionKey(e.isActionKey())
                            .isAltDown(e.isAltDown())
                            .isAltGraphDown(e.isAltGraphDown())
                            .isMetaDown(e.isMetaDown())
                            .isShiftDown(e.isShiftDown())
                    .build()
                );
            }

            return false;

        });

        E.getE().getScreen().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Input.this.mouseQueue.add(
                        MouseEntry.Builder.create()
                                .button(e.getButton())
                                .clickCount(e.getClickCount())
                                .isPopupTrigger(e.isPopupTrigger())
                                .locationOnScreen(e.getLocationOnScreen())
                                .point(e.getPoint())
                                .isAltDown(e.isAltDown())
                                .isAltGraphDown(e.isAltGraphDown())
                                .isControlDown(e.isControlDown())
                                .isMetaDown(e.isMetaDown())
                                .isShiftDown(e.isShiftDown())
                        .build()
                );

            }
        });

    }

    /**
     * Returns all pressed keys since the last method call in the wrong order.
     *
     * The first element in the stream is the oldest key.
     * @return
     */
    public Stream<KeyEntry> getKeyboardEntries() {
        Stream<KeyEntry> stream = new LinkedList<>(this.keyQueue).stream();
        this.keyQueue.clear();
        return stream;
    }

    /**
     * Returns all pressed mouse buttons since the last method call in the wrong order.
     *
     * The first element in the stream is the oldest mouse button.
     * @return
     */
    public Stream<MouseEntry> getMouseEntries() {
        Stream<MouseEntry> stream = new LinkedList<>(this.mouseQueue).stream();
        this.mouseQueue.clear();
        return stream;
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("keyQueue", this.keyQueue)
                .append("mouseQueue", this.mouseQueue)
                .build();
    }

}
