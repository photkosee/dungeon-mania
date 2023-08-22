package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MidnightArmourTest {

    @Test
    @DisplayName("Test having zombie while having all materials")
    public void pickUpSunStone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightCannot", "c_SunStoneTest_pickUpSunStone");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        assertThrows(InvalidActionException.class, () ->
            dmc.build("midnight_armour")
        );
    }

    @Test
    @DisplayName("Test building and fighting")
    public void buildBow() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightFight", "c_midnightFight");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        // mercenary has 10/10, player has 5/5
        // player would be dead if the armour isn't providing atk&def (+10/10)

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        assertEquals(0, TestUtils.getEntities(res, "mercenary").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
    }
}
