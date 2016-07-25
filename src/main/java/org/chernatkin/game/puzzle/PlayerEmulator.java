package org.chernatkin.game.puzzle;

import java.util.Map;
import java.util.Random;

import org.chernatkin.game.puzzle.character.GamePerson;
import org.chernatkin.game.puzzle.engine.GameEngine;
import org.chernatkin.game.puzzle.engine.GameOverException;
import org.chernatkin.game.puzzle.map.CurrentMapVew;
import org.chernatkin.game.puzzle.map.Direction;
import org.chernatkin.game.puzzle.map.Point2D;

public class PlayerEmulator implements Runnable {

    private static final Direction[] DIRECTIONS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    
    private final GamePerson person;
    
    private final GameEngine engine;
    
    private final Random rand = new Random();
    
    private volatile boolean alive = true;
    
    public PlayerEmulator(GamePerson person, GameEngine engine) {
        this.person = person;
        this.engine = engine;
    }

    @Override
    public void run() {
        
        try{
            randomWalk();
        } catch(Exception e){
            return;
        }
    }
    
    private void randomWalk() throws GameOverException{
        
        CurrentMapVew view = engine.getCurrentView(person);
        
        while(alive && person.getHealth() > GameEngine.MIN_HEALTH_LEVEL){
            Direction direction = getHumanTarget(view.getVisiblePersons(), view.getCurrentPersonPoint());
            if(direction != null) {
                view = getOrDefault(engine.applyWeapon(person, direction, person.getCharacter().getWeapon()), view);
            }
            else{
                view = getOrDefault(engine.move(person, getRandomDirection()), view);
            }
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return;
            }
            
            alive = alive && person.getHealth() > GameEngine.MIN_HEALTH_LEVEL;
        }
    }
    
    private static <T> T getOrDefault(T t, T defaultValue){
        return t != null ? t : defaultValue;
    }
    
    private Direction getRandomDirection(){
        return DIRECTIONS[rand.nextInt(DIRECTIONS.length - 1)];
    }

    private Direction getHumanTarget(Map<Point2D, GamePerson> visiblePersons, Point2D currentPersonPoint){
        for(Map.Entry<Point2D, GamePerson> entry : visiblePersons.entrySet()){
        	if(entry.getValue().isBot()){
        		continue;
        	}
            
            Direction direction = getDirectionIfAvailable(currentPersonPoint, entry.getKey());
            if(direction != null){
                return direction;
            }
        }
        return null;
    }
    
    private Direction getDirectionIfAvailable(Point2D currentPersonPoint, Point2D point){
        return Direction.valueOf(point.getX() - currentPersonPoint.getX(), point.getY() - currentPersonPoint.getY());
    }
    
    public void stop(){
        alive = false;
    }
}
