package main;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


class Grid {
	private int[][] gridArray;
	public int [][] mappedGridArray;
	private final int sizeX;
	private final int sizeY;
	private BufferedImage image;
	
	Grid(){
		this.sizeX = 0;
		this.sizeY = 0;
	}
	
	Grid(int sizeX, int sizeY){
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.gridArray = new int[this.sizeX][this.sizeY];
		this.mappedGridArray = new int[this.sizeX][this.sizeY];
	}
	
	//########### getteres ##########
	public int getSizeX() {	return this.sizeX; }
	
	public int getSizeY() { return this.sizeY; }
	
	public int showOnePixel(int x, int y) {
		return this.gridArray[x][y];		
	}

	public void changeValue(int value, int x, int y) {
		this.gridArray[x][y] = value;
	}

	public void fillTheGrid(ArrayList<Grain> listOfGrain) {
		for(Grain x: listOfGrain) {
			this.gridArray[x.getCoords().x][x.getCoords().y] = x.getGrainID();
		}
	}

	public void showGrid() {
		for(int i = 0; i < this.sizeX ; i++) {
			for(int j = 0; j < this.sizeY; j++ ) {
				System.out.print(this.gridArray[i][j] + " ");
			}
			System.out.println(" ");
		}
	}
	
	public int[][] returnGridArray() {
		return this.gridArray;		
	}
	
	public void updateGridArray(int[][] gridArray) {
		this.gridArray = gridArray;
	}
	
	public void mapFunction(int numberOfGrain) {
		double nof = (double) numberOfGrain;
		for (int i = 0; i < sizeX; i++)
		{
			for(int j = 0; j < sizeY; j++)
			{
				if(gridArray[i][j] != 1)
					mappedGridArray[i][j] = (int)(gridArray[i][j] / nof * 16777215);
			}
		}
	}
	
	//matrix -> image
	public ImageIcon getImage() {
		//String path = "" + name + ".png";
		image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
	    for (int x = 0; x < sizeX; x++) {
	        for (int y = 0; y < sizeY; y++) {
	            image.setRGB(x, y, mappedGridArray[x][y]);
	        }
	    }
	    ImageIcon img = new ImageIcon(image);
	    return img;
	}

	public void saveImage(File outputfile){
		try {
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			              
		}
	}
	
	public void exportToTxt(File outputfile) throws FileNotFoundException {
		try (PrintWriter out = new PrintWriter(outputfile)) {
		    for(int i = 0; i < sizeX; i++) {
		    	for(int j = 0; j < sizeY; j++) {
		    		out.println(i + " " + j + " " + gridArray[i][j]);
		    	}
		    }
		}
	}

	public void loadGridArrayFromTxt(File file){
		try {
			BufferedReader br = new BufferedReader(new FileReader((file)));
			String readLine = "";
			int x,y,value;
			while ((readLine = br.readLine()) != null) {
				String[] splitedArray = readLine.split(" ");
				x = Integer.parseInt(splitedArray[0]);
				y = Integer.parseInt(splitedArray[1]);
				value = Integer.parseInt(splitedArray[2]);
				gridArray[x][y] = value;
			}


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public ImageIcon loadGridArrayFromPng(File file){
		image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
		}

		return new ImageIcon(image);
	}
}
