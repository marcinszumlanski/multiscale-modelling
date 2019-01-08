package main;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;

class Main extends JFrame {
	private Grid grid1;
	private GrainMaker nowyGM;
	private NHHandler nh;
	private InclusionHandler ih;
	private InclusionHandler ihInitial;



	JButton buttonCalculate;
	JRadioButton circle, square;
	JTextField textFieldGrainCount, textBeforeFieldInclsionCount, textFieldInclsionCount, textFieldInclsionSize;
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem menuItem, menuItem2, menuItem3, menuItem4, menuItemEND;
	JLabel labelImage, labelText1, labelText2, labelText3, inclusionSize;
	boolean circleFlag = true;
	public static void main(String[] args) {
		new Main();
	}
	
	public Main() {


		this.setSize(500, 500);
		Toolkit tk = Toolkit.getDefaultToolkit();
		
		Dimension dim = tk.getScreenSize();
		
		int xPos = (dim.width / 2) - (this.getWidth() /2 );
		int yPos = (dim.height / 2) - (this.getHeight() /2 );
		
		this.setLocation(xPos, yPos);
		
		this.setResizable(false);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setTitle("CA Grain Growth");
		
		JPanel thePanel = new JPanel();
		JPanel thePanel2 = new JPanel();
		JPanel thePanel3 = new JPanel();


		circle = new JRadioButton("Circle");
		square = new JRadioButton("Square");
		ButtonGroup bG = new ButtonGroup();
		bG.add(circle);
		bG.add(square);
		this.add(circle);
		this.add(square);
		circle.setSelected(true);
		thePanel3.add(circle);
		thePanel3.add(square);


		labelText1 = new JLabel("Amount of grains:");
		thePanel.add(labelText1);

		textFieldGrainCount = new JTextField(5);
		thePanel.add(textFieldGrainCount);

		inclusionSize = new JLabel("Inclusion Size:");
		thePanel.add(inclusionSize);

		textFieldInclsionSize = new JTextField(5);
		thePanel.add(textFieldInclsionSize);

		labelText3 = new JLabel("Inclusion initial /");
		thePanel3.add(labelText3);

		labelText2 = new JLabel("Inclusion Final");
		thePanel3.add(labelText2);

		textBeforeFieldInclsionCount = new JTextField(5);
		thePanel3.add(textBeforeFieldInclsionCount);



		textFieldInclsionCount = new JTextField(5);
		thePanel3.add(textFieldInclsionCount);


		this.menuCreater();

		buttonCalculate = new JButton("Start simulation");
		thePanel3.add(buttonCalculate);

		labelImage = new JLabel();
		ImageIcon imgDefault = new ImageIcon("default.png");
		labelImage.setIcon(imgDefault);
		thePanel2.add(labelImage);


		//LISTENR OBJECT!!!!!!!!!!!!!!!!!!
		ListenForButton lFB =  new ListenForButton();

		menuItem.addActionListener(lFB);
		menuItem2.addActionListener(lFB);
		menuItem3.addActionListener(lFB);
		menuItem4.addActionListener(lFB);
		menuItemEND.addActionListener(lFB);
		buttonCalculate.addActionListener(lFB);
		circle.addActionListener(lFB);
		square.addActionListener(lFB);

		this.add(thePanel, BorderLayout.PAGE_START);
		this.add(thePanel3, BorderLayout.CENTER);
		this.add(thePanel2, BorderLayout.PAGE_END);
		this.setVisible(true);
		this.setJMenuBar(menuBar);
	}


	private void menuCreater(){
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		menuBar.add(menu);
		// ########## MENU ITEMS ########
		menuItem = new JMenuItem("Save as png...");
		menu.add(menuItem);

		menuItem2 = new JMenuItem("Save as txt...");
		menu.add(menuItem2);

		menuItem3 = new JMenuItem("Load from png..");
		menu.add(menuItem3);

		menuItem4 = new JMenuItem("Load from txt...");
		menu.add(menuItem4);

		menuItemEND = new JMenuItem("Exit");
		menu.add(menuItemEND);
		// ############## END ###########
	}


	private class ListenForButton implements ActionListener {
		private JFileChooser fc;

		public ListenForButton() {
			this.fc = new JFileChooser();
		}
		
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == buttonCalculate) {
				grid1 = new Grid(300, 300);
				String stringNOG = textFieldGrainCount.getText();

				ihInitial = new InclusionHandler(grid1);
				ihInitial.addInclusionInitial(Integer.parseInt(textBeforeFieldInclsionCount.getText()), Integer.parseInt(textFieldInclsionSize.getText()), circleFlag);

				int numberOfGrain = Integer.parseInt(stringNOG);
				nowyGM = new GrainMaker(numberOfGrain, grid1.getSizeX(), grid1.getSizeY());

				grid1.fillTheGrid(nowyGM.getListOfGrain());

				//tu jest petla
				nh = new NHHandler(grid1);
				boolean isFull = false;
				while(!isFull)
				{
					isFull = nh.checkIfFull();
					nh.makeAGrainGrowth();
				}

				grid1.updateGridArray(nh.getArrayNew());

				ih = new InclusionHandler(grid1);
				ih.addInclusion(Integer.parseInt(textFieldInclsionCount.getText()), Integer.parseInt(textFieldInclsionSize.getText()), circleFlag);
				grid1.updateGridArray(ih.getGridArray());
				grid1.mapFunction(numberOfGrain);

				labelImage.setIcon(grid1.getImage());
			}

			if(e.getSource() == menuItemEND){
				System.exit(0);
			}

			if (e.getSource() == menuItem2) {
				int retVal = fc.showSaveDialog(null);
				if(retVal == JFileChooser.APPROVE_OPTION) {
					try {
						File file = new File(fc.getSelectedFile() + ".txt");
						grid1.exportToTxt(file);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
			}

			if(e.getSource() == menuItem){
				int retVal = fc.showSaveDialog(null);
				if(retVal == JFileChooser.APPROVE_OPTION) {
					File file = new File(fc.getSelectedFile() + ".png");
					grid1.saveImage(file);
				}
			}

			if(e.getSource() == menuItem3){
				int retVal = fc.showOpenDialog(null);
				if(retVal == JFileChooser.APPROVE_OPTION){
					grid1 = new Grid(300,300);
					labelImage.setIcon(grid1.loadGridArrayFromPng(
							fc.getSelectedFile()
					));
				}
			}

			if(e.getSource() == menuItem4){
				int retVal = fc.showOpenDialog(null);
				if (retVal == JFileChooser.APPROVE_OPTION){
					grid1 = new Grid(300, 300);
					grid1.loadGridArrayFromTxt(fc.getSelectedFile());
					labelImage.setIcon(grid1.getImage());
				}
			}

			if(e.getSource() == circle){
					circleFlag = true;
			}

			if(e.getSource() == square){
					circleFlag = false;
			}
		}
	}
}
