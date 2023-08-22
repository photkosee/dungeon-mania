package dungeonmania.entities;

import dungeonmania.map.GameMap;

public interface Overlapable {

    public abstract void onOverlap(GameMap map, Entity entity);
}
