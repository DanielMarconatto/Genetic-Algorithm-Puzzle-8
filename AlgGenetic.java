/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alggenetic;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author danie
 */
public class AlgGenetic {

    /**
     * @param args the command line arguments
     */
    static int[][] obj;
    static boolean encontrou = false;

    static int populacaoSize = 100;
    static int parentes = 3;

    public static void setObjetivo() {
        obj = new int[3][3];

        obj[0][0] = 1;
        obj[0][1] = 2;
        obj[0][2] = 3;

        obj[1][0] = 4;
        obj[1][1] = 5;
        obj[1][2] = 6;

        obj[2][0] = 7;
        obj[2][1] = 8;
        obj[2][2] = 0;
    }

    public static int[][] iniciaMatriz() {
        int[][] inicial = new int[3][3];
        /*
        inicial[0][0] = 0;
        inicial[0][1] = 1;
        inicial[0][2] = 3;

        inicial[1][0] = 4;
        inicial[1][1] = 2;
        inicial[1][2] = 5;

        inicial[2][0] = 7;
        inicial[2][1] = 8;
        inicial[2][2] = 6;
         */

        inicial[0][0] = 2;
        inicial[0][1] = 6;
        inicial[0][2] = 0;

        inicial[1][0] = 5;
        inicial[1][1] = 7;
        inicial[1][2] = 3;

        inicial[2][0] = 8;
        inicial[2][1] = 1;
        inicial[2][2] = 4;
        return inicial;

    }

    public static void main(String[] args) {
        setObjetivo();

        No nodo = new No();

        nodo.setMatriz(iniciaMatriz());
        ArrayList<Integer> lista = new ArrayList();
        lista.add(5);
        nodo.setPassos(lista);

        isObjetivo(nodo.getMatriz());
        System.out.println("Estado inicial: \n");
        printMatriz(nodo.getMatriz());

        int t = 0;
        No[] populacao = geraPopulacaoInicial(nodo);

        for (int i = 0; i < populacao.length; i++) {
            populacao[i].setFitness(Fitness(populacao[i]));
            isObjetivo(populacao[i].getMatriz());
        }

        System.out.println("População Inicial: ");
        for (int i = 0; i < populacaoSize; i++) {
            System.out.println("Individuo: " + i);
            printMatriz(populacao[i].getMatriz());
        }

        while (!encontrou) {
            t++;
            populacao = selecionaPopulacao(populacao);
            populacao = geraPopulacao(populacao);
            for (int i = 0; i < populacao.length; i++) {
                populacao[i].setFitness(Fitness(populacao[i]));
                isObjetivo(populacao[i].getMatriz());
            }
        }
        System.out.println("---------------------");
        System.out.println("Gerações: " + t);

        System.out.println("População Final: \n");
        for (int i = 0; i < populacaoSize; i++) {
            System.out.println("Individuo: " + i);
            printMatriz(populacao[i].getMatriz());
        }

    }

    public static No[] geraPopulacao(No[] populacao) {
        Random random = new Random();
        No[] population = new No[populacaoSize];
        for (int i = 0; i < population.length; i++) {
            ArrayList<Integer> moves = movimentos(populacao[i].getMatriz());
            No individuo = new No();
            int move = moves.get(random.nextInt(moves.size()));
            individuo.setMatriz(movimenta(populacao[i].getMatriz(), move));
            ArrayList<Integer> lista = populacao[i].getPassos();
            lista.add(move);
            individuo.setPassos(lista);
            population[i] = individuo;
        }
        return population;
    }

    public static No[] ordenaPopulacao(No[] populacao) {
        for (int i = 1; i < populacao.length; i++) {
            No aux = populacao[i];
            int j = i;
            while ((j > 0) && (populacao[j - 1].getFitness() > aux.getFitness())) {
                populacao[j] = populacao[j - 1];
                j -= 1;
            }
            populacao[j] = aux;
        }
        return populacao;
    }

    public static No[] selecionaPopulacao(No[] populacao) {
        populacao = ordenaPopulacao(populacao);
        for (int i = 0; i < parentes; i++) {
            populacao[(populacao.length / 2) + i] = populacao[i];
        }
        populacao = ordenaPopulacao(populacao);
        return populacao;
    }

