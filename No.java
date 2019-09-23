/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alggenetic;

import java.util.ArrayList;

/**
 *
 * @author danie
 */
public class No {
    ArrayList<Integer> passos;
    int[][] matriz;

    int fitness;

    public int[][] getMatriz() {
        int[][] mat = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mat[i][j] = matriz[i][j];
            }
        }
        return mat;
    }

    public void setMatriz(int[][] matriz) {
        this.matriz = matriz;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public ArrayList<Integer> getPassos() {
        return passos;
    }

    public void setPassos(ArrayList<Integer> passos) {
        this.passos = passos;
    }

    
    
    

}
