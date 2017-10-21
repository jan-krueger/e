package de.SweetCode.e;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.input.combinations.InputTreeBuilder;
import de.SweetCode.e.input.entries.InputCombinationEntry;

import java.awt.event.KeyEvent;

public class EDebug implements GameComponent {

    EDebug() {
        E.getE().addComponent(
                InputTreeBuilder.create("_e_keys_debug_state").child(KeyEvent.VK_CONTROL).child(KeyEvent.VK_D).build()
        );
    }

    @Override
    public void update(InputEntry inputEntry, long delta) {

        if(inputEntry.has(InputCombinationEntry.class, e -> e.stream().anyMatch(f -> f.getInputCombination().getName().equals("_e_keys_debug_state")))) {
            E.getE().getScreen().setDisplayDebuggingInformation(
                    !E.getE().getScreen().isDisplayDebuggingInformation()
            );
        }

    }

    @Override
    public boolean isActive() {
        return E.getE().getSettings().isDebugging();
    }

}
