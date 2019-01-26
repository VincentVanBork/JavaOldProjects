package DifferentialEvolution;

import java.io.Serializable;

public class Values implements Serializable {
    int function ;
    int n;
    double F ;
    double CR;
    int space;
    double STOP;

    public Values(int function,int n,double F,double CR,int space,double STOP){
        this.function = function;
        this.n = n;
        this.F = F;
        this.CR = CR;
        this.space = space;
        this.STOP = STOP;
    }

    public double getCR() {
        return CR;
    }

    public double getF() {
        return F;
    }

    public int getFunction() {
        return function;
    }

    public int getN() {
        return n;
    }

    public int getSpace() {
        return space;
    }

    public void setCR(double CR) {
        this.CR = CR;
    }

    public void setF(double f) {
        F = f;
    }

    public void setFunction(int function) {
        this.function = function;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public double getSTOP() {
        return STOP;
    }

    public void setSTOP(double STOP) {
        this.STOP = STOP;
    }
}
