package org.chernatkin.game.puzzle.serialization;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.chernatkin.game.puzzle.character.GamePerson;
import org.chernatkin.game.puzzle.map.Point2D;

public class GameState {

    private final String mapName;
    
    private final Map<GamePerson, Point2D> persons;

    public GameState(String mapName, Map<GamePerson, Point2D> persons) {
        this.mapName = mapName;
        this.persons = persons;
    }

    public String getMapName() {
        return mapName;
    }

    public Map<GamePerson, Point2D> getPersons() {
        return persons;
    }
    
    public GamePerson getHuman(){
        return persons.keySet().stream()
                               .filter(p -> !p.isBot())
                               .findFirst().get();
    }
    
    public List<GamePerson> getBots(){
        return persons.keySet().stream()
                               .filter(p -> p.isBot())
                               .collect(Collectors.toList());
    }
}
