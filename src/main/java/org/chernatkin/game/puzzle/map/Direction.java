package org.chernatkin.game.puzzle.map;

public enum Direction {
    
    NORTH(0, 1),
    
    SOUTH(0, -1),
    
    WEST(-1, 0),
    
    EAST(1, 0),
    
    NORTH_EAST(1, -1),
    
    NORTH_WEST(-1, -1),
    
    SOUTH_WEST(-1, 1),
    
    SOUTH_EAST(1, 1);
    
    private final int x;
    
    private final int y;

    private Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public static Direction valueOf(int x, int y){
        for(Direction direction : Direction.values()){
            if(direction.x == x && direction.y == y){
                return direction;
            }
        }
        
        return null;
    }
}
