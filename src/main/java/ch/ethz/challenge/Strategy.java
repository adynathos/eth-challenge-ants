package ch.ethz.challenge;

public abstract class Strategy {
	public abstract UnitRole myRole();
	
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
