package montecarlo;

import java.awt.Color;
import static java.awt.Color.MAGENTA;
import static java.awt.Color.white;
import static java.lang.System.arraycopy;

public class Moore {
    
    /**
     *
     * @param previousStep
     * @param sizeX
     * @param sizeY
     * @param isPeriodic
     * @return
     */
    public static Color[][] regMoor(Color[][] previousStep, int sizeX, int sizeY, boolean isPeriodic) {
        Color[][] currentStep = new Color[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++) {
            arraycopy(previousStep[i], 0, currentStep[i], 0, sizeY);
        }
        int l;
        int r;
        int down;
        int up;
        for (int i = 1; i < sizeX - 1; i++) {
            for (int j = 1; j < sizeY - 1; j++) {
                down = j - 1;
                up = j + 1;
                l = i - 1;
                r = i + 1;
                if (previousStep[i][j] != Color.white) {
                    if (isPeriodic) {
                        if (i - 1 == 0) {
                            l = sizeX - 2;
                        }
                        if (i + 1 == sizeX - 1) {
                            r = 1;
                        }
                        if (j - 1 == 0) {
                            down = sizeY - 2;
                        }
                        if (j + 1 == sizeY - 1) {
                            up = 1;
                        }
                    }
                    if (checkNeighborColor(previousStep, l, down, i, j)) {
                        currentStep[l][down] = previousStep[i][j];
                    }
                    if (checkNeighborColor(previousStep, l, j, i, j)) {
                        currentStep[l][j] = previousStep[i][j];
                    }
                    if (checkNeighborColor(previousStep, i, down, i, j)) {
                        currentStep[i][down] = previousStep[i][j];
                    }
                    if (checkNeighborColor(previousStep, r, down, i, j)) {
                        currentStep[r][down] = previousStep[i][j];
                    }
                    if (checkNeighborColor(previousStep, l, up, i, j)) {
                        currentStep[l][up] = previousStep[i][j];
                    }
                    if (checkNeighborColor(previousStep, r, up, i, j)) {
                        currentStep[r][up] = previousStep[i][j];
                    }
                    if (checkNeighborColor(previousStep, r, j, i, j)) {
                        currentStep[r][j] = previousStep[i][j];
                    }
                    if (checkNeighborColor(previousStep, i, up, i, j)) {
                        currentStep[i][up] = previousStep[i][j];
                    }
                }
            }
        }
        return currentStep;
    }

    private static boolean checkNeighborColor(Color[][] previousStep, int w_i, int w_j, int i, int j) {
        return previousStep[w_i][w_j] == white && previousStep[i][j] != MAGENTA;
    }
   
    /**
     *
     */
    public Moore() {
    } 
}
