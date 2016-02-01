package ch.ethz.challenge;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Database {
	public Ants api;
	
	public Map<UnitRole, Strategy> strategies = new EnumMap<UnitRole, Strategy>(UnitRole.class);
	
	ArrayList<Unit> unitsAll;
	
	Map<Tile, Unit> unitsByPositionNext = new HashMap<Tile, Unit>();
	
	ArrayList<Tile> enemies;
	
	ArrayList<Tile> ourBases;
	
	ArrayList<Tile> enemyBases;
		
	// Called on start of round, before strategies are evaluated
	public void update() {
		
		for(Unit u : unitsAll) {
			u.setAim(null);
		}
		
//		unitPositions.clear();
//		unitPositions.addAll(api.getMyAnts());

		unitsAll.clear();
		for (Tile myAntPos : api.getMyAnts()) {
			Unit u = unitsByPositionNext.getOrDefault(myAntPos, null);
			
			if(u == null)
			{
				u = new Unit(api);
				u.positionNow = myAntPos;
				
				unitsAll.add(u);
			}
			else
			{
				unitsAll.add(u);
			}
		}
		
		for(Unit u : unitsAll)
		{
			float bestPrio = 0;
			UnitRole bestRole = UnitRole.JustCreated;
			
			for (Map.Entry<UnitRole, Strategy> entry : strategies.entrySet())
			{
				float prio = entry.getValue().priority(u);
				if(prio > bestPrio)
				{
					
				}
			}
			
        }
	}
}
