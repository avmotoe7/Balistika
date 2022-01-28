package ballistic;

//import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class RungeKutta {
    private final BiFunction<Double, Double, Double> f;


    public RungeKutta(BiFunction<Double, Double, Double> f) {
        this.f = f;
    }


    public double rk4(double t, double y, double h) {
        double k1 = f.apply(t, y);
        double k2 = f.apply(t + h / 2, y + h * k1 / 2);
        double k3 = f.apply(t + h / 2, y + h * k2 / 2);
        double k4 = f.apply(t + h, y + h * k3);

        return y + h * (k1 + 2 * k2 + 2 * k3 + k4) / 6;
    }

    public double simpson(double y1, double y2, double y3, double h) {
        return h * (y1 + 4 * y2 + y3) / 3;
    }

    /**
     * Kosijev problem y'=f(t,y) (diferencijalna forma)
     * racuna se vredost u tacki y(t+h)
     *
     * @param t pocetni trenutak
     * @param h prirast tacke t
     * @param y pocetna vrednost
     * @param f funkcija f(t,y)
     * @return vrednost u y(t+h)
     */
    public static double rk4(double t, double h, double y, BiFunction<Double, Double, Double> f) {
        double k1 = f.apply(t, y);
        double k2 = f.apply(t + h / 2, y + h * k1 / 2);
        double k3 = f.apply(t + h / 2, y + h * k2 / 2);
        double k4 = f.apply(t + h, y + h * k3);

        return y + h * (k1 + 2 * k2 + 2 * k3 + k4) / 6;
    }

    /**
     * Kosijev problem y'=f(t,y)
     * racuna vrednost y na intervali [t0, t_end] u  n tacaka
     *
     * @param t0    pocetak intervala
     * @param y0    pocetna vrednost u t0
     * @param t_end kraj intervala
     * @param n     broj tacaka u intervalu
     * @param f     funkcija f(t,y)
     * @return niz vrednosti y u n tacaka intervala [t0, t_end]
     */
    public static double[] integrate(double t0, double y0, double t_end, int n, BiFunction<Double, Double, Double> f) {
        double[] ret = new double[n];
        double h = (t_end - t0) / (n - 1);

        double y = y0;
        double t = t0;

        for (int i = 0; i < n; i++) {
            ret[i] = y;
            y = rk4(t, h, y, f);
            t += h;
        }
        return ret;
    }

    /**
     * Integracija niza Simpsonovom metodom
     *
     * @param y ulazni integracioni niy, pozeljno je da broj
     *          clanova niza bude neparan veci ili jednak 3
     * @param h korak diskretiyacije
     * @return integral niza sipsonovom metodom
     */
    public static double simpson(double[] y, double h) {
        int n = y.length;
        double sum = 0;
        for (int i = 0; i < n - 2; i += 2) {
            sum += h * (y[i] + 4 * y[i + 1] + y[i + 2]) / 3;
        }
        return sum;
    }

    public static <T, F extends BiFunction<? extends Double, ? extends Double, ? extends Double>> T test(T t, F f) {
        return t;
    }

}
