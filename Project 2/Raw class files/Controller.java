package montecarlo;

import java.awt.Color;
import static java.awt.Color.MAGENTA;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Controller extends Moore {
    static Random rand = new Random();
    static Color dominantColor;

    /**
     *
     */
    public static int[][] tempTab;

    /**
     *
     * @param tab
     * @param Width
     * @param Height
     * @param neighborhood
     * @return
     */
    public static Color[][] monteCarlo(Color[][] tab, int Width, int Height, int neighborhood) {
        int energyAfter, energyBefore;
        int randomX[] = fillRandom(Width);
        int randomY[] = fillRandom(Height);

        for (int i : randomX) {
            for (int j : randomY) {
                if (tab[i][j] != MAGENTA) {
                    energyBefore = 0;
                    List<Color> neighbors = getNeighbors(tab, i, j);

                    Set<Color> colors = new HashSet<>();
                    colors.add(tab[i][j]);
                    energyBefore = (int) neighbors.stream().map((neighbor) -> {
                        colors.add(neighbor);
                        return neighbor;
                    }).filter((neighbor) -> (neighbor != tab[i][j])).count();
                    colors.remove(MAGENTA);
                    energyAfter = 0;
                    Color tmp = tab[i][j];
                    tab[i][j] = randColor(colors);

                    energyAfter = (int) neighbors.stream().filter((neighbor) -> (neighbor != tab[i][j])).count();
                    int delta = energyAfter - energyBefore;

                    if (delta > 0) {
                        tab[i][j] = tmp; 
                    }
                }
            }
        }
        return tab;
    }

    /**
     *
     * @param tab
     * @param i
     * @param j
     * @return
     */
    public static List<Color> getNeighbors(Color[][] tab, int i, int j) {
        int l, r, down, up;
        down = j - 1;
        up = j + 1;
        l = i - 1;
        r = i + 1;

        List<Color> neighborsList = new ArrayList<>();
        neighborsList.add(tab[l][down]);
        neighborsList.add(tab[l][j]);
        neighborsList.add(tab[i][down]);
        neighborsList.add(tab[r][down]);
        neighborsList.add(tab[l][up]);
        neighborsList.add(tab[r][up]);
        neighborsList.add(tab[r][j]);
        neighborsList.add(tab[i][up]);
        return neighborsList;
    }

    private static int[] fillRandom(int rozmiarX) {
        int array[] = new int[rozmiarX - 2];
        for (int i = 0; i < rozmiarX - 2; i++) {
                array[i] = i + 1;
        }
        int index;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            if (index != i)
            {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
        return array;
    }                  

    /**
     *
     * @param colors
     * @return
     */
    public static Color randColor(Set<Color> colors) {
        int a = rand.nextInt(colors.size());
        return colors.toArray(new Color[colors.size()])[a];
    }

    /**
     *
     * @param tab
     * @param sizeX
     * @param sizeY
     * @param neighborhood
     * @param tempTab
     * @return
     */
    public static Color[][] recrystalization(Color[][] tab, int sizeX, int sizeY, int neighborhood,
			int[][] tempTab) {
        int energyAfter = 0, energyBefore = 0;
        int losoweX[] = fillRandom(sizeX);
        int losoweY[] = fillRandom(sizeY);

        for (int i : losoweX) {
            for (int j : losoweY) {
                energyBefore = 0;
                List<Color> neighbors = getNeighbors(tab, i, j);
                Set<Color> reclistalNeighbours = new HashSet<>(); 
                if (recrystalColors().contains(tab[i][j])) { 
                        reclistalNeighbours.add(tab[i][j]); 
                }
                energyBefore = (int) neighbors.stream().map((neighbor) -> {            
                    if (recrystalColors().contains(neighbor)) {
                        reclistalNeighbours.add(neighbor); 
                    }
                    return neighbor;
                }).filter((neighbor) -> (neighbor != tab[i][j])).count();
                energyBefore += tempTab[i][j]; 

                if (!reclistalNeighbours.isEmpty()) {
                    energyAfter = 0;
                    Color randomColor = randColor(reclistalNeighbours); 
                    energyAfter = (int) neighbors.stream().filter((neighbor) -> (neighbor != randomColor)).count(); 
                    int delta = energyAfter - energyBefore; 

                    if (delta <= 0) { 
                            tempTab[i][j]=0;
                            tab[i][j] = randomColor; 
                    }
                }
            }
        }
        return tab;
    }

    /**
     *
     * @return
     */
    public static Set<Color> recrystalColors() {
        Set<Color> kolory = new HashSet<>();
        kolory.add(new Color(255, 0, 0));
        kolory.add(new Color(255, 25, 0));
        kolory.add(new Color(255, 50, 0));
        kolory.add(new Color(255, 75, 0));
        kolory.add(new Color(255, 100, 0));
        kolory.add(new Color(255, 125, 0));
        kolory.add(new Color(255, 150, 0));
        kolory.add(new Color(255, 175, 0));
        kolory.add(new Color(255, 200, 0));
        kolory.add(new Color(255, 225, 0));
        kolory.add(new Color(255, 250, 0));
        kolory.add(new Color(255, 37, 0));
        kolory.add(new Color(255, 72, 0));
        kolory.add(new Color(255, 90, 0));

        return kolory;
    }

    private Controller() {
    }
}