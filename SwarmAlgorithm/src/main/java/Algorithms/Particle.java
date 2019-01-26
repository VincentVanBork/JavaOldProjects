package Algorithms;

import java.io.Serializable;
import java.util.Random;

/**
 * Represents a particle from the Particle Swarm Optimization algorithm.
 */
public class Particle implements Serializable {

    public Matrix position;        // Current position.
    private Matrix velocity;
    private Matrix bestPosition;    // Personal best solution.
    private double bestEval;        // Personal best value.
    private type function;  // The evaluation function to use.

    /**
     * Construct a Particle with a random starting position.
     * @param beginRange    the minimum xyz values of the position (inclusive)
     * @param endRange      the maximum xyz values of the position (exclusive)
     */
    Particle (type function, int beginRange, int endRange) {
        if (beginRange >= endRange) {
            throw new IllegalArgumentException("Begin range must be less than end range.");
        }
        this.function = function;
        position = new Matrix();
        velocity = new Matrix();
        setRandomPosition(beginRange, endRange);
        bestPosition = velocity.clone();
        bestEval = eval();
    }

    /**
     * The evaluation of the current position.
     * @return      the evaluation
     */
    private double eval () {
        if (function == type.RosenBrock) {
            return Function.RosenBrock(position.getX(),position.getY());
        } else if (function == type.Ackleys) {
            return Function.ackleysFunction(position.getX(), position.getY());
        } else if (function == type.Booths) {
            return Function.boothsFunction(position.getX(), position.getY());
        } else {
            return Function.XsqerYsqer(position.getX(), position.getY());
        }
    }

    private void setRandomPosition (int beginRange, int endRange) {
        int x = rand(beginRange, endRange);
        int y = rand(beginRange, endRange);
        int z = rand(beginRange, endRange);
        position.set(x, y, z);
    }

    /**
     * Generate a random number between a certain range.
     * @param beginRange    the minimum value (inclusive)
     * @param endRange      the maximum value (exclusive)
     * @return              the randomly generated value
     */
    private static int rand (int beginRange, int endRange) {
        Random r = new Random();
        return r.nextInt(endRange - beginRange) + beginRange;
    }

    /**
     * Update the personal best if the current evaluation is better.
     */
    void updatePersonalBest () {
        double eval = eval();
        if (eval < bestEval) {
            bestPosition = position.clone();
            bestEval = eval;
        }
    }

    /**
     * Get a copy of the position of the particle.
     * @return  the x position
     */
    public Matrix getPosition() {
        return position.clone();
    }

    /**
     * Get a copy of the velocity of the particle.
     * @return  the velocity
     */
    Matrix getVelocity () {
        return velocity.clone();
    }

    /**
     * Get a copy of the personal best solution.
     * @return  the best position
     */
    Matrix getBestPosition() {
        return bestPosition.clone();
    }

    /**
     * Get the value of the personal best solution.
     * @return  the evaluation
     */
    double getBestEval () {
        return bestEval;
    }

    /**
     * Update the position of a particle by adding its velocity to its position.
     */
    void updatePosition () {
        this.position.add(velocity);
    }

    /**
     * Set the velocity of the particle.
     * @param velocity  the new velocity
     */
    void setVelocity (Matrix velocity) {
        this.velocity = velocity.clone();
    }

    public enum type {
        RosenBrock,
        Ackleys,
        Booths,
        XsqerYsqer
    }

}
