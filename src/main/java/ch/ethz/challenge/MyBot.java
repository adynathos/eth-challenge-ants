package ch.ethz.challenge;

import java.io.IOException;
import java.util.*;

/**
 * Starter bot implementation.
 */
public class MyBot extends Bot {
	public Ants api;
	
	public Map<UnitRole, Strategy> strategies = new EnumMap<UnitRole, Strategy>(UnitRole.class);
	
	ArrayList<Unit> unitsAll = new ArrayList<Unit>();
	
	Map<Tile, Unit> unitsByPositionNext = new HashMap<Tile, Unit>();
	
	ArrayList<Tile> enemies = new ArrayList<Tile>();

	private Map<Tile, Tile> orders = new HashMap<Tile, Tile>();

    private Set<Tile> unseenTiles = new HashSet<Tile>();

    private Set<Tile> enemyHills = new HashSet<Tile>();

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
    
    public void setup(int loadTime, int turnTime, int rows, int cols, int turns, int viewRadius2,
            int attackRadius2, int spawnRadius2) {
        super.setup(loadTime, turnTime, rows, cols, turns, viewRadius2, attackRadius2,
            spawnRadius2);
        
        Strategy s1 = new StrategyWorker();
        s1.database = this;
        this.strategies.put(s1.myRole(), s1);
        
        Strategy s2 = new StrategyFighter();
        s2.database = this;
        this.strategies.put(s2.myRole(), s2);
        
        Strategy s3 = new StrategyExplore();
        s3.database = this;
        this.strategies.put(s3.myRole(), s3);
    }
    
    private List<Tile> getInterestingPoints(int n) {
        List<Tile> l = new LinkedList<Tile>();

        return l;
    }


    private boolean doMoveDirection(Tile antLoc, Aim direction) {
        Ants ants = getAnts();
        // Track all moves, prevent collisions
        Tile newLoc = ants.getTile(antLoc, direction);
        if (ants.getIlk(newLoc).isUnoccupied() && !orders.containsKey(newLoc)) {
            ants.issueOrder(antLoc, direction);
            orders.put(newLoc, antLoc);
            return true;
        } else {
            return false;
        }
    }

