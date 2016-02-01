package ch.ethz.challenge;

public class Unit {
	protected MyBot database;
	
	public Tile positionNow;
	public Tile positionNext;
	public UnitRole roleNow = UnitRole.JustCreated;
	public Aim nextAim;	
	
	public Tile explorationTarget = null;
	
	public Unit(MyBot db) {
		this.database = db;
	}
	
	public void setAim(Aim aim) {
		nextAim = aim;
		if(aim == null)
		{
			this.positionNext = this.positionNow;
		}
		else
		{
			this.positionNext = database.api.getTile(this.positionNow, aim);
		}
	}
	
	public int getFoodAmount() {
		return database.getAnts().getFoodAmount(this.positionNow);
	}
}
