package mcheroes.dropper;

import mcheroes.core.CoreProvider;
import mcheroes.core.minigames.actions.RegisterMinigameAction;
import org.bukkit.plugin.java.JavaPlugin;

public class DropperPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        CoreProvider.get().getActionManager().run(new RegisterMinigameAction(new DropperMinigame()));
    }
}
