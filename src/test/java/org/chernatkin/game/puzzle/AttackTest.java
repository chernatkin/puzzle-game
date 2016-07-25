package org.chernatkin.game.puzzle;

import org.chernatkin.game.puzzle.character.GamePerson;
import org.chernatkin.game.puzzle.engine.GameEngine;
import org.chernatkin.game.puzzle.engine.GameOverException;
import org.chernatkin.game.puzzle.map.CurrentMapVew;
import org.chernatkin.game.puzzle.map.Direction;
import org.chernatkin.game.puzzle.map.Point2D;
import org.chernatkin.game.puzzle.serialization.GameLoader;
import org.chernatkin.game.puzzle.serialization.GameState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttackTest extends AbstractGameTest {

    private static final String SAVED_GAME = "bunker_simple";
    
    private static final GamePerson player1 = new GamePerson("player1", null, 100, 0, false);
    private static final Point2D player1Point = new Point2D(null, 3, 0);
    
    private static final GamePerson bot1 = new GamePerson("bot1", null, 100, 0, true);
    private static final Point2D bot1Point = new Point2D(null, 4, 10);
    
    private static final GamePerson bot2 = new GamePerson("bot2", null, 100, 0, true);
    private static final Point2D bot2Point = new Point2D(null, 3, 20);
    
    private GamePerson human;
    
    private GamePerson enemy1;
    
    private GamePerson enemy2;
    
    private GameEngine engine;
    
    @Before
    public void initGame(){
        
        GameLoader loader = GameLoader.loadGameData();
        GameState state = loader.loadSavedGameState(SAVED_GAME);
        
        human = state.getHuman();
        enemy1 = state.getBots().stream().filter(b -> b.getName().equals(bot1.getName())).findFirst().get();
        enemy2 = state.getBots().stream().filter(b -> b.getName().equals(bot2.getName())).findFirst().get();
        
        engine = new GameEngine(loader.loadMap(state.getMapName()), state.getPersons());
    }
    
    @Test
    public void attackTest() throws GameOverException{
        
        CurrentMapVew view = engine.getCurrentView(human);
        moveTo(view.getCurrentPersonPoint(), bot1Point.getX(), bot1Point.getY() - 3);
        
        view = engine.applyWeapon(human, Direction.NORTH, human.getCharacter().getWeapon());
        Assert.assertEquals(100, enemy1.getHealth());
        Assert.assertEquals(0, human.getExperence());
        
        moveTo(view.getCurrentPersonPoint(), bot1Point.getX(), bot1Point.getY() - 2);
        
        //first bot killed
        view = attack(Direction.NORTH, enemy1, 5);
        Assert.assertEquals(1, view.getVisiblePersons().size());
        
        
        view = moveTo(view.getCurrentPersonPoint(), bot2Point.getX(), bot2Point.getY() - 1);
        Assert.assertEquals(2, view.getVisiblePersons().size());
        
        try{
            //second bot killed
            view = attack(Direction.NORTH, enemy2, 5);
            Assert.fail();
        } catch(GameOverException goe){
        }
        
        Assert.assertEquals(200, human.getExperence());
        Assert.assertEquals(100, human.getHealth());
    }
    
    protected CurrentMapVew attack(Direction direction, GamePerson attacked, int number) throws GameOverException{
        CurrentMapVew view = null;
        final int strength = human.getCharacter().getWeapon().getStrength();
        int health = attacked.getHealth();
        int experience = human.getExperence();
        
        for(int i = 0; i < number; i++){
            view = engine.applyWeapon(human, direction, human.getCharacter().getWeapon());
            
            int newHealth = Math.max(0, health - strength);
            experience += health - newHealth;
            health = newHealth;
            
            Assert.assertEquals(health, attacked.getHealth());
            Assert.assertEquals(experience, human.getExperence());
        }
        
        return view;
    }
    
    protected CurrentMapVew moveTo(Point2D current, int targetX, int targetY) throws GameOverException{
        
        int xMoving = targetX - current.getX();
        int yMoving = targetY - current.getY();
        
        Direction xDirection = xMoving > 0 ? Direction.EAST : Direction.WEST;
        Direction yDirection = yMoving > 0 ? Direction.NORTH : Direction.SOUTH;
        
        CurrentMapVew view = null;
        
        while(current.getX() != targetX){
            view = engine.move(human, xDirection);
            current = view.getCurrentPersonPoint();
        }
        
        while(current.getY() != targetY){
            view = engine.move(human, yDirection);
            current = view.getCurrentPersonPoint();
        }
        
        return view;
    }
    
}
