package montecarlo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class View extends javax.swing.JFrame {

    private int wait = 0;
    private static final long serialVersionUID = 1L;
    private static final int KRATKA = 0;
    int sizeX; 
    int sizeY; 
    Color tab[][];
    Random rand = new Random();
    int neighborhood = 1;
    boolean isPeriodic = true;
    private boolean isDP = false;
    private int[][] energy_H;
    int nucleation = 0;
    private final int skala = 3;
    boolean isAfterRec = false;
    int where = 0;

    public View() {
        initComponents();
        initCustomComponents();
    }

private void addNucleons(int max) {
    int amount = 0;

    while (amount < max) {
        int x = rand.nextInt(sizeX - 2) + 1;
        int y = rand.nextInt(sizeY - 2) + 1;
        Color color = Controller.randColor(Controller.recrystalColors());
        if (where == 1) {
            if (!Controller.recrystalColors().contains(tab[x][y]) && isGrainInBorder(x, y)) {
                energy_H[x][y] = 0;
                tab[x][y] = color;
                amount++;
            }   
        } else if (where == 2) {
            if (!Controller.recrystalColors().contains(tab[x][y]) && isGrainInsight(x, y)) {
                energy_H[x][y] = 0;
                tab[x][y] = color;
                amount++;
            }
        }
    }
}

private void distrubuteEnergy(int energyInBorders, int energyInsight) {

    energy_H = new int[sizeX][sizeY];

    for (int i = 0; i < sizeX; i++) {
        for (int j = 0; j < sizeY; j++) {
            energy_H[i][j] = energyInsight;
        }
    }

    for (int i = 1; i < sizeX - 1; i++) {
        for (int j = 1; j < sizeY - 1; j++) {
            if (isGrainInBorder(i, j)) { 
                energy_H[i][j] = energyInBorders;
            } else {
                energy_H[i][j] = energyInsight;
            }
        }
    }
}
        
private boolean isGrainInBorder(int i, int j) {

    List<Color> neighb = Controller.getNeighbors(tab, i, j);
    Color baseColor = neighb.get(0);
    
    if (neighb.stream().anyMatch((color) -> (color != baseColor))) {
        return true;
    }
    return false;
}
        
private boolean isGrainInsight(int i, int j) {

    List<Color> neighb = Controller.getNeighbors(tab, i, j);
    Color baseColor = neighb.get(0);

    if (neighb.stream().anyMatch((color) -> (color == baseColor))) {
        return true;
    } 
    return false;
}

private void buttonOkMainActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonOkMainActionPerformed
    for (;;) {
        int s = colorLattice(tab, wait);

        if (s == 0 || (s == sizeX * 2 + sizeY * 2 - 4)) {
            break;
        }
            tab = caStep(tab);
    }
}

private int colorLattice(Color[][] tab2, int p_przerwaczasowa) {
    int s = 0;
    try {
        Thread.sleep(p_przerwaczasowa);
        } catch (InterruptedException ex) {
    }
    Graphics g = getGraphics();
    for (int i = 0; i < sizeX; i++) {
        for (int j = 0; j < sizeY; j++) {
            g.setColor(tab2[i][j]);

            g.fillRect(displayPanel.getX() + 10 + (skala * i), displayPanel.getY()+ 35 + (skala * j), skala - KRATKA, skala - KRATKA);
            if (tab2[i][j] == Color.white) {
                s++;
    }
        }
    }
    return s;
}

private Color[][] caStep(Color[][] tab1) {
    tab1 = Controller.regMoor(tab1, sizeX, sizeY, isPeriodic);
    return tab1;
}

private void generateCA(java.awt.event.ActionEvent evt) {
    inicTab();
    int ilosc = Integer.parseInt(caGrainsNumber.getText());
    int z = 0;
    do {
        z = 0;
        tab[rand.nextInt(sizeX - 2) + 1][rand.nextInt(sizeY - 2) + 1] = new Color(rand.nextInt(180),
                        rand.nextInt(150) + 100, rand.nextInt(180));

        for (int b = 0; b < sizeX; b++) {
            for (int j = 0; j < sizeY; j++) {
                if (tab[b][j] != Color.white) {
                    z++;
                }
            }
        }
    } while (z < ilosc);
}

