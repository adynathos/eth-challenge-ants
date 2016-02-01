package ch.ethz.challenge;

public abstract class Strategy {
	public Database database; // set after contruction automatically
	
	public abstract UnitRole myRole();	
	
	public Ants getApi() {
		return this.database.api;
	}
	
	// Priority of doing this action
	public float priority(Unit u)
	{
		return 0;
	}
	
	// Set fields on unit like u.positionNext
	public void perform(Unit u)
	{
		
	}
}
