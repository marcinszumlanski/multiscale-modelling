package main;
import java.util.*;
class GrainMaker {
	private ArrayList<Grain> listOfGrain;
	private ArrayList<Point> listOfPoints;
	private int numberOfGrain;
	
	GrainMaker(int numberOfGrain, int maxX, int maxY){
		this.numberOfGrain = numberOfGrain;
		listOfGrain = new ArrayList<Grain>();
		listOfPoints = new ArrayList<Point>();
		this.addGrainToList(maxX, maxY);
	}
	
	private void addGrainToList(int maxX, int maxY) {
		for(int i = 0; i < this.numberOfGrain; i++) {
			Point newCoords;
			do
			{
				coordRandomizer randomizer = new coordRandomizer(maxX, maxY);
				newCoords = randomizer.getTwoRandomNumber();
			}
			while (listOfPoints.contains(newCoords));

			listOfPoints.add(newCoords);
			listOfGrain.add(new Grain(newCoords));
		}
	}

	public void showAllGrain() {
		for(Grain x: this.listOfGrain) {
			System.out.println("Dla ziarna nr: " + x.getGrainID() + ", X wynosi: " +
					x.getCoords().x +", Y wynosi: " + x.getCoords().y + ".");
		}
	}
	
	public ArrayList<Grain> getListOfGrain(){
		return this.listOfGrain;
	}
}
