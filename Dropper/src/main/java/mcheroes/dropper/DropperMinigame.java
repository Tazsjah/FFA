package mcheroes.dropper;

import mcheroes.core.api.minigame.Minigame;
import mcheroes.dropper.minigame.GameLevel;

import java.util.*;

public class DropperMinigame implements Minigame {
    private final List<GameLevel> levels = new ArrayList<>();
    private final Map<UUID, GameLevel> playerLevels = new HashMap<>();

    private boolean started = false;

    @Override
    public String getId() {
        return "dropper";
    }

    @Override
    public void start() {
        started = true;
        playerLevels.clear();
    }

    @Override
    public void stop() {
        started = false;
    }

    @Override
    public boolean hasStarted() {
        return started;
    }

    @Override
    public boolean canStart() {
        return !levels.isEmpty();
    }
}
