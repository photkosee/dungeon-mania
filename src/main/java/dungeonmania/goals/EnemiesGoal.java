package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.enemies.ZombieToastSpawner;

public class EnemiesGoal implements Goal {

    private int destroyGoal;

    public EnemiesGoal(int destroyGoal) {
        super();
        this.destroyGoal = destroyGoal;
    }

    @Override
    public boolean achieved(Game game) {
        return game.getKillCount() >= destroyGoal
                && game.getEntitiesCount(ZombieToastSpawner.class) == 0;
    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game)) return "";
        return ":enemies";
    }
}
