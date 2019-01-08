package main;

import java.util.Random;

public class InclusionHandler {
	private int[][] gridArray;	
	
	
	public InclusionHandler(Grid grid){
		this.gridArray = grid.returnGridArray();	
	}
	
	
	
	public void addInclusion(int numberOfInclusions, int size, boolean isCircle) {
		int count = 0;
		Point coords = new Point();
        Random rand = new Random();
		while(count < numberOfInclusions) {
			int failCount = 0;
			coords.x = rand.nextInt(gridArray.length - 1);
			coords.y = rand.nextInt(gridArray[0].length - 1);
//			if(coords.x < gridArray.length - 1)
//			{
				if((gridArray[coords.x][coords.y] != gridArray[coords.x+1][coords.y] && gridArray[coords.x][coords.y]!= 1 && gridArray[coords.x+1][coords.y]!=1)
				|| (gridArray[coords.x][coords.y] != gridArray[coords.x][coords.y+1] && gridArray[coords.x][coords.y]!= 1 && gridArray[coords.x][coords.y+1]!=1))
				{
					if(addSingleInclusion(coords, size, isCircle))
					{
						count++;
						failCount = 0;
					}
				}
				else
				{
					failCount++;
				}
				if(failCount > 20)
				{
					count++;
				}
//			}
		}
	}

	public void addInclusionInitial(int numberOfInclusions, int size, boolean isCircle) {
		int count = 0;
		Point coords = new Point();
		Random rand = new Random();
		while(count < numberOfInclusions) {
			int failCount = 0;
			coords.x = rand.nextInt(gridArray.length - 1);
			coords.y = rand.nextInt(gridArray[0].length - 1);
//			if(coords.x < gridArray.length - 1)
//			{
				if(addSingleInclusion(coords, size, isCircle))
				{
					count++;
					failCount = 0;
				}
				else
				{
					failCount++;
				}

				if(failCount > 20)
				{
					count++;
				}
//			}
		}
	}

	boolean addSingleInclusion(Point coords, int size, boolean isCircle)
	{
		gridArray[coords.x][coords.y] = 1;

		int halfSize = size / 2;

		Point bottomRight = new Point();
		bottomRight.x = coords.x + halfSize;
		bottomRight.y = coords.y + halfSize;

		Point upperLeft = new Point();
		upperLeft.x = coords.x - halfSize;
		upperLeft.y = coords.y - halfSize;

		if (bottomRight.x < gridArray.length - 1 && bottomRight.y < gridArray[0].length && upperLeft.x > 0 && upperLeft.y > 0)
		{
			for(int i = upperLeft.x; i < bottomRight.x; i++)
			{
				for (int j = upperLeft.y; j<bottomRight.y; j++)
				{
					if(isCircle) {
						if ((Math.pow(i - coords.x, 2)) + (Math.pow(j - coords.y, 2)) <= Math.pow(halfSize, 2))
						{
							gridArray[i][j] = 1;
						}
					}
					else
					{
						gridArray[i][j] = 1;
					}
				}
			}
		}
		else {
			return false;
		}
		return true;

	}

	public int[][] getGridArray(){
		return gridArray;
	}

}
