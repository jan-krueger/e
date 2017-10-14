package de.SweetCode.e;

import de.SweetCode.e.input.InputEntry;

import java.awt.event.KeyEvent;

public class EDebug implements GameComponent {

    public EDebug() {}

    @Override
    public void update(InputEntry inputEntry, long delta) {

        inputEntry.getKeyEntries().forEach(e -> {
            if(e.getKeyCode() == KeyEvent.VK_F12) {
                E.getE().getScreen().setDisplayDebuggingInformation(
                        !E.getE().getScreen().isDisplayDebuggingInformation()
                );
            }
        });

    }

    @Override
    public boolean isActive() {
        return E.getE().getSettings().isDebugging();
    }

}
