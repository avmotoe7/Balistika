package test;

import ballistic.*;
import ballistic.Balistic.StandardProjectile;

import java.util.function.BiFunction;

import static java.lang.Math.*;

public class Test {

    public static void main(String[] args) {
        System.out.println("Zdravo svete!");

        int n = 5;
        double t0 = 0;
        double y0 = 1;
        double t_end = 1 / sqrt(2);

        // test integracije
        // lambda funkcije
        BiFunction<Double, Double, Double> f = (Double t, Double y) -> -t / y;
        BiFunction<Double, Double, Double> u = (Double t, Double y) -> sqrt(1 - t * t);
        BiFunction<Double, Double, Double> r = (Double q, Double w) -> q * sqrt(q * q + w * w);

        double[] y = RungeKutta.integrate(t0, y0, t_end, n, f);

        double t = t0;
        double h = (t_end - t0) / (n - 1);
        double z;

        for (double yk : y) {
            z = u.apply(t, 0.0);
            System.out.printf("%10.5f \t %10.5f \t %10.5f \t %12.7f \n", t, yk, z, (yk - z));
            t += h;
        }
        System.out.printf("Povrsina > %f\n", 8 * (RungeKutta.simpson(y, h) - 0.25));
        System.out.println(PI);

        //======================================================================
        // test za balistiku
        double dt = 1.0e-3;
        double d = 0;
        double tl = 0;
        double D = 501;
        double paralaksa = -0.085;

        double pritisak = 100;//kPa
        double temperatura = 15;// C

        //RealBullet bullet = new PPU_7_62_mm_FMJ(pritisak,temperatura);
        StandardBullet bullet = new StandardBullet(StandardProjectile.G1, 0.27, Balistic.P0, Balistic.T0);
        //Bullet bullet = new PPU_6_5_mm_FMJ(pritisak,temperatura);
        BullitTrajectory p = new BullitTrajectory(693, 6.8+20+6, paralaksa, bullet);

        double theta = 0.0;

        while (d < D) {
            System.out.printf("%6.2f \t %6.3f \t %6.1f \t %4.1f \t %6.1f\n", d, p.getRY() * 100, p.getV(), p.getDrop() * 100, tl * 1000);
            d = p.next(dt);
            tl += dt;
        }

        double zeroTarget = 100;
        double zeroTheta = p.getTheta(paralaksa, zeroTarget);

        System.out.println(p.getThetaSimple(paralaksa, zeroTarget));
        System.out.println(zeroTheta);

        System.out.println("================================");

        System.out.println(zeroTheta);
        System.out.println(p.getTheta(paralaksa, 300) - zeroTheta);
        System.out.println(p.getTheta(paralaksa, 500) - zeroTheta);

    }
}
