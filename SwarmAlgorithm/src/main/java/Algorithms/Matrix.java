package Algorithms;

import java.io.Serializable;

/**
 * Can represent a position as well as a velocity.
 */
class Matrix implements Serializable {

    public double x, y, z;
    public double limit = Double.MAX_VALUE;

    Matrix () {
        this(0, 0, 0);
    }

    Matrix (double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX () {
        return x;
    }

    public double getY () {
        return y;
    }

    public double getZ () {
        return z;
    }

    void set (double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    private void setX (double x) {
        this.x = x;
    }

    private void setY (double y) {
        this.y = y;
    }

    private void setZ (double z) {
        this.z = z;
    }

    void add (Matrix v) {
        x += v.x;
        y += v.y;
        z += v.z;
        limit();
    }

    void sub (Matrix v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        limit();
    }

    void mul (double s) {
        x *= s;
        y *= s;
        z *= s;
        limit();
    }

    void div (double s) {
        x /= s;
        y /= s;
        z /= s;
        limit();
    }

    void normalize () {
        double m = mag();
        if (m > 0) {
            x /= m;
            y /= m;
            z /= m;
        }
    }

    private double mag () {
        return Math.sqrt(x*x + y*y);
    }

    void limit (double l) {
        limit = l;
        limit();
    }

    private void limit () {
        double m = mag();
        if (m > limit) {
            double ratio = m / limit;
            x /= ratio;
            y /= ratio;
        }
    }

    public Matrix clone () {
        return new Matrix(x, y, z);
    }

    public String toString () {
        return "(" + x + ", " + y + ", " + z + ")";
    }

}