//CA -> MC
private void buttonDPActionPerformed(ActionEvent evt) {
    wait = 10;
    generateCA(null); //losowanie
    buttonOkMainActionPerformed(null); //kolorowanie i rozrost

    wylosujZiarna(); //losowanie stalych ziaren
    colorLattice(tab, wait);

    addNucleon();

    isDP = true;

    monteCActionPerformed(null);
    wait = 10;
}
                
private void wylosujZiarna() {
    int ilosc = 0;
    int max = Integer.parseInt(dpAmount.getText());
    while (ilosc != max) {
        Color cell = tab[rand.nextInt(sizeX - 2) + 1][rand.nextInt(sizeY - 2) + 1];
        if (cell != Color.MAGENTA) {
            ilosc++;

            for (int i = 0; i < sizeX; i++) {
                for (int j = 0; j < sizeY; j++) {
                    if (cell == tab[i][j]) {
                        tab[i][j] = Color.MAGENTA;
                    }
                }
            }
        }
    }
    for (int i = 0; i < sizeX; i++) {
        for (int j = 0; j < sizeY; j++) {
                if (Color.MAGENTA != tab[i][j]) {
                    tab[i][j] = Color.WHITE;
                }
        }
    }
}

private void addNucleon() {
    int ilosc = 0;
    int max = 80;

    while (ilosc != max) {
        int x = rand.nextInt(sizeX - 2) + 1;
        int y = rand.nextInt(sizeY - 2) + 1;

        if (tab[x][y] != Color.MAGENTA) {
            tab[x][y] = new Color(rand.nextInt(180), rand.nextInt(150) + 100, rand.nextInt(180));
            ilosc++;
        }
    }
}

private void monteCActionPerformed(java.awt.event.ActionEvent evt) {

    Graphics g = getGraphics();
    if (!isDP) {
        inicTab();
    }
    isDP = false;
    Set<Color> kolory = generateMCColors();
    for (int i = 0; i < sizeX; i++) {
        for (int j = 0; j < sizeY; j++) {
            if (tab[i][j] != Color.MAGENTA) {
                tab[i][j] = Controller.randColor(kolory);
            }
        }
    }
    for (int d = 0; d < Integer.parseInt(mcSteps.getText()); d++) {
        MCColors(g, 5);
        tab = Controller.monteCarlo(tab, sizeX, sizeY, neighborhood);

    }
}
        
private Set<Color> generateMCColors() {
    Set<Color> kolory = new HashSet<>();
    int iloscKolorow = Integer.parseInt(colorsNumber.getText());

    for ( int i = 1; i <= iloscKolorow; i++){
        Random rand_device = new Random();
        float r = rand_device.nextFloat();
        float g = rand_device.nextFloat();
        float b = rand_device.nextFloat();
        kolory.add(new Color(r, g, b));
    }	
    return kolory;
} 

private void MCColors(Graphics g, int przerwa) {
    try {
            Thread.sleep(przerwa);
        } catch (InterruptedException ex) {
    }
    for (int i = 0; i < sizeX; i++) {
        for (int j = 0; j < sizeY; j++) {
            if (i == 0) {
                tab[i][j] = tab[1][j];
            }
            if (j == 0) {
                tab[i][j] = tab[i][1];
            }
            if (i == sizeX - 1) {
                tab[i][j] = tab[sizeX - 2][j];
            }
            if (j == sizeY - 1) {
                tab[i][j] = tab[i][sizeY - 2];
            }
        g.setColor(tab[i][j]);
        g.fillRect(displayPanel.getX() + 10 + (skala * i), displayPanel.getY()+ 35 + (skala * j), skala - KRATKA, skala - KRATKA);
        }
    }
}

private void inicTab() {
    sizeX = 125;
    sizeY = 125;
    tab = new Color[sizeX][sizeY];
    for (int i = 0; i < sizeX; i++) {
        for (int j = 0; j < sizeY; j++) {
            if (tab[i][j] != Color.MAGENTA) {
                tab[i][j] = Color.WHITE;
            }
        }
    }
}
                             
