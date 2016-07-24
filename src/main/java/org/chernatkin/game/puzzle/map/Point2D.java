package org.chernatkin.game.puzzle.map;

public class Point2D {

    private final PointType type;
    
    private final int x;
    
    private final int y;
    
    public Point2D(PointType type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public PointType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj == null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        Point2D other = (Point2D) obj;
        if (x != other.x || y != other.y) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Point2D [type=" + type + ", x=" + x + ", y=" + y + "]";
    }
}
