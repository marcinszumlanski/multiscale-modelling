package main;


class NHHandler {
	private int[][] gridArrayOld, gridArrayNew;
	private int dX;
	private int dY;
	
	
	NHHandler(Grid grid1){
		this.dX = grid1.getSizeX(); 
		this.dY = grid1.getSizeY();
		gridArrayOld = new int[this.dX][this.dY];
		gridArrayNew = new int[this.dX][this.dY];
		this.copyArray(grid1);
	}
	
	
	//copy grid to avoid reference
	private void copyArray(Grid grid1) {
		for (int i = 0; i < this.dX; i++)
		{
			for(int j = 0; j < this.dY; j++)
			{
				this.gridArrayOld[i][j] = grid1.showOnePixel(i,j);
				this.gridArrayNew[i][j] = grid1.showOnePixel(i,j);
			}
		}
	}
	
	//Overwrite array no reference
	private void swapArrays() {
		for (int i = 0; i < this.dX; i++)
		{
			for(int j = 0; j < this.dY; j++)
			{
				this.gridArrayOld[i][j] = this.gridArrayNew[i][j];
			}
		}
	}

	
	//neighborhood function #1
	private void neumann() {
		for(int i = 0; i < this.dX; i++ ) {
			for(int j = 0; j < this.dY; j++) {
				if(i != 0) {
					if(this.gridArrayNew[i][j] != this.gridArrayOld[i-1][j] && this.gridArrayNew[i][j] == 0 && this.gridArrayOld[i-1][j] != 1) {
						this.gridArrayNew[i][j] = this.gridArrayOld[i-1][j];
					}
				}
				if( i != this.dX - 1) {
					if(this.gridArrayNew[i][j] != this.gridArrayOld[i+1][j] && this.gridArrayNew[i][j] == 0 && this.gridArrayOld[i+1][j] != 1) {
						this.gridArrayNew[i][j] = this.gridArrayOld[i+1][j];
					}
				}
				if( j != this.dY - 1) {
					if(this.gridArrayNew[i][j] != this.gridArrayOld[i][j+1] && this.gridArrayNew[i][j] == 0 && this.gridArrayOld[i][j+1] != 1) {
						this.gridArrayNew[i][j] = this.gridArrayOld[i][j+1];
					}			
				}
				if( j != 0) {
					if(gridArrayNew[i][j] != gridArrayOld[i][j-1] && gridArrayNew[i][j] == 0 && this.gridArrayOld[i][j-1] != 1) {
						gridArrayNew[i][j] = gridArrayOld[i][j-1];
					}				
				}
			}
		}	
	}

	private void advancedMore(){
		int count;
		for(int i =0; i < dX; i++){
			for(int j=0; j < dY; j++){
				//counting number
				//if()
			}
		}
	}


	public boolean checkIfFull() {
		for (int i = 0; i < this.dX; i++)
		{
			for(int j = 0; j < this.dY; j++)
			{
				if(this.gridArrayNew[i][j] == 0) return false;
			}
		}
		return true;
	}

	public void makeAGrainGrowth() {

			this.swapArrays();
			neumann();

	}
	
	public int[][] getArrayNew() {
		return gridArrayNew;
	}		
}
