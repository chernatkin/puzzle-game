package org.chernatkin.game.puzzle.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Game2DMap {

    private static final int MAX_VISIBLE_DISTANCE = 20; 
    
    private final String name;
    
    private final Point2D[][] map;
    
    private final int xSize;
    
    private final int ySize;

    public Game2DMap(String name, int xSize, int ySize, Collection<Point2D> points) {
        this.name = name;
        this.map = new Point2D[ySize][xSize];
        this.xSize = xSize;
        this.ySize = ySize;
        
        for(Point2D p : points){
            map[p.getY()][p.getX()] = p;
        }
    }
    
    public Point2D[][] getVisibleMap(Point2D standPoint){
        
        int fromX = Math.max(standPoint.getX() - MAX_VISIBLE_DISTANCE, 0);
        int fromY = Math.max(standPoint.getY() - MAX_VISIBLE_DISTANCE, 0);
        
        int toX = Math.min(standPoint.getX() + MAX_VISIBLE_DISTANCE, xSize - 1);
        int toY = Math.min(standPoint.getY() + MAX_VISIBLE_DISTANCE, ySize - 1);
        
        Point2D[][] visibleMap = new Point2D[toY - fromY + 2][toX - fromX + 2];
        
        for(int i = fromY; i <= toY; i++){
            for(int j = fromX; j <= toX; j++){
                visibleMap[i - fromY][j - fromX] = map[i][j];
            }
        }
        
        return visibleMap;
    }
    
    protected Point2D getPoint(Point2D srcPoint, Direction direction){
        int newX = srcPoint.getX() + direction.getX();
        int newY = srcPoint.getY() + direction.getY();
        
        if(newX >= xSize || newY >= ySize || newY < 0 || newX < 0){
            return null;
        }
        
        return map[newY][newX];
    }
    
    public Point2D getPointForMove(Point2D srcPoint, Direction direction){
        if(srcPoint == null){
            return null;
        }
        
        Point2D dstPoint = getPoint(srcPoint, direction);
        
        if(dstPoint == null || !dstPoint.getType().isWalkable()){
            return null;
        }
        
        return dstPoint;
    }
    
    public List<Point2D> getSortedPoints(Point2D point, Direction direction, int distance){
        if(point == null){
            return Collections.emptyList();
        }
        List<Point2D> points = new ArrayList<>(distance);
        
        Point2D currentPoint = getPoint(point, direction);
        int currentPointDistance = 1;
        
        while(currentPoint != null && currentPointDistance <= distance){
            points.add(currentPoint);
            
            currentPoint = getPoint(currentPoint, direction);
            currentPointDistance++;
        }
        
        return points;
    }

    public String getName() {
        return name;
    }

}
