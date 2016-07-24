package org.chernatkin.game.puzzle.character;

public class GamePerson {

    private final GameCharacter character;
    
    private final String name;
    
    private volatile int health;
    
    private volatile int experence;
    
    private final boolean bot;

    public GamePerson(String name, GameCharacter character, int health, int experence, boolean bot) {
        this.character = character;
        this.name = name;
        this.health = health;
        this.experence = experence;
        this.bot = bot;
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getExperence() {
        return experence;
    }

    public void setExperence(int experence) {
        this.experence = experence;
    }

    public boolean isBot() {
        return bot;
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
        GamePerson other = (GamePerson) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
