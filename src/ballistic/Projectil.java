package ballistic;

import java.util.function.BiFunction;

import static java.lang.Math.sqrt;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Projectil {

    double t;
    double rx, ry;
    double vx, vy;

    double BC;
    double K;

    double k1, k2, k3, k4;

    final double g = 9.81;


    private final BiFunction<Double, Double, Double> fx = (Double q, Double w) -> -K * sqrt(q * q + w * w) * q;
    private final BiFunction<Double, Double, Double> fy = (Double w, Double q) -> -g - K * sqrt(q * q + w * w) * w;

    public Projectil(double v0, double theta, double ry) {
        t = 0;
        rx = 0;
        this.ry = ry;
        vx = v0 * cos(PI * theta / 60 / 180); //theta iz MOA -> rad
        vy = v0 * sin(PI * theta / 60 / 180);

        // Grendel 6.5x39 vo= 815 m/s
        setK(6.5e-3, 7.9e-3, 1.2, 0.349);

        // ppu 7.62x39 vo=
        setK(7.62e-3, 8.0e-3, 1.2, 0.349);
    }

    public void setK(double cal, double m, double ro, double Cd) {
        double A = PI * cal * cal / 4;
        BC = m / (A * Cd);
        //K = 0.5 * ro / BC;
        K= 0.5*ro*A*Cd/m;
    }

    // q je vx za fx i vy za fy
    private double rk4(double q, double w, double h, BiFunction<Double, Double, Double> f) {
        k1 = f.apply(q, w);
        k2 = f.apply(q + h * k1 / 2, w);
        k3 = f.apply(q + h * k2 / 2, w);
        k4 = f.apply(q + h * k3, w);

        return q + h * (k1 + 2 * k2 + 2 * k3 + k4) / 6;
    }

    public double next(double dt) {
        double h = dt / 2;

        double vx1 = rk4(vx, vy, h, fx);
        double vy1 = rk4(vy, vx, h, fy);

        double vx2 = rk4(vx1, vy1, h, fx);
        double vy2 = rk4(vy1, vx1, h, fy);

        // Simpson's rul
        rx += h * (vx + 4 * vx1 + vx2) / 3;
        ry += h * (vy + 4 * vy1 + vy2) / 3;

        vx = vx2;
        vy = vy2;

        t += dt;

        return rx;
    }

    public double getV() {
        return sqrt(vx * vx + vy * vy);
    }

    public double getRY() {
        return ry;
    }

}
