package main;
import java.util.*;

class coordRandomizer {
	private Random rand;
	private int maxDestX, maxDestY;

	coordRandomizer(int maxDestX, int maxDestY){
		this.rand = new Random();	
		this.maxDestX = maxDestX;
		this.maxDestY = maxDestY;
	}
	
	public Point getTwoRandomNumber() {
		Point point = new Point();
		point.x = this.rand.nextInt(this.maxDestX);
		point.y = this.rand.nextInt(this.maxDestY);
		return point;
	}
		
}