private void initCustomComponents() {

    mc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                    monteCActionPerformed(evt);
            }
    });

    buttonGroupLosowanie.add(buttonLosowe1);
    buttonLosowe1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                    generateCA(evt);
            }
    });

    buttonGroupLosowanie.add(ca2mc);
    ca2mc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonDPActionPerformed(evt);
            }

    });

    buttonGroupLosowanie.add(mc2ca);

    buttonGroupNec.add(beginingNucleation);
    buttonGroupNec.add(increasingNucleation);
    buttonGroupNec.add(constantNucleation);
    buttonGroupBorderRed.add(onBorderNucleation);
    buttonGroupBorderRed.add(insideNucleation);
pack();
}// </editor-fold>                        

    /**
     *
     * @param evt
     */
protected void energyActionPerformed(ActionEvent evt) {

    int[][] ener = null;
    if (isAfterRec) {
        ener = Controller.tempTab;
        isAfterRec = false;
    } else {
        int energyInBorders = Integer.parseInt(bordersEnergy.getText());
        int energyInsight = Integer.parseInt(insideEnergy.getText());
        boolean homog = isHomogenous.getState();
        if (homog){
            int homogenous = Integer.parseInt(homogEnergy.getText());
            energyInsight = homogenous;
            energyInBorders = homogenous;
            distrubuteEnergy(energyInBorders, energyInsight);
            ener = energy_H;
        }else{
        distrubuteEnergy(energyInBorders, energyInsight);
        ener = energy_H;
        }
    }

    Color[][] ene = new Color[sizeX][sizeY];

    for (int i = 0; i < sizeX; i++) {
        for (int j = 0; j < sizeY; j++) {
                ene[i][j] = new Color(25 * ener[i][j], 25 * ener[i][j], 25 * ener[i][j]);
        }
    }
    colorLattice(ene, 0);
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupSasiedztwo = new javax.swing.ButtonGroup();
        buttonGroupNec = new javax.swing.ButtonGroup();
        buttonGroupBorderRed = new javax.swing.ButtonGroup();
        buttonGroupLosowanie = new javax.swing.ButtonGroup();
        buttonLosowe1 = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        applyEnergy = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        colorsNumber = new javax.swing.JTextField();
        colorsLabel = new javax.swing.JLabel();
        mcSteps = new javax.swing.JTextField();
        mc = new javax.swing.JButton();
        mcStepsLabel = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        caGrainsNumber = new javax.swing.JTextField();
        caGrainsLabel = new javax.swing.JLabel();
        CA = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        onBorderNucleation = new javax.swing.JRadioButton();
        constantNucleation = new javax.swing.JRadioButton();
        increasingNucleation = new javax.swing.JRadioButton();
        insideNucleation = new javax.swing.JRadioButton();
        beginingNucleation = new javax.swing.JRadioButton();
        labelNucleation1 = new javax.swing.JLabel();
        nucleonsNumber = new javax.swing.JTextField();
        nucleationLabel = new javax.swing.JLabel();
        recrystalize = new javax.swing.JButton();
        showEnergy = new javax.swing.JButton();
        displayPanel = new javax.swing.JPanel();
        labelBoundry1 = new javax.swing.JLabel();
        labelinsight1 = new javax.swing.JLabel();
        bordersEnergy = new javax.swing.JTextField();
        insideEnergy = new javax.swing.JTextField();
        isHomogenous = new java.awt.Checkbox();
        homogEnergy = new javax.swing.JTextField();
        energyDistributionLabel = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        ca2mc = new javax.swing.JRadioButton();
        ca2ca = new javax.swing.JRadioButton();
        mc2mc = new javax.swing.JRadioButton();
        mc2ca = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        dpAmount = new javax.swing.JTextField();
        dpAmountLabel = new javax.swing.JLabel();
        dualPhaseLabel = new javax.swing.JLabel();

        buttonLosowe1.setText("Random");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        applyEnergy.setText("Apply Energy");
        applyEnergy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyEnergyActionPerformed(evt);
            }
        });

        colorsNumber.setText("8");

        colorsLabel.setText("Colors:");

        mcSteps.setText("10");
        mcSteps.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mcStepsActionPerformed(evt);
            }
        });

        mc.setText("Monte Carlo");
        mc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mcActionPerformed(evt);
            }
        });

        mcStepsLabel.setText("MC Steps:");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(mc, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(colorsLabel)
                            .addComponent(mcStepsLabel))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(colorsNumber)
                            .addComponent(mcSteps, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)))))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mcStepsLabel)
                    .addComponent(mcSteps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(colorsNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mc)
                .addContainerGap())
        );

        caGrainsNumber.setText("15");
        caGrainsNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                caGrainsNumberActionPerformed(evt);
            }
        });

        caGrainsLabel.setText("CA grains:");

        CA.setText("Cellular Automata");
        CA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CAActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(CA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(caGrainsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                        .addComponent(caGrainsNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(caGrainsLabel)
                    .addComponent(caGrainsNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CA))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        onBorderNucleation.setText("On borders");
        onBorderNucleation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onBorderNucleationActionPerformed(evt);
            }
        });

        constantNucleation.setText("Const");
        constantNucleation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                constantNucleationActionPerformed(evt);
            }
        });

        increasingNucleation.setText("Increasing");
        increasingNucleation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                increasingNucleationActionPerformed(evt);
            }
        });

        insideNucleation.setText("Inside");
        insideNucleation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insideNucleationActionPerformed(evt);
            }
        });

        beginingNucleation.setText("At the begining");
        beginingNucleation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beginingNucleationActionPerformed(evt);
            }
        });

        labelNucleation1.setText("Nucleons:");

        nucleonsNumber.setText("10");

        nucleationLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        nucleationLabel.setText("Nucleation");

        recrystalize.setText("Crystalize");
        recrystalize.setActionCommand("Recrystalize");
        recrystalize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recrystalizeActionPerformed(evt);
            }
        });

        showEnergy.setText("Show energy");
        showEnergy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showEnergyActionPerformed(evt);
            }
        });

        jLayeredPane1.setLayer(onBorderNucleation, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(constantNucleation, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(increasingNucleation, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(insideNucleation, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(beginingNucleation, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(labelNucleation1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(nucleonsNumber, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(nucleationLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(recrystalize, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(showEnergy, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(recrystalize, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(increasingNucleation)
                    .addComponent(constantNucleation)
                    .addComponent(insideNucleation)
                    .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jLayeredPane1Layout.createSequentialGroup()
                            .addComponent(labelNucleation1)
                            .addGap(18, 18, 18)
                            .addComponent(nucleonsNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(beginingNucleation))
                    .addComponent(onBorderNucleation)
                    .addComponent(nucleationLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(showEnergy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nucleationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nucleonsNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNucleation1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(increasingNucleation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(beginingNucleation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(constantNucleation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(onBorderNucleation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(insideNucleation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(recrystalize, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showEnergy, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLayeredPane1)
                .addGap(0, 0, 0))
        );

        displayPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout displayPanelLayout = new javax.swing.GroupLayout(displayPanel);
        displayPanel.setLayout(displayPanelLayout);
        displayPanelLayout.setHorizontalGroup(
            displayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 365, Short.MAX_VALUE)
        );
        displayPanelLayout.setVerticalGroup(
            displayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 365, Short.MAX_VALUE)
        );

        labelBoundry1.setText("Energy borders:");

        labelinsight1.setText("Energy inside:");

        bordersEnergy.setText("0");

        insideEnergy.setText("0");

        isHomogenous.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        isHomogenous.setLabel("homogenous");

        homogEnergy.setText("0");
        homogEnergy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homogEnergyActionPerformed(evt);
            }
        });

        energyDistributionLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        energyDistributionLabel.setText("Energy distribution");

        ca2mc.setText("Substructure CA ->MC");
        ca2mc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ca2mcActionPerformed(evt);
            }
        });

        ca2ca.setText("Substructure CA -> CA");
        ca2ca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ca2caActionPerformed(evt);
            }
        });

        mc2mc.setText("Substructure MC -> MC");
        mc2mc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mc2mcActionPerformed(evt);
            }
        });

        mc2ca.setText("Substructure MC -> CA");
        mc2ca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mc2caActionPerformed(evt);
            }
        });

        dpAmount.setText("3");
        dpAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dpAmountActionPerformed(evt);
            }
        });

        dpAmountLabel.setText("DP amount:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(dpAmountLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dpAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dpAmountLabel)
                    .addComponent(dpAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        dualPhaseLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        dualPhaseLabel.setText("Dual-Phase");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ca2mc)
                            .addComponent(mc2ca)
                            .addComponent(mc2mc))
                        .addGap(116, 116, 116))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(ca2ca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(dualPhaseLabel)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(dualPhaseLabel)
                .addGap(11, 11, 11)
                .addComponent(ca2mc)
                .addGap(10, 10, 10)
                .addComponent(ca2ca)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mc2ca)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mc2mc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(energyDistributionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(applyEnergy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(isHomogenous, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(labelBoundry1)
                                            .addComponent(labelinsight1))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(bordersEnergy, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                                                .addComponent(insideEnergy, javax.swing.GroupLayout.Alignment.LEADING))
                                            .addComponent(homogEnergy, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(14, 14, 14))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(displayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(998, 998, 998))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(isHomogenous, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(energyDistributionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(2, 2, 2)
                                        .addComponent(homogEnergy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(3, 3, 3)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(bordersEnergy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelBoundry1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(insideEnergy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelinsight1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(applyEnergy)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(displayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

/**/
    private void caGrainsNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_caGrainsNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_caGrainsNumberActionPerformed

    private void ca2caActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ca2caActionPerformed
        wait = 10;
        generateCA(null); //losowanie
        buttonOkMainActionPerformed(null); //kolorowanie i rozrost

        wylosujZiarna(); //losowanie stalych ziaren
        colorLattice(tab, wait);

        addNucleon();

        isDP = true;

        buttonOkMainActionPerformed(null);
        wait = 10;
    }//GEN-LAST:event_ca2caActionPerformed

    private void mc2caActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mc2caActionPerformed
        wait=10;
        monteCActionPerformed(null);
        wylosujZiarna();
        colorLattice(tab, wait);

        addNucleon();

        isDP = false;

        buttonOkMainActionPerformed(null); 
        wait = 10;
    }//GEN-LAST:event_mc2caActionPerformed

    private void mc2mcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mc2mcActionPerformed
        wait = 10;
        monteCActionPerformed(null);//buttonLosoweActionPerformed(null); //losowanie
        wylosujZiarna(); //losowanie stalych ziaren
        colorLattice(tab, wait);
        isDP = true;
        addNucleon();

        wait = 10;
        monteCActionPerformed(null);
    }//GEN-LAST:event_mc2mcActionPerformed

    private void CAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CAActionPerformed
    generateCA(null);

    for (;;) {
        int s = colorLattice(tab, wait);

        if (s == 0 || (s == sizeX * 2 + sizeY * 2 - 4)) {
            break;
        }
            tab = caStep(tab);
    }
    }//GEN-LAST:event_CAActionPerformed

    private void increasingNucleationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_increasingNucleationActionPerformed
        nucleation = 1;
    }//GEN-LAST:event_increasingNucleationActionPerformed

    private void beginingNucleationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beginingNucleationActionPerformed
        nucleation = 2;
    }//GEN-LAST:event_beginingNucleationActionPerformed

    private void constantNucleationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_constantNucleationActionPerformed
        nucleation = 3;
    }//GEN-LAST:event_constantNucleationActionPerformed

    private void onBorderNucleationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onBorderNucleationActionPerformed
        where = 1;
    }//GEN-LAST:event_onBorderNucleationActionPerformed

    private void insideNucleationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insideNucleationActionPerformed
        where = 2;
    }//GEN-LAST:event_insideNucleationActionPerformed

    private void applyEnergyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyEnergyActionPerformed
        int[][] ener = null;
        if (isAfterRec) {
            ener = Controller.tempTab;
            isAfterRec = false;
        } else {
            int energyInBorders = Integer.parseInt(bordersEnergy.getText());
            int energyInsight = Integer.parseInt(insideEnergy.getText());
            boolean homog = isHomogenous.getState();
            if (homog){
                int homogenous = Integer.parseInt(homogEnergy.getText());
                energyInsight = homogenous;
                energyInBorders = homogenous;
                distrubuteEnergy(energyInBorders, energyInsight);
                ener = energy_H;
            }else{
            distrubuteEnergy(energyInBorders, energyInsight);
            ener = energy_H;
            }
        }

        Color[][] ene = new Color[sizeX][sizeY];

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                ene[i][j] = new Color(25 * ener[i][j], 25 * ener[i][j], 25 * ener[i][j]);
            }
        }
        colorLattice(ene, 0);
    }//GEN-LAST:event_applyEnergyActionPerformed

    private void recrystalizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recrystalizeActionPerformed
        int nuclQuant = Integer.parseInt(nucleonsNumber.getText());

        Graphics g = getGraphics();

        if(nucleation == 2)
        {
            addNucleons(nuclQuant);
        }

        for (int i = 0; i < Integer.parseInt(mcSteps.getText()); i++) {
            switch (nucleation) {
                case 1:
                    addNucleons(nuclQuant + nuclQuant * i);
                    break;
                case 3:
                    addNucleons(nuclQuant);
                    break;
                default:
                    break;
            }
            MCColors(g, 20);
            tab = Controller.recrystalization(tab, sizeX, sizeY, neighborhood, energy_H);
        }

        isAfterRec = true;
    }//GEN-LAST:event_recrystalizeActionPerformed

    private void dpAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dpAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dpAmountActionPerformed

    private void mcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mcActionPerformed

    private void mcStepsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mcStepsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mcStepsActionPerformed

    private void ca2mcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ca2mcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ca2mcActionPerformed

    private void homogEnergyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homogEnergyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_homogEnergyActionPerformed

    private void showEnergyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showEnergyActionPerformed
        Color[][] ene = new Color[sizeX][sizeY];

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if(energy_H[i][j] == 0)
                {
                    ene[i][j] = new Color (255, 0, 0);
                }
                else
                {
                    ene[i][j] = new Color(25 * energy_H[i][j], 25 * energy_H[i][j], 25 * energy_H[i][j]);
                }
            }
        }
        colorLattice(ene, 0);
    }//GEN-LAST:event_showEnergyActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) 
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
  
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new View().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CA;
    private javax.swing.JButton applyEnergy;
    private javax.swing.JRadioButton beginingNucleation;
    private javax.swing.JTextField bordersEnergy;
    private javax.swing.ButtonGroup buttonGroupBorderRed;
    private javax.swing.ButtonGroup buttonGroupLosowanie;
    private javax.swing.ButtonGroup buttonGroupNec;
    private javax.swing.ButtonGroup buttonGroupSasiedztwo;
    private javax.swing.JRadioButton buttonLosowe1;
    private javax.swing.JRadioButton ca2ca;
    private javax.swing.JRadioButton ca2mc;
    private javax.swing.JLabel caGrainsLabel;
    private javax.swing.JTextField caGrainsNumber;
    private javax.swing.JLabel colorsLabel;
    private javax.swing.JTextField colorsNumber;
    private javax.swing.JRadioButton constantNucleation;
    private javax.swing.JPanel displayPanel;
    private javax.swing.JTextField dpAmount;
    private javax.swing.JLabel dpAmountLabel;
    private javax.swing.JLabel dualPhaseLabel;
    private javax.swing.JLabel energyDistributionLabel;
    private javax.swing.JTextField homogEnergy;
    private javax.swing.JRadioButton increasingNucleation;
    private javax.swing.JTextField insideEnergy;
    private javax.swing.JRadioButton insideNucleation;
    private java.awt.Checkbox isHomogenous;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel labelBoundry1;
    private javax.swing.JLabel labelNucleation1;
    private javax.swing.JLabel labelinsight1;
    private javax.swing.JButton mc;
    private javax.swing.JRadioButton mc2ca;
    private javax.swing.JRadioButton mc2mc;
    private javax.swing.JTextField mcSteps;
    private javax.swing.JLabel mcStepsLabel;
    private javax.swing.JLabel nucleationLabel;
    private javax.swing.JTextField nucleonsNumber;
    private javax.swing.JRadioButton onBorderNucleation;
    private javax.swing.JButton recrystalize;
    private javax.swing.JButton showEnergy;
    // End of variables declaration//GEN-END:variables
}
