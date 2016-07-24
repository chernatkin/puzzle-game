package org.chernatkin.game.puzzle.character;

public class Weapon {

    private final String name;
    
    private final int strength;
    
    private final int distance;

    public Weapon(String name, int strength, int distance) {
        this.name = name;
        this.strength = strength;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public int getStrength() {
        return strength;
    }

    public int getDistance() {
        return distance;
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
        Weapon other = (Weapon) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Weapon [name=" + name + ", strength=" + strength + ", distance=" + distance + "]";
    } 
    
}
