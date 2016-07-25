package org.chernatkin.game.puzzle;

import org.chernatkin.game.puzzle.character.GamePerson;
import org.chernatkin.game.puzzle.engine.GameEngine;
import org.chernatkin.game.puzzle.engine.GameOverException;
import org.chernatkin.game.puzzle.map.CurrentMapVew;
import org.chernatkin.game.puzzle.map.Direction;
import org.chernatkin.game.puzzle.map.Game2DMap;
import org.chernatkin.game.puzzle.map.Point2D;
import org.chernatkin.game.puzzle.serialization.GameLoader;
import org.chernatkin.game.puzzle.serialization.GameState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MovingTest extends AbstractGameTest {

    private static final String SAVED_GAME = "bunker_simple";
    
    private static final String MAP_NAME = "bunker";
    
    private static final GamePerson player1 = new GamePerson("player1", null, 100, 0, false);
    private static final Point2D player1Point = new Point2D(null, 3, 0);
    
    private static final GamePerson bot1 = new GamePerson("bot1", null, 100, 0, true);
    private static final Point2D bot1Point = new Point2D(null, 4, 10);
    
    private static final GamePerson bot2 = new GamePerson("bot2", null, 100, 0, true);
    private static final Point2D bot2Point = new Point2D(null, 3, 20);
    
    private GamePerson human;
    
    private GameEngine engine;
    
    @Before
    public void initGame(){
        
        GameLoader loader = GameLoader.loadGameData();
        Assert.assertTrue(loader.getSavedGamesNames().contains(SAVED_GAME));
        
        GameState state = loader.loadSavedGameState(SAVED_GAME);
        Assert.assertEquals(3, state.getPersons().size());
        
        Assert.assertEquals(player1Point, state.getPersons().get(player1));
        Assert.assertEquals(bot1Point, state.getPersons().get(bot1));
        Assert.assertEquals(bot2Point, state.getPersons().get(bot2));
        
        Assert.assertEquals(MAP_NAME, state.getMapName());
        
        human = state.getHuman();
        Assert.assertEquals(player1, human);
        
        Game2DMap map = loader.loadMap(state.getMapName());
        
        engine = new GameEngine(map, state.getPersons());
    }
    
    @Test
    public void movingTest() throws GameOverException{
        
        CurrentMapVew view = engine.getCurrentView(human);
        Assert.assertEquals(2, view.getVisiblePersons().size());
        Assert.assertEquals(human, view.getCurrentPerson());
        Assert.assertEquals(player1Point, view.getCurrentPersonPoint());
        
        move(Direction.SOUTH, player1Point.getX(), player1Point.getY());
        move(Direction.WEST, player1Point.getX(), player1Point.getY());
        
        move(Direction.NORTH, player1Point.getX(), player1Point.getY() + 1);
        move(Direction.EAST, player1Point.getX() + 1, player1Point.getY() + 1);
    }
    
    protected CurrentMapVew move(Direction direction, int expectedX, int expectedY) throws GameOverException{
        CurrentMapVew view = engine.move(human, direction);
        Assert.assertEquals(human, view.getCurrentPerson());
        Assert.assertEquals(new Point2D(null, expectedX, expectedY), view.getCurrentPersonPoint());
        
        return view;
    }
    
}
