package Algorithms;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.*;

import Algorithms.Particle.type;


/**
 * swarm of particles
 */
public class Swarm {

    private static double STOP_CONDITION = 0.000001 ;
    private int numOfParticles;
    private double inertia, cognitiveComponent, socialComponent;
    private Matrix bestPosition;
    private double bestEval;
    private type function; // The function to search.
    public static final double DEFAULT_INERTIA = 0.729844;
    public static final double DEFAULT_COGNITIVE = 1.496180; // Cognitive component.
    public static final double DEFAULT_SOCIAL = 1.496180; // Social component.
    ObjectOutputStream out;

    /**
     * When Particles are created they are given a random position.
     * The random position is selected from a specified range.
     * If the begin range is -100 and the end range is 100 then the
     * value will be between -100 (inclusive) and 100 (exclusive).
     */
    private int beginRange, endRange;
    private static final int DEFAULT_BEGIN_RANGE = -100;
    private static final int DEFAULT_END_RANGE = 101;



    /**
     * Construct the Swarm with custom values.
     * @param particles     the number of particles to create
     * @param inertia       the particles resistance to change
     * @param cognitive     the cognitive component or introversion of the particle
     * @param social        the social component or extroversion of the particle
     * @param STOP_CONDITION as name suggests
     * @param out type of outing irrelevant for algorithm necceasry for server
     */
    public Swarm (type function, int particles, double inertia, double cognitive, double social, double STOP_CONDITION, ObjectOutputStream out) {
        this.numOfParticles = particles;
        this.inertia = inertia;
        this.cognitiveComponent = cognitive;
        this.socialComponent = social;
        this.function = function;
        this.out = out;
        this.STOP_CONDITION = STOP_CONDITION;
        double infinity = Double.POSITIVE_INFINITY;
        bestPosition = new Matrix(infinity, infinity, infinity);
        bestEval = Double.POSITIVE_INFINITY;
        beginRange = DEFAULT_BEGIN_RANGE;
        endRange = DEFAULT_END_RANGE;


    }

    /**
     * algorithm.
     */
    public void run (){
        try{
        System.out.println();
        Particle[] particles = initialize();


        double oldEval = bestEval;

        out.writeObject("Creating particles, setting first evaluation as inf");

        out.writeObject("FirstEvaluation\t" + bestEval);

        int generation = 0;

        int holdup = 0;
        ArrayList<Double> Rad = new ArrayList<>();
        // R takiego ze norma roznicy xm (pozycji teraz czastki) i globalnej min -{ r musi byc najwieksza rowne z kazdej czastek }
        // R podzielone przez diameeer (S) swarmu - R dla poczatkowych czastek
        updateRadius(particles,Rad);
        Double R0 = Collections.max(Rad);
        out.writeObject(R0);
        Double R = Collections.max(Rad);
        out.writeObject(R);
        out.writeObject(R/R0);
        while(R/R0 > STOP_CONDITION){
            generation += 1;
            updateRadius(particles,Rad);
            R = Collections.max(Rad);

            if (bestEval == oldEval) {
                holdup += 1;
                out.writeObject("Hold up number : " + holdup + " at " + bestEval);
            }


            if (bestEval < oldEval) {
                holdup = 0;

                out.writeObject("Global Best Evaluation (Epoch " + (generation + 1) + "):\t" + bestEval);
                oldEval = bestEval;
            }

            for (Particle p : particles) {
                p.updatePersonalBest();
                updateGlobalBest(p);
            }

            for (Particle p : particles) {
                updateVelocity(p);
                p.updatePosition();
            }
        }

        out.writeObject("RESULT");
        out.writeObject("x = " + bestPosition.getX());
        System.out.println("x = " + bestPosition.getX());
        out.writeObject("y = " + bestPosition.getY());

            System.out.println("y = " + bestPosition.getY());
        out.writeObject("Final Best Evaluation: " + bestEval);
            System.out.println("Final Best Evaluation: " + bestEval);

        out.writeObject("END");
        out.flush();

        out.writeObject(particles);
        out.flush();


    }catch (IOException e){
            e.getMessage();
        }
    }

    /**
     * Create a set of particles, each with random starting positions.
     * @return  an array of particles
     */
    private Particle[] initialize () {
        Particle[] particles = new Particle[numOfParticles];
        for (int i = 0; i < numOfParticles; i++) {
            Particle particle = new Particle(function, beginRange, endRange);
            particles[i] = particle;
            updateGlobalBest(particle);
        }
        return particles;
    }

    /**
     * Update the global best solution if a the specified particle has
     * a better solution
     * @param particle  the particle to analyze
     */
    private void updateGlobalBest (Particle particle) {
        if (particle.getBestEval() < bestEval) {
            bestPosition = particle.getBestPosition();
            bestEval = particle.getBestEval();
        }
    }

    private void updateRadius(Particle[] set, ArrayList<Double> setR){
        for (Particle p :set){
            setR.clear();
            Double x = Math.pow(p.getPosition().getX()-bestPosition.getX(),2);
            Double y = Math.pow(p.getPosition().getY()-bestPosition.getY(),2);
            Double R = Math.sqrt(x+y);
            setR.add(R);
        }
    }

    /**
     * Update the velocity of a particle using the velocity update formula
     * @param particle  the particle to update
     */
    private void updateVelocity (Particle particle) {
        Matrix oldVelocity = particle.getVelocity();
        Matrix pBest = particle.getBestPosition();
        Matrix gBest = bestPosition.clone();
        Matrix pos = particle.getPosition();

        Random random = new Random();
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();

        // The first product of the formula.
        Matrix newVelocity = oldVelocity.clone();
        newVelocity.mul(inertia);

        // The second product of the formula.
        pBest.sub(pos);
        pBest.mul(cognitiveComponent);
        pBest.mul(r1);
        newVelocity.add(pBest);

        // The third product of the formula.
        gBest.sub(pos);
        gBest.mul(socialComponent);
        gBest.mul(r2);
        newVelocity.add(gBest);

        particle.setVelocity(newVelocity);
    }

}
