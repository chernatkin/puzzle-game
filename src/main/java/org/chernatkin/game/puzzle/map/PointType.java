package org.chernatkin.game.puzzle.map;

public class PointType {
    
    private final String id;
    
    private final String symbol;
    
    private final boolean transparent;
    
    private final boolean walkable;

    public PointType(String id, String symbol, boolean transparent, boolean walkable) {
        this.id = id;
        this.transparent = transparent;
        this.walkable = walkable;
        this.symbol = symbol;
    }

    public String getId() {
        return id;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PointType other = (PointType) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PointType [id=" + id + ", symbol=" + symbol + ", transparent="
                + transparent + ", walkable=" + walkable + "]";
    }
}