    public static void isObjetivo(int[][] mat) {
        boolean igual = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (mat[i][j] != obj[i][j]) {
                    igual = false;
                }
            }
        }
        if (igual) {
            encontrou = true;
            printMatriz(mat);
        }
    }

    public static No[] geraPopulacaoInicial(No nodo) {
        Random random = new Random();
        No[] population = new No[populacaoSize];
        ArrayList<Integer> moves = movimentos(nodo.getMatriz());
        for (int i = 0; i < population.length; i++) {
            No individuo = new No();
            int move = moves.get(random.nextInt(moves.size()));
            ArrayList<Integer> lista = nodo.getPassos();
            lista.add(move);
            individuo.setMatriz(movimenta(nodo.getMatriz(), move));
            individuo.setPassos(lista);
            population[i] = individuo;
        }
        return population;
    }

    public static int Fitness(No nodo) {
        int numPecasTrocadas = calculaPecas(nodo.getMatriz());
        int distanciaManhattan = manhattan(nodo.getMatriz());
        int numInversoes = inversoes(nodo.getMatriz());
        return ((36 * numPecasTrocadas) + (18 * distanciaManhattan) + (2 * numInversoes));
    }

    public static int manhattan(int[][] mat) {
        int total = 0;
        int distancia = 0;
        int ver;
        int hor;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ver = 0;
                hor = 0;
                if ((mat[i][j] != obj[i][j]) && (mat[i][j] != 0)) {

                    for (int k = 0; k < 3; k++) {
                        for (int l = 0; l < 3; l++) {
                            if (mat[i][j] == obj[k][l]) {
                                ver = k - i;
                                hor = l - j;
                            }
                        }
                    }

                    if (ver < 0) {
                        ver = ver * -1;
                    }
                    if (hor < 0) {
                        hor = hor * -1;
                    }
                }
                distancia = ver + hor;
                total += distancia;
            }
        }
        return total;
    }

    public static int inversoes(int[][] mat) {
        int inv = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (mat[i][j] - (mat[i][j + 1]) == 1) {
                    inv++;
                }
            }
        }
        return inv;
    }

    public static int calculaPecas(int[][] mat) {
        int pecas = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if ((mat[i][j] != obj[i][j]) && (mat[i][j] != 0)) {
                    pecas++;
                }
            }
        }
        return pecas;
    }

    public static void printMatriz(int[][] matriz) {
        System.out.println("| " + matriz[0][0] + " , " + matriz[0][1] + " , " + matriz[0][2] + " |");
        System.out.println("| " + matriz[1][0] + " , " + matriz[1][1] + " , " + matriz[1][2] + " |");
        System.out.println("| " + matriz[2][0] + " , " + matriz[2][1] + " , " + matriz[2][2] + " |");
        System.out.println("---------------------");
        System.out.println("\n");
    }

    public static int[][] movimenta(int[][] matriz, int dir) {
        int[][] objetivo = new int[3][3];
        objetivo = matriz;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (matriz[i][j] == 0) {
                    switch (dir) {
                        case 0:
                            //Cima
                            objetivo[i][j] = objetivo[i - 1][j];
                            objetivo[i - 1][j] = 0;
                            return objetivo;
                        case 1:
                            //Baixo                            
                            objetivo[i][j] = objetivo[i + 1][j];
                            objetivo[i + 1][j] = 0;
                            return objetivo;
                        case 2:
                            //Esquerda
                            objetivo[i][j] = objetivo[i][j - 1];
                            objetivo[i][j - 1] = 0;
                            return objetivo;
                        case 3:
                            //Direita
                            objetivo[i][j] = objetivo[i][j + 1];
                            objetivo[i][j + 1] = 0;
                            return objetivo;
                    }
                }
            }
        }
        return objetivo;
    }

    public static ArrayList<Integer> movimentos(int[][] matriz) {

        //0 -> Cima
        //1 -> Baixo
        //2 -> Esquerda
        //3 -> Direita
        ArrayList<Integer> lista = new ArrayList();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (matriz[i][j] == 0) {
                    if (i > 1) {
                        lista.add(0);
                    } else if (i < 1) {
                        lista.add(1);
                    } else {
                        lista.add(0);
                        lista.add(1);
                    }

                    if (j > 1) {
                        lista.add(2);
                    } else if (j < 1) {
                        lista.add(3);
                    } else {
                        lista.add(2);
                        lista.add(3);
                    }
                }
            }
        }
        return lista;
    }

}
