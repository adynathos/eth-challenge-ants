package ch.ethz.challenge;

public class Unit {
	public Tile positionNow;
	public Tile positionNext;
	public UnitRole roleNow;
	public UnitRole roleNext;
	public Aim nextAim;
	
	void setAim(Aim aim) {
		nextAim = aim;
		this.positionNext = aim.transformedTile(this.positionNow);
	}
}
