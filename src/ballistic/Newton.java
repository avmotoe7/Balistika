package ballistic;

import java.util.function.Function;
import java.lang.Math;
import static java.lang.Math.cos;
import static java.lang.Math.exp;
import static java.lang.Math.sin;
import java.util.function.BiFunction;

public class Newton {

    private static final Function<Double, Double> f = (x) -> x * (x - 1) * (x - 3);

    public static double zero(double x0, double h, double e, Function<Double, Double> f) {
        double x_old;
        double x_new;
        double err;

        x_old = x0;
        do {
            x_new = x_old - h * f.apply(x_old) / (f.apply(x_old + h) - f.apply(x_old));
            err = Math.abs(x_new - x_old);
            x_old = x_new;
        } while (err > e);

        return x_new;
    }

    private static final Function<Double, Double> F = (x) -> (x - 100) * (x - 100) * (x - 150) * (x - 150) + 10;

    public static double gradiendDescent(double x0, double h, double e, Function<Double, Double> f) {

        double x_old;
        double x_new;
        double err;
        double stepSize;
        double fx;      // prvi izvod
        double fxx;     // drugi izvod

        x_old = x0;

        do {
            fx = (f.apply(x_old + h) - f.apply(x_old)) / h;
            fxx = (f.apply(x_old + h) - 2 * f.apply(x_old) + f.apply(x_old - h)) / (h * h);

            if (fxx > 0) {
                stepSize = 1 / fxx;  //Njutnova metoda

            } else {

                stepSize = 0.5;     //opadajuci gradijent
            }

            x_new = x_old - stepSize * fx;

            while (f.apply(x_old) - f.apply(x_new) < 0) { // prebacaj
                stepSize /= 2;
                x_new = x_old - stepSize * fx;
            }

            err = f.apply(x_old) - f.apply(x_new);
            x_old = x_new;

        } while (err > e);

        return x_new;
    }

    private static final BiFunction<Double, Double, Double> G = (x, y) ->(x-3)*(x-3)+(y-15)*(y-15);

    //private static final BiFunction<Double, Double, Double> G = (x, y) -> sin(x*x/2-y*y/4)*cos(2*x+1-exp(y));
    public static double[] gradiendDescent2D(double x0, double y0, double h, double e, BiFunction<Double, Double, Double> f) {

        double x_old = x0;
        double y_old = y0;
        double a, b;
        double x_new, y_new;
        double fx, fy, fxx, fyy, fxy;
        double err;
        do {
            fx = (f.apply(x_old + h, y_old) - f.apply(x_old - h, y_old)) / (2 * h);
            fy = (f.apply(x_old, y_old + h) - f.apply(x_old, y_old - h)) / (2 * h);
            fxx = (f.apply(x_old + h, y_old) - 2 * f.apply(x_old, y_old) + f.apply(x_old - h, y_old)) / (h * h);
            fyy = (f.apply(x_old, y_old + h) - 2 * f.apply(x_old, y_old) + f.apply(x_old, y_old - h)) / (h * h);
            fxy = (f.apply(x_old + h, y_old + h) - f.apply(x_old + h, y_old) - f.apply(x_old, y_old + h) + 2 * f.apply(x_old, y_old)
                    - f.apply(x_old - h, y_old) - f.apply(x_old, y_old - h) + f.apply(x_old - h, y_old - h)) / (4 * h * h);

            a = fxx * x_old + fxy * y_old - fx;
            b = fyy * y_old + fxy * x_old - fy;

            y_new = (a * fxy - b * fxx) / (fxy * fxy - fxx * fyy);
            x_new = (a - fxy * y_new) / fxx;

            err = f.apply(x_old, y_old) - f.apply(x_new, y_new);

            x_old = x_new;
            y_old = y_new;

        } while (err > e);

        double ret[] = {x_new, y_new};
        System.out.printf("%10.8f \t %10.8f\n", ret[0], ret[1]);
        return ret;
    }

    public static void main(String[] args) {

        System.out.println(Newton.zero(0.6, 1e-3, 1e-5, f));
        System.out.println(Newton.zero(3.6, 1e-5, 1e-2, (x) -> Math.sin(x)));
        System.out.println(Math.PI);

        System.out.println("=================================");
        System.out.println(Newton.gradiendDescent(126, 1e-4, 1e-5, F));

        double min[] = Newton.gradiendDescent2D(500, 100, 1e-4, 1e-5, G);
        System.out.printf("%10.8f \t %10.8f\n", min[0], min[1]);

    }
}
