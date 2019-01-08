package main;

class Grain {
	private Point coords;
	private int grainID;
	private static int ID = 1;
	
	Grain(Point coords){
		this.coords = coords;
		ID++;
		this.grainID = ID;
	}

	public int getGrainID() { return this.grainID; }
	public Point getCoords() { return this.coords; }
	
	public void setGrainID(int grainID) {
		this.grainID = grainID;
	}
}
