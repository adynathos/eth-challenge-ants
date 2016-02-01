package ch.ethz.challenge;

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
		if (u.getFoodAmount > 0){
			res += 0.5f;
		}
		
		return res;
	}
	
	@Override
	public void perform(Unit u) {
		u.roleNext = myRole();
		
		
		u.setAim(Aim.EAST);
	}
	
}
