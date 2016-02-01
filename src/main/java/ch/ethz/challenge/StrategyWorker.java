package ch.ethz.challenge;
import java.util.*;

// Example strategy
public class StrategyWorker extends Strategy {
	
	@Override
	public UnitRole myRole() {
		return UnitRole.Worker;
	}

	@Override
	public float priority(Unit u) {
		float res = 0;
		if (u.roleNow == UnitRole.Worker){
			res += 0.5f;
		}
		if (u.getFoodAmount() > 0){
			res += 0.5f;
		}
		
		Set<Tile> enemy = getApi().getEnemyAnts();
		
		for(Tile e : enemy){
			if (database.api.getDistance(e, u.positionNow) < 10){
				res *= 0.9;
			}
		}
		
		for (Tile f : database.api.getFoodTiles()){
			int dist = database.api.getDistance(u.positionNow, f);
			if (dist < 30){
				res *= 1.1;
			}
		}
		
		return res;
	}
	
	@Override
	public void perform(Unit u) {
		if (u.getFoodAmount() > 0){ // Bringing food home.
			if (database.ourBases.size() < 1){
				return; // No home :(
			}
			
			Tile closestBase = null;
			int minDist = Integer.MAX_VALUE;
			
			for (Tile b : database.ourBases){
				int dist = database.api.getDistance(u.positionNow, b);
				if (dist < minDist){
					minDist = dist;
					closestBase = b;
				}
			}
			
			BFS bfs = new BFS (u.positionNow, closestBase, database);
			
			Aim aim = bfs.goTo(true);
			
			if (!database.unitsByPositionNext.containsKey(aim.transformedTile(u.positionNow))){
				u.setAim(aim);
			}
			else{
				u.setAim(null);
			}
			
		}
		else { // Looking for food.
			Tile closestFood = null;
			int minDist = Integer.MAX_VALUE;
			
			for (Tile f : database.api.getFoodTiles()){
				int dist = database.api.getDistance(u.positionNow, f);
				if (dist < minDist){
					minDist = dist;
					closestFood = f;
				}
			}
			BFS bfs = new BFS (u.positionNow, closestFood, database);
			Aim aim = bfs.goTo(true);
			
			if (!database.unitsByPositionNext.containsKey(aim.transformedTile(u.positionNow))){
				u.setAim(aim);
			}
			else{
				u.setAim(null);
			}
		}
		u.roleNext = myRole();
	}
	
}
