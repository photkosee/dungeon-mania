package dungeonmania.entities;

import dungeonmania.map.GameMap;

import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.util.Position;

public class Door extends Entity implements Overlapable {
    private boolean open = false;
    private int number;

    public Door(Position position, int number) {
        super(position.asLayer(Entity.DOOR_LAYER));
        this.number = number;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (open || entity instanceof Spider) {
            return true;
        }
        return (entity instanceof Player
                && (hasKey(((Player) entity).getInventory()) || hasSunStone(((Player) entity).getInventory())));
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;
        Key key = player.getKey(number);
        if (hasSunStone(player.getInventory())) {
            open();
        } else if (hasKey(player.getInventory())) {
            player.remove(key);
            open();
        }
    }

    private boolean hasKey(Inventory inventory) {
        Key key = inventory.getKey(number);
        return (key != null);
    }

    private boolean hasSunStone(Inventory inventory) {
        return inventory.count(SunStone.class) >= 1;
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
    }
}
