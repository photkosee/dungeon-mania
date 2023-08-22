package dungeonmania.goals;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Exit;
import dungeonmania.util.Position;

public class ExitGoal implements Goal {

    @Override
    public boolean achieved(Game game) {
        if (game.getEntitiesCount(Exit.class) == 0) return false;
        List<Exit> es = game.getEntities(Exit.class);
        Position pos = game.getPlayerPosition();
        return es
            .stream()
            .map(Entity::getPosition)
            .anyMatch(pos::equals);
    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game)) return "";
        return ":exit";
    }
}
