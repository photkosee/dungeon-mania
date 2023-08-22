package dungeonmania.goals;

import dungeonmania.Game;

public class TreasureGoal implements Goal {

    private int treasureGoal;

    public TreasureGoal(int treasureGoal) {
        super();
        this.treasureGoal = treasureGoal;
    }

    @Override
    public boolean achieved(Game game) {
        return game.getTreasurePick() >= treasureGoal;
    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game)) return "";
        return ":treasure";
    }
}
