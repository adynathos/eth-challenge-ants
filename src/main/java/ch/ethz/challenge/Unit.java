package ch.ethz.challenge;

public class Unit {
	protected Ants api;
	
	public Tile positionNow;
	public Tile positionNext;
	public UnitRole roleNow;
	public UnitRole roleNext;
	public Aim nextAim;	
	
	public Unit(Ants api) {
		this.api = api;
	}
	
	public void setAim(Aim aim) {
		nextAim = aim;
		if(aim == null)
		{
			this.positionNext = this.positionNow;
		}
		else
		{
			this.positionNext = aim.transformedTile(this.positionNow);
		}
	}
	
	public int getFoodAmount() {
		return api.getFoodAmount(this.positionNow);
	}
}
