package dungeonmania.entities.buildables;


import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;

public class Shield extends Buildable {
    private int durability;
    private double defence;

    public Shield(int durability, double defence) {
        super(null);
        this.durability = durability;
        this.defence = defence;
    }

    @Override
    public BattleStatistics applyBuff(Game game, BattleStatistics origin) {
        durability--;
        if (durability <= 0) {
            game.playerRemoveItem(this);
        }
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            0,
            defence,
            1,
            1));
    }

    public static boolean isBuildable(Inventory inventory, Game game) {
        int wood = inventory.count(Wood.class);
        int treasure = inventory.count(Treasure.class);
        int keys = inventory.count(Key.class);
        int sunStone = inventory.count(SunStone.class);
        return wood >= 2 && (treasure >= 1 || keys >= 1 || sunStone >= 1);
    }

    public static void build(Inventory inventory) {
        List<Wood> wood = inventory.getEntities(Wood.class);
        List<Treasure> treasure = inventory.getEntities(Treasure.class);
        List<Key> keys = inventory.getEntities(Key.class);
        List<SunStone> sunStone = inventory.getEntities(SunStone.class);
        inventory.remove(wood.get(0));
        inventory.remove(wood.get(1));
        if (sunStone.size() >= 1) {
            return;
        } else if (treasure.size() >= 1) {
            inventory.remove(treasure.get(0));
        } else {
            inventory.remove(keys.get(0));
        }
    }
}
