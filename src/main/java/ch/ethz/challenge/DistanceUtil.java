package ch.ethz.challenge;

public class DistanceUtil {
	
	public static int Dist (Tile a, Tile b){
		return Math.abs(a.getCol() - b.getCol()) + Math.abs(a.getRow() - b.getRow());
	}
}
