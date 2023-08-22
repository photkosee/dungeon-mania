package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogicalTest {

    @Test
    @DisplayName("or light bulb")
    public void orLight() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logical", "c_bombTest_pickUp");
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.RIGHT);

        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @DisplayName("and light bulb")
    public void andLight() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_andLight", "c_bombTest_pickUp");
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @DisplayName("xor light bulb")
    public void xorLight() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_xorLight", "c_bombTest_pickUp");
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.RIGHT);

        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @DisplayName("co and light bulb")
    public void coAndLight() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_coAndLight", "c_bombTest_pickUp");
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.RIGHT);

        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @DisplayName("co and light bulb fail")
    public void coAndLightFail() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_coAndLightFail", "c_bombTest_pickUp");
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @DisplayName("or door can enter")
    public void orDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_orDoor", "c_bombTest_pickUp");
        assertEquals(new Position(0, 3), getPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 3), getPos(res));
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 4), getPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 4), getPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 4), getPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 4), getPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 4), getPos(res));
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(5, 3), getPos(res));
    }

    @Test
    @DisplayName("or door cant enter")
    public void orDoorCannot() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_orDoor", "c_bombTest_pickUp");
        assertEquals(new Position(0, 3), getPos(res));
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(0, 4), getPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 4), getPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 4), getPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 4), getPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 4), getPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 4), getPos(res));
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(5, 4), getPos(res));
    }

    private Position getPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }
}
