package dungeonmania;

import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

import dungeonmania.battles.BattleFacade;
import dungeonmania.entities.ConductorEntity;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.LogicalEntity;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.ZombieToastSpawner;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goals.Goal;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Game {

    public static final int PLAYER_MOVEMENT = 0;
    public static final int PLAYER_MOVEMENT_CALLBACK = 1;
    public static final int AI_MOVEMENT = 2;
    public static final int AI_MOVEMENT_CALLBACK = 3;

    private String id;
    private String name;
    private Goal goals;
    private GameMap map;
    private Player player;
    private BattleFacade battleFacade;
    private EntityFactory entityFactory;
    private boolean isInTick = false;
    private int tickCount = 0;
    private PriorityQueue<ComparableCallback> sub = new PriorityQueue<>();
    private PriorityQueue<ComparableCallback> addingSub = new PriorityQueue<>();
    private int killCount = 0;

    public Game(String dungeonName) {
        this.name = dungeonName;
        this.map = new GameMap();
        this.battleFacade = new BattleFacade();
    }

    public void init() {
        this.id = UUID.randomUUID().toString();
        map.init();
        this.tickCount = 0;
        register(() -> player.onTick(tickCount), PLAYER_MOVEMENT, "potionQueue");
    }

    public Game tick(Direction movementDirection) {
        registerOnce(
            () -> player.move(this.getMap(), movementDirection), PLAYER_MOVEMENT, "playerMoves");
        tick();
        return this;
    }

    public Game tick(String itemUsedId) throws InvalidActionException {
        Entity item = player.getEntity(itemUsedId);
        if (item == null)
            throw new InvalidActionException(String.format("Item with id %s doesn't exist", itemUsedId));
        if (!(item instanceof Bomb) && !(item instanceof Potion))
            throw new IllegalArgumentException(String.format("%s cannot be used", item.getClass()));

        registerOnce(() -> {
            if (item instanceof Bomb)
                player.use((Bomb) item, map);
            if (item instanceof Potion)
                player.use((Potion) item, tickCount);
        }, PLAYER_MOVEMENT, "playerUsesItem");
        tick();
        return this;
    }

    /**
     * Battle between player and enemy
     * @param player
     * @param enemy
     */
    public void battle(Player player, Enemy enemy) {
        battleFacade.battle(this, player, enemy);
        if (player.getBattleStatisticsHealth() <= 0) {
            map.destroyEntity(player);
        }
        if (enemy.getBattleStatisticsHealth() <= 0) {
            this.killCount += 1;
            map.destroyEntity(enemy);
        }
    }

    public Game build(String buildable) throws InvalidActionException {
        List<String> buildables = player.getBuildables(this);
        if (!buildables.contains(buildable)) {
            throw new InvalidActionException(String.format("%s cannot be built", buildable));
        }
        registerOnce(() -> player.build(this, buildable, entityFactory), PLAYER_MOVEMENT, "playerBuildsItem");
        tick();
        return this;
    }

    public Game interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        Entity e = map.getEntity(entityId);
        if (e == null || !(e instanceof Interactable))
            throw new IllegalArgumentException("Entity cannot be interacted");
        if (!((Interactable) e).isInteractable(player)) {
            throw new InvalidActionException("Entity cannot be interacted");
        }
        registerOnce(
            () -> ((Interactable) e).interact(player, this), PLAYER_MOVEMENT, "playerBuildsItem");
        tick();
        return this;
    }

    public <T extends Entity> long countEntities(Class<T> type) {
        return map.countEntities(type);
    }

    public void register(Runnable r, int priority, String id) {
        if (isInTick)
            addingSub.add(new ComparableCallback(r, priority, id));
        else
            sub.add(new ComparableCallback(r, priority, id));
    }

    public void registerOnce(Runnable r, int priority, String id) {
        if (isInTick)
            addingSub.add(new ComparableCallback(r, priority, id, true));
        else
            sub.add(new ComparableCallback(r, priority, id, true));
    }

    public void unsubscribe(String id) {
        for (ComparableCallback c : sub) {
            if (id.equals(c.getId())) {
                c.invalidate();
            }
        }
        for (ComparableCallback c : addingSub) {
            if (id.equals(c.getId())) {
                c.invalidate();
            }
        }
    }

    public int tick() {
        isInTick = true;
        sub.forEach(s -> s.run());
        isInTick = false;
        sub.addAll(addingSub);
        addingSub = new PriorityQueue<>();
        sub.removeIf(s -> !s.isValid());
        tickCount++;
        initRegisterLogical();
        initUpdateLogical();
        // update the weapons/potions duration
        return tickCount;
    }

    private void initRegisterLogical() {
        List<LogicalEntity> logical = getEntities(LogicalEntity.class);
        for (LogicalEntity e : logical) {
            e.activate(getMap());
        }
    }

    private void initUpdateLogical() {
        List<ConductorEntity> conductors = getEntities(ConductorEntity.class);
        for (ConductorEntity e : conductors) {
            e.updateActivated();
        }
    }

    public int getTick() {
        return this.tickCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Goal getGoals() {
        return goals;
    }

    public void setGoals(Goal goals) {
        this.goals = goals;
    }

    public GameMap getMap() {
        return map;
    }

    public boolean getIsInTick() {
        return isInTick;
    }

    public List<Entity> getEntities(Position position) {
        return map.getEntities(position);
    }

    public void moveTo(Entity entity, Position position) {
        map.moveTo(entity, position);
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public <T extends Entity> List<T> getEntities(Class<T> type) {
        return map.getEntities(type);
    }

    public <T extends Entity> int getEntitiesCount(Class<T> type) {
        return getEntities(type).size();
    }

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }

    public void spawnZombie(ZombieToastSpawner zombieToastSpawner) {
        entityFactory.spawnZombie(this, zombieToastSpawner);
    }

    public void spawnSpider() {
        entityFactory.spawnSpider(this);
    }

    public void setEntityFactory(EntityFactory factory) {
        entityFactory = factory;
    }

    public Player getPlayer() {
        return player;
    }

    public Position getPlayerPosition() {
        return player.getPosition();
    }

    public int getPlayerPositionX() {
        return player.getPositionX();
    }

    public int getPlayerPositionY() {
        return player.getPositionY();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Position getPlayerPreviousDistinctPosition() {
        return player.getPreviousDistinctPosition();
    }

    public boolean isPlayerCardinallyAdjacent(Position pos) {
        return player.isPlayerCardinallyAdjacent(pos);
    }

    public void playerRemoveItem(InventoryItem item) {
        player.remove(item);
    }

    public Inventory getInventory() {
        return player.getInventory();
    }

    public List<String> getBuildables() {
        return player.getBuildables(this);
    }

    public BattleFacade getBattleFacade() {
        return battleFacade;
    }

    public void setBattleFacade(BattleFacade battleFacade) {
        this.battleFacade = battleFacade;
    }

    public int getTreasurePick() {
        return player.getTreasurePick();
    }

    public int getKillCount() {
        return killCount;
    }

    public Position dijkstraPathFind(Position src, Position dest, Entity entity) {
        return map.dijkstraPathFind(src, dest, entity);
    }

    public void destroyEntity(Entity entity) {
        map.destroyEntity(entity);
    }
}
