package de.SweetCode.e.input;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.concurrent.LinkedTransferQueue;
import java.util.stream.Stream;

public class Input extends KeyAdapter {

    private Queue<KeyEntry> queue = new LinkedTransferQueue<>();

    public Input() {
        this.register();
    }

    /**
     * Adds the listener.
     */
    private void register() {

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {

            if(e.getID() == KeyEvent.KEY_PRESSED) {
                this.queue.add(
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

    }

    /**
     * Returns all pressed keys since the last method call in the wrong order.
     *
     * The first element in the stream is the oldest key.
     * @return
     */
    public Stream<KeyEntry> getPressedKeys() {
        Stream<KeyEntry> stream = new LinkedList<>(this.queue).stream();
        this.queue.clear();
        return stream;
    }


}
