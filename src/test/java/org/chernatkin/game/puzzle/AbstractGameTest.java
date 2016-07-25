package org.chernatkin.game.puzzle;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.chernatkin.game.puzzle.serialization.GameLoader;
import org.junit.BeforeClass;

public class AbstractGameTest {
    
    @BeforeClass
    public static void beforeClass() throws Exception {
        
        replaceVariable("WEAPONS_FILE_PATH", "/descriptors/weapons.csv");
        replaceVariable("CHARACTERS_FILE_PATH", "/descriptors/characters.csv");
        replaceVariable("POINT_TYPES_FILE_PATH", "/descriptors/point_types.csv");
        replaceVariable("MAPS_FILE_PATH", "/descriptors/maps");
        replaceVariable("GAMES_FILE_PATH", "/descriptors/games");
    }

    private static void replaceVariable(String varName, String newValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, URISyntaxException{
        newValue = Paths.get(AbstractGameTest.class.getResource(newValue).toURI()).toAbsolutePath().toString();
        
        Field f = GameLoader.class.getDeclaredField(varName);
        f.setAccessible(true);
        f.set(null, newValue);
    }
}
