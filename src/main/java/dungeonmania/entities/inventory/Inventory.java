package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.MidnightArmour;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.buildables.Shield;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Sword;

public class Inventory {

    private List<InventoryItem> items = new ArrayList<>();

    public boolean add(InventoryItem item) {
        items.add(item);
        return true;
    }

    public void remove(InventoryItem item) {
        items.remove(item);
    }

    public List<String> getBuildables(Game game) {
        List<String> result = new ArrayList<>();
        if (Bow.isBuildable(this, game)) {
            result.add("bow");
        }
        if (Shield.isBuildable(this, game)) {
            result.add("shield");
        }
        if (MidnightArmour.isBuildable(this, game)) {
            result.add("midnight_armour");
        }
        if (Sceptre.isBuildable(this, game)) {
            result.add("sceptre");
        }
        return result;
    }

    public InventoryItem checkBuildCriteria(Game game, String name, EntityFactory factory) {
        List<String> buildableList = getBuildables(game);
        if (!buildableList.contains(name)) return null;
        switch (name) {
        case "bow":
            Bow.build(this);
            return factory.buildBow();
        case "shield":
            Shield.build(this);
            return factory.buildShield();
        case "midnight_armour":
            MidnightArmour.build(this);
            return factory.buildMidnightArmour();
        case "sceptre":
            Sceptre.build(this);
            return factory.buildSceptre();
        default:
            return null;
        }
    }

    public <T extends InventoryItem> T getFirst(Class<T> itemType) {
        for (InventoryItem item : items)
            if (itemType.isInstance(item)) return itemType.cast(item);
        return null;
    }

    public <T extends InventoryItem> int count(Class<T> itemType) {
        int count = 0;
        for (InventoryItem item : items)
            if (itemType.isInstance(item)) count++;
        return count;
    }

    public Entity getEntity(String itemUsedId) {
        for (InventoryItem item : items)
            if (((Entity) item).getId().equals(itemUsedId)) return (Entity) item;
        return null;
    }

    public List<Entity> getEntities() {
        return items.stream().map(Entity.class::cast).collect(Collectors.toList());
    }

    public <T> List<T> getEntities(Class<T> clz) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }

    public boolean hasWeapon() {
        return getFirst(Sword.class) != null || getFirst(Bow.class) != null;
    }

    public BattleItem getWeapon() {
        BattleItem weapon = getFirst(Sword.class);
        if (weapon == null)
            return getFirst(Bow.class);
        return weapon;
    }

    public void useWeapon(Game game) {
        getWeapon().applyBuff(game, new BattleStatistics(
            0,
            0,
            0,
            0,
            0));
    }

    public Key getKey(int number) {
        List<Key> keys = getEntities(Key.class);
        return keys.stream().filter(e -> e.getNumber() == number).findAny().orElse(null);
    }

    public int getSceptreDuration() {
        Sceptre sceptre = getFirst(Sceptre.class);
        if (sceptre == null) return 0;
        return sceptre.getDuration();
    }
}
