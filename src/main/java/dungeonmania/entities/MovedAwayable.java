package dungeonmania.entities;

import dungeonmania.map.GameMap;

public interface MovedAwayable {

    public abstract void onMovedAway(GameMap map, Entity entity);
}
