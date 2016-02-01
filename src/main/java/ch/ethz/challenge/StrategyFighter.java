package ch.ethz.challenge;
import java.util.*;

// Example strategy
public class StrategyFighter extends Strategy {
	
	@Override
	public UnitRole myRole() {
		return UnitRole.Fighter;
	}

	@Override
	public float priority(Unit u) {
		float res = 0;
		
		if (u.getFoodAmount() > 0){
			return 0;
		}
		
		if (u.roleNow == UnitRole.Fighter){
			res += 0.5f;
		}
		
		Set<Tile> friends = getApi().getMyAnts();
		
		for(Tile f : friends){
			if (database.api.getDistance(f, u.positionNow) < 20){
				res *= 1.1;
			}
		}
		
		return res;
	}
	
	@Override
	public void perform(Unit u) {
		 // Attack the enemy's base.
		Tile closestBase = null;
		int minDist = Integer.MAX_VALUE;
		
		for (Tile b : database.api.getEnemyHills()){
			int dist = database.api.getDistance(u.positionNow, b);
			if (dist < minDist){
				minDist = dist;
				closestBase = b;
			}
		}
		
		if(closestBase != null)
		{
			if(database.api == null)
			{
				return;
			}
			
			BFS bfs = new BFS (u.positionNow, closestBase, database);
			Aim aim = bfs.goTo(true);

			if (!database.unitsByPositionNext.containsKey(aim.transformedTile(u.positionNow, database.api))){
				u.setAim(aim);
			}
			else{
				u.setAim(null);
			}
		}
		else
		{
			u.setAim(null);
		}
		
	}
	
}
