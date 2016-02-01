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
			if (DistanceUtil.Dist(e, u.positionNext) < 10){
				res /= 1.1;
			}
		}
		
		return res;
	}
	
	@Override
	public void perform(Unit u) {
		u.roleNext = myRole();
		
		
		u.setAim(Aim.EAST);
	}
	
}
