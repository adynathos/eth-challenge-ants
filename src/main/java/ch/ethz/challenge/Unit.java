package ch.ethz.challenge;

public class Unit {
	protected MyBot database;
	
	public Tile positionNow;
	public Tile positionNext;
	public UnitRole roleNow = UnitRole.JustCreated;
	public Aim nextAim;	
	
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
			this.positionNext = aim.transformedTile(this.positionNow, database.api);
		}
	}
	
	public int getFoodAmount() {
		return database.api.getFoodAmount(this.positionNow);
	}
}
