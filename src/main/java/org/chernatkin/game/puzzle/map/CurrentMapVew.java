package org.chernatkin.game.puzzle.map;

import java.util.Map;

import org.chernatkin.game.puzzle.character.GamePerson;

public class CurrentMapVew {
    
    private Point2D[][] visibleMap;
    
    private Map<Point2D, GamePerson> visiblePersons;
    
    private Point2D currentPersonPoint;

    public CurrentMapVew(Point2D[][] visibleMap, Map<Point2D, GamePerson> visiblePersons, Point2D currentPersonPoint) {
        this.visibleMap = visibleMap;
        this.visiblePersons = visiblePersons;
        this.currentPersonPoint = currentPersonPoint;
    }

    public Point2D[][] getVisibleMap() {
        return visibleMap;
    }

    public void setVisibleMap(Point2D[][] visibleMap) {
        this.visibleMap = visibleMap;
    }

    public Map<Point2D, GamePerson> getVisiblePersons() {
        return visiblePersons;
    }

    public void setVisiblePersons(Map<Point2D, GamePerson> visiblePersons) {
        this.visiblePersons = visiblePersons;
    }

    public Point2D getCurrentPersonPoint() {
        return currentPersonPoint;
    }
    
    public GamePerson getCurrentPerson(){
        return getVisiblePersons().get(getCurrentPersonPoint());
    }

}
