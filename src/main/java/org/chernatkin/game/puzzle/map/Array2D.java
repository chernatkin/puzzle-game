package org.chernatkin.game.puzzle.map;

public class Array2D<T> {
    
    private final T [][] map;
    
    private final int fromX;
    
    private final int toX;
    
    private final int fromY;
    
    private final int toY;

    public Array2D(T[][] map, int fromY, int toY, int fromX, int toX) {
        this.map = map;
        this.fromX = fromX;
        this.toX = toX;
        this.fromY = fromY;
        this.toY = toY;
    }

    public T load(int x, int y){
        if(!inRange(x, 0, lengthX() - 1) || !inRange(y, 0, lengthY() - 1)){
            throw new ArrayIndexOutOfBoundsException(String.format("Index {%s, %s} sould be in [%s, %s] - [%s, %s]", 
                                                                   x, y, fromX, fromY, toX, toY));
        }
        
        return map[fromY + y][fromX + x];
    }
    
    public boolean isGlobalCoordinateBelongArray(int x, int y){
        return inRange(x, fromX, toX) && inRange(y, fromY, toY);
    }
    
    public int lengthX(){
        return toX - fromX + 1;
    }
    
    public int lengthY(){
        return toY - fromY + 1;
    }

    private static boolean inRange(int value, int from, int to){
        return value >= from && value <= to;
    }
    
}
