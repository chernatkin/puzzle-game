package org.chernatkin.game.puzzle.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chernatkin.game.puzzle.character.GamePerson;
import org.chernatkin.game.puzzle.character.Weapon;
import org.chernatkin.game.puzzle.map.CurrentMapVew;
import org.chernatkin.game.puzzle.map.Direction;
import org.chernatkin.game.puzzle.map.Game2DMap;
import org.chernatkin.game.puzzle.map.Point2D;

public class GameEngine {
    
    public static final int MAX_HEALTH_LEVEL = 100;
    
    public static final int MIN_HEALTH_LEVEL = 0;

    private final Game2DMap map;
    
    private final Map<GamePerson, Point2D> persons;
    
    private final Map<Point2D, GamePerson> occupiedPoints;
    
    public GameEngine(Game2DMap map, Map<GamePerson, Point2D> persons){
        this.map = map;
        this.persons = persons;
        
        this.occupiedPoints = new HashMap<>(persons.size(), 1);
        
        for(Map.Entry<GamePerson, Point2D> entry: persons.entrySet()){
            this.occupiedPoints.put(entry.getValue(), entry.getKey());
        }
    }
    
    public synchronized CurrentMapVew move(GamePerson person, Direction direction) throws GameOverException{
        Point2D p = map.getPointForMove(persons.get(person), direction);
        if(p == null){
            return getCurrentView(person);
        }
        
        moveToPoint(person, p);
        
        return getCurrentView(person);
    }
    
    public synchronized CurrentMapVew getCurrentView(GamePerson person) throws GameOverException{
        Point2D currentPoint = persons.get(person);
        
        if(currentPoint == null){
            throw new GameOverException("Game over. You Killed!");
        }
        
        if(persons.size() == 1){
            throw new GameOverException("All enemy killed. You win!");
        }
        
        final Point2D[][] visiblePoints = currentPoint == null ? null : map.getVisibleMap(currentPoint);
        
        Map<Point2D, GamePerson> visiblePersons = new HashMap<>(occupiedPoints.size());
        
        for(int i = 0; i < visiblePoints.length; i++){
            Point2D[] rowPoints = visiblePoints[i];
            for(int j = 0; j < rowPoints.length; j++){
                if(occupiedPoints.containsKey(rowPoints[j])){
                    visiblePersons.put(rowPoints[j], occupiedPoints.get(rowPoints[j]));
                }
            }
        }
        
        return new CurrentMapVew(visiblePoints, visiblePersons, currentPoint);
    }
    
    public synchronized CurrentMapVew applyWeapon(GamePerson person, Direction direction, Weapon weapon) throws GameOverException{
        
        isPersonHaveWeapon(person, weapon);
        
        attack(person, direction, weapon);
        
        return getCurrentView(person);
    }
    
    private boolean attack(GamePerson person, Direction direction, Weapon weapon){
        List<Point2D> attackedPoints = map.getSortedPoints(persons.get(person), direction, weapon.getDistance());
        if(attackedPoints == null || attackedPoints.isEmpty()){
            return false;
        }
        
        for(Point2D attackedPoint : attackedPoints){
            GamePerson attackedPerson = occupiedPoints.get(attackedPoint);
            if(attackedPerson == null){
                continue;
            }
            
            reduceHealth(person, attackedPerson, weapon, attackedPoint);
            return true;
        }
        return false;
    }
    
    private void reduceHealth(GamePerson person, GamePerson attackedPerson, Weapon weapon, Point2D attackedPoint){
        final int oldHealth = attackedPerson.getHealth();
        final int newHealth = Math.max(MIN_HEALTH_LEVEL, oldHealth - weapon.getStrength());
        
        attackedPerson.setHealth(newHealth);
        
        if(attackedPerson.getHealth() <= MIN_HEALTH_LEVEL){
            occupiedPoints.remove(attackedPoint);
            persons.remove(attackedPerson);
        }
        int healthChange = oldHealth - newHealth;
        person.setExperence(person.getExperence() + healthChange);
    }
    
    private void isPersonHaveWeapon(GamePerson person, Weapon weapon){
        if(!person.getCharacter().getWeapon().equals(weapon)){
            throw new IllegalArgumentException("Current person have no such weapon:" + weapon.getName());
        }
    }
    
    private void moveToPoint(GamePerson person, Point2D p){
        Point2D curPoint = persons.get(person);
        occupiedPoints.remove(curPoint);
        
        occupiedPoints.put(p, person);
        persons.put(person, p);
    }

    public synchronized Map<GamePerson, Point2D> getPersons() {
        return persons;
    }
    
    public String getMapName(){
        return map.getName();
    }
}
