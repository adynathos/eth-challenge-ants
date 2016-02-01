package ch.ethz.challenge;

// Example strategy
public class StrategyWorker extends Strategy {
	
	@Override
	public UnitRole myRole() {
		return UnitRole.Worker;
	}

	@Override
	public float priority(Unit u) {
		return 0;
	}
	
	@Override
	public void perform(Unit u) {
		u.setAim(Aim.EAST);
	}
	
}
