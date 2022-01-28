package ballistic;

import java.util.function.BiFunction;
import java.util.function.Function;

import static java.lang.Math.PI;

public abstract class Bullet_old {

    private final Function<Double, Double> Cd;
    protected final double BC;

    /**
     * Konstrukcija objekta ako je poznata funkcija
     * @param cal calibar u milimetrima
     * @param m   masa u gramima
     * @param f  funkcija koeficijenta otpora u zavisnosti od mahovog brojai
     */
    public Bullet_old(double cal, double m, Function<Double, Double> f) {

        this.Cd = f;

        cal = cal / 1000;      // konverzija u metre
        m = m / 1000;          // konverzija u kilograme

        double A = PI * cal * cal / 4;  // poprecna povrsina projektila
        this.BC = m / A;                // balisticki koeficijent za koeficijent otpora Cd=1
    }

    public double getBC(double v) {
        // BC = m/(A*Cd)
        return BC / Cd.apply(v);
    }

    public abstract double getK(double v);
        //return 0.5*ro/getBC(v);
    
}
