package ch.ethz.challenge;

import java.util.Set;

public class StrategyExplore extends Strategy {
	@Override
	public UnitRole myRole() {
		return UnitRole.Explore;
	}
	
	@Override
	public float priority(Unit u) {
		return 0.1f;
	}

	@Override
	public void perform(Unit u) {
		while(u.explorationTarget == null)
		{
			Tile newTarget = new Tile((int)(Math.random() * database.api.getRows()), (int)(Math.random() * database.api.getCols()));
			if(database.api.getIlk(newTarget).isPassable())
			{
				u.explorationTarget = newTarget;
			}
		}
		
		BFS bfs = new BFS (u.positionNow, u.explorationTarget, database);
		Aim aim = bfs.goTo(true);
		u.setAim(aim);		
	}
	
}
