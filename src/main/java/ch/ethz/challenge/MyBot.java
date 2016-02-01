package ch.ethz.challenge;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;


/**
 * Starter bot implementation.
 */
public class MyBot extends Bot {
	public Database database = new Database();
	
	public Map<UnitRole, Strategy> strategies = new EnumMap<UnitRole, Strategy>(UnitRole.class);
	
    /**
     * Main method executed by the game engine for starting the bot.
     * 
     * @param args command line arguments
     * 
     * @throws IOException if an I/O error occurs
     */
    public static void main(String[] args) throws IOException {
        new MyBot().readSystemInput();
    }
    
    /**
     * For every ant check every direction in fixed order (N, E, S, W) and move it if the tile is
     * passable.
     */
    @Override
    public void doTurn() {
        Ants api = getAnts();
        
        this.database.api = api;
        this.database.update();
        
        for (Tile myAnt : api.getMyAnts()) {
            for (Aim direction : Aim.values()) {
                if (api.getIlk(myAnt, direction).isPassable()) {
                    api.issueOrder(myAnt, direction);

                    break;
                }
            }
        }
    }
}
