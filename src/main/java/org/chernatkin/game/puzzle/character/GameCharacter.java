package org.chernatkin.game.puzzle.character;

public class GameCharacter {

    private final String name;
    
    private final String symbol;
    
    private final Weapon weapon;

    public GameCharacter(String name, String symbol, Weapon weapon) {
        this.name = name;
        this.symbol = symbol;
        this.weapon = weapon;
    }

    public String getName() {
        return name;
    }
    
    public Weapon getWeapon() {
        return weapon;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        GameCharacter other = (GameCharacter) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GameCharacter [name=" + name + ", weapon=" + weapon + "]";
    }
    
}