    private boolean doMoveLocation(Tile antLoc, Tile destLoc) {
        Ants ants = getAnts();
        // Track targets to prevent 2 ants to the same location
        List<Aim> directions = ants.getDirections(antLoc, destLoc);
        BFS bfs = new BFS(antLoc, destLoc, this);
        Aim toGo = bfs.goTo(true);
        if (toGo == null) {
            for (Aim direction : directions) {
                if (doMoveDirection(antLoc, direction)) {
                    return true;
                }
            }
        } else {
            if (doMoveDirection(antLoc, toGo)){
                return true;
            } else {
                for (Aim direction : directions) {
                    if (doMoveDirection(antLoc, direction)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * For every ant check every direction in fixed order (N, E, S, W) and move it if the tile is
     * passable.
     */
    @Override
    public void doTurn() {
    	api = getAnts();
    	
	    for(Unit u : unitsAll) {
			u.setAim(null);
		}
	    
	    orders.clear();

		unitsAll.clear();
		for (Tile myAntPos : api.getMyAnts()) {
			Unit u = unitsByPositionNext.getOrDefault(myAntPos, null);
			
			if(u == null)
			{
				u = new Unit(this);
				u.database = this;
			}
			u.positionNow = myAntPos;
			
			unitsAll.add(u);
		}
		
		for(Unit u : unitsAll)
		{
			float bestPrio = -1;
			UnitRole bestRole = UnitRole.JustCreated;
			
			for (Map.Entry<UnitRole, Strategy> entry : strategies.entrySet())
			{
				float prio = entry.getValue().priority(u);
				if(prio > bestPrio)
				{
					bestRole = entry.getValue().myRole();
					bestPrio = prio;
				}
				
				u.roleNow = bestRole;
				Strategy s = strategies.getOrDefault(bestRole, null);
				
				if(s != null)
				{
					s.perform(u);
				}
				
				if(u.nextAim != null)
				{
					unitsByPositionNext.put(u.positionNext, u);
					orders.put(u.positionNow, u.positionNext);
					api.issueOrder(u.positionNow, u.nextAim);
					u.positionNow = u.positionNext;
				}
			}
        }
	    
		return;
/*		
        Ants ants = getAnts();
        orders.clear();
        Map<Tile, Tile> foodTargets = new HashMap<Tile, Tile>();

        // add all locations to unseen tiles set, run once
        if (unseenTiles == null) {
            unseenTiles = new HashSet<Tile>();
            for (int row = 0; row < ants.getRows(); row++) {
                for (int col = 0; col < ants.getCols(); col++) {
                    unseenTiles.add(new Tile(row, col));
                }
            }
        }

        // remove any tiles that can be seen, run each turn
        for (Iterator<Tile> locIter = unseenTiles.iterator(); locIter.hasNext(); ) {
            Tile next = locIter.next();
            if (ants.isVisible(next)) {
                locIter.remove();
            }
        }

        // prevent stepping on own hill
        for (Tile myHill : ants.getMyHills()) {
            orders.put(myHill, null);
        }


        // find close food
        List<Route> foodRoutes = new ArrayList<Route>();
        TreeSet<Tile> sortedFood = new TreeSet<Tile>(ants.getFoodTiles());
        TreeSet<Tile> sortedAnts = new TreeSet<Tile>(ants.getMyAnts());

        //If has a food on it comes back home
        for (Tile antLoc : sortedAnts) {
            if (ants.getFoodAmount(antLoc) == 0) continue;
            int distanceMin = 9999999;
            Tile bestTile = null;
            for (Tile myHill : ants.getMyHills()) {
                int distTemp = ants.getDistance(antLoc, myHill);
                if (distTemp < distanceMin) {
                    distanceMin = distTemp;
                    bestTile = myHill;
                }
            }
            if (bestTile != null) {
                doMoveLocation(antLoc, bestTile);
            }
        }

        for (Tile foodLoc : sortedFood) {
            for (Tile antLoc : sortedAnts) {
                int distance = ants.getDistance(antLoc, foodLoc);
                Route route = new Route(antLoc, foodLoc, distance);
                foodRoutes.add(route);
            }
        }
        Collections.sort(foodRoutes);
        for (Route route : foodRoutes) {
            if (!foodTargets.containsKey(route.getEnd())
                    && !foodTargets.containsValue(route.getStart())
                    && doMoveLocation(route.getStart(), route.getEnd())) {
                foodTargets.put(route.getEnd(), route.getStart());
            }
        }

        // add new hills to set
        for (Tile enemyHill : ants.getEnemyHills()) {
            if (!enemyHills.contains(enemyHill)) {
                enemyHills.add(enemyHill);
            }
        }
        // attack hills
        List<Route> hillRoutes = new ArrayList<Route>();
        for (Tile hillLoc : enemyHills) {
            for (Tile antLoc : sortedAnts) {
                if (!orders.containsValue(antLoc)) {
                    int distance = ants.getDistance(antLoc, hillLoc);
                    Route route = new Route(antLoc, hillLoc, distance);
                    hillRoutes.add(route);
                }
            }
        }
        Collections.sort(hillRoutes);
        for (Route route : hillRoutes) {
            doMoveLocation(route.getStart(), route.getEnd());
        }

        // explore unseen areas
        for (Tile antLoc : sortedAnts) {
            if (!orders.containsValue(antLoc)) {
                List<Route> unseenRoutes = new ArrayList<Route>();
                for (Tile unseenLoc : unseenTiles) {
                    int distance = ants.getDistance(antLoc, unseenLoc);
                    Route route = new Route(antLoc, unseenLoc, distance);
                    unseenRoutes.add(route);
                }
                Collections.sort(unseenRoutes);
                for (Route route : unseenRoutes) {
                    if (doMoveLocation(route.getStart(), route.getEnd())) {
                        break;
                    }
                }
            }
        }

        // unblock hills
        for (Tile myHill : ants.getMyHills()) {
            if (ants.getMyAnts().contains(myHill) && !orders.containsValue(myHill)) {
                for (Aim direction : Aim.values()) {
                    if (doMoveDirection(myHill, direction)) {
                        break;
                    }
                }
            }
        }
    */
    }
}
