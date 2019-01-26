package DifferentialEvolution;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Differential {
    int dimensions = 2;
    int n;
    double F; // [0,2]
    double CR; // [0,1]
    int space;
    int function;

    double[][] population;
    Random random;

    double[] mutant;

    double[] finalists;
    double SD = Double.POSITIVE_INFINITY;
    double STOP;

    int generation = 0;
    double minimum = Double.POSITIVE_INFINITY;

    ObjectOutputStream out;


    public Differential(int function, int n, double F, double CR, int space, ObjectOutputStream out, double STOP) {
        this.function = function;
        this.n = n;
        this.F = F;
        this.CR = CR;
        this.space = space;
        this.out = out;
        this.STOP = STOP;

    }

    public void run() {
        population = new double[n][dimensions];
        random = new Random();
        for (int x = 0; x < n; x++) {
            for (int dim = 0; dim < dimensions; dim++) {
                population[x][dim] = 0 + random.nextFloat() * (space); //random.nextFloat() [0,1]
            }
        }
        System.out.println(SD);
        while (SD > STOP) {

            generation++;

            for (int x = 0; x < population.length; x++) {
                ArrayList<Integer> list = new ArrayList<>();
                for (int i = 1; i < population.length; i++) {
                    list.add(i);
                }

                int finalX = x;
                list.removeIf(a -> a.equals(finalX));

                mutant = new double[dimensions];

                for (int dim = 0; dim < dimensions; dim++) {
                    Collections.shuffle(list);
                    int a = list.get(0);
                    int b = list.get(1);
                    int c = list.get(2);

                    if (random.nextFloat() < this.CR) {
                        mutant[dim] = a + F * (b - c);

                    } else mutant[dim] = population[x][dim];

                }

                if (function == 1) {

                    if (Functions.RosenBrock(mutant[0], mutant[1]) < Functions.RosenBrock(population[x][0], population[x][1])) {
                        population[x] = mutant;
                    }

                    for (double[] doubles : population) {
                        if (Functions.RosenBrock(doubles[0], doubles[1]) < minimum) {
                            minimum = Functions.RosenBrock(doubles[0], doubles[1]);
                        }
                    }

                }
                if (function == 2) {
                    if (Functions.ackleysFunction(mutant[0], mutant[1]) < Functions.ackleysFunction(population[x][0], population[x][1])) {
                        population[x] = mutant;
                    }

                    for (double[] doubles : population) {
                        if (Functions.ackleysFunction(doubles[0], doubles[1]) < minimum) {
                            minimum = Functions.ackleysFunction(doubles[0], doubles[1]);
                        }
                    }

                }

                if (function == 3) {
                    if (Functions.boothsFunction(mutant[0], mutant[1]) < Functions.boothsFunction(population[x][0], population[x][1])) {
                        population[x] = mutant;
                    }


                    for (double[] doubles : population) {
                        if (Functions.boothsFunction(doubles[0], doubles[1]) < minimum) {
                            minimum = Functions.boothsFunction(doubles[0], doubles[1]);
                        }
                    }
                }

                if (function == 4) {
                    if (Functions.XsqerYsqer(mutant[0], mutant[1]) < Functions.XsqerYsqer(population[x][0], population[x][1])) {
                        population[x] = mutant;
                    }

                    for (double[] doubles : population) {
                        if (Functions.XsqerYsqer(doubles[0], doubles[1]) < minimum) {
                            minimum = Functions.XsqerYsqer(doubles[0], doubles[1]);
                        }
                    }
                }
            }

            finalists = new double[population.length];

            if (function == 1) {
                for (int x = 0; x < finalists.length; x++) {
                    finalists[x] = Functions.RosenBrock(population[x][0], population[x][1]);
                }
            }
            if (function == 2) {
                for (int x = 0; x < finalists.length; x++) {
                    finalists[x] = Functions.ackleysFunction(population[x][0], population[x][1]);
                }
            }
            if (function == 3) {
                for (int x = 0; x < finalists.length; x++) {
                    finalists[x] = Functions.boothsFunction(population[x][0], population[x][1]);
                }
            }
            if (function == 4) {
                for (int x = 0; x < finalists.length; x++) {
                    finalists[x] = Functions.XsqerYsqer(population[x][0], population[x][1]);
                }
            }

            SD = StandardDeviation.calculateSD(finalists);
            //System.out.println(SD);
            try {
                out.writeUTF("StandardDeviation of population");
                out.writeUTF(Double.toString(SD));
                out.flush();
            } catch (IOException e) {
                e.getMessage();
            }

        }

        System.out.println(minimum);
        try {
            out.writeUTF("Best minimum is " + minimum + " after generations " + generation);
            out.flush();
            out.writeUTF("END");
            out.flush();

            out.writeObject(population);
            out.flush();
        } catch (IOException e) {
            e.getMessage();
        }
    }

}


