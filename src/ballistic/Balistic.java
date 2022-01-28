/**
 * Balisticke konstante
 */
package ballistic;

import static java.lang.Math.sqrt;

/**
 *
 * @author vidak
 */
public class Balistic {

    // gasne konstante
    public static final double R = 8.314472; // (J/mol K) - gasna konstanta
    public static final double M = 28.9656;  // (g/mol) - molarna masa vazduha

    // The International Standard Metric Conditions 
    // for natural gas and similar fluids
    public static final double T0 = 15;      // Celzijusa
    public static final double P0 = 101.325; // KPa  

    public enum StandardProjectile {
        G1, G7
    };
    
    // G7 drag model data
    private static final double g7x[]
            = {0.60, 0.65, 0.70, 0.725, 0.75, 0.775, 0.80, 0.825, 0.85, 0.875,
                0.90, 0.925, 0.95, 0.975, 1.0, 1.025, 1.05, 1.075, 1.10, 1.125,
                1.15, 1.20, 1.25, 1.30, 1.35, 1.40, 1.50, 1.55, 1.60, 1.65,
                1.70, 1.75, 1.80, 1.85, 1.90, 1.95, 2.00, 2.05, 2.10, 2.15,
                2.20, 2.25, 2.30, 2.35, 2.40, 2.45, 2.50, 2.55, 2.60, 2.65,
                2.70, 2.75, 2.80, 2.85, 2.90, 2.95, 3.00, 3.10, 3.20, 3.30};

    private static final double g7y[]
            = {0.1194, 0.1197, 0.1202, 0.1207, 0.1215, 0.1226, 0.1242, 0.1266, 0.1306, 0.1368,
                0.1464, 0.1660, 0.2054, 0.2993, 0.3803, 0.4015, 0.4043, 0.4034, 0.4014, 0.3987,
                0.3955, 0.3884, 0.3810, 0.3732, 0.3657, 0.3580, 0.3440, 0.3376, 0.3315, 0.3260,
                0.3209, 0.3160, 0.3117, 0.3078, 0.3042, 0.3010, 0.2980, 0.2951, 0.2922, 0.2892,
                0.2864, 0.2835, 0.2807, 0.2779, 0.2752, 0.2725, 0.2697, 0.2670, 0.2643, 0.2615,
                0.2588, 0.2561, 0.2533, 0.2506, 0.2479, 0.2451, 0.2424, 0.2368, 0.2313, 0.2258};

    // G1 drag model data
    private static final double g1x[]
            = {0.60, 0.70, 0.725, 0.75, 0.775, 0.80, 0.825, 0.85, 0.875, 0.90,
                0.925, 0.95, 0.975, 1.00, 1.025, 1.05, 1.075, 1.10, 1.125, 1.15,
                1.20, 1.25, 1.30, 1.35, 1.40, 1.45, 1.50, 1.55, 1.60, 1.65,
                1.70, 1.75, 1.80, 1.85, 1.90, 1.95, 2.00, 2.05, 2.10, 2.15,
                2.20, 2.25, 2.30, 2.35, 2.40, 2.45, 2.50, 2.60, 2.70, 2.80,
                2.90, 3.00, 3.10, 3.20, 3.30};

    private static final double g1y[]
            = {0.2034, 0.2165, 0.2230, 0.2313, 0.2417, 0.2546, 0.2706, 0.2901, 0.3136, 0.3415,
                0.3734, 0.4084, 0.4448, 0.4805, 0.5136, 0.5427, 0.5677, 0.5883, 0.6053, 0.6191,
                0.6393, 0.6518, 0.6589, 0.6621, 0.6625, 0.6607, 0.6573, 0.6528, 0.6474, 0.6413,
                0.6347, 0.6280, 0.6210, 0.6141, 0.6072, 0.6003, 0.5934, 0.5867, 0.5804, 0.5743,
                0.5685, 0.5630, 0.5577, 0.5527, 0.5481, 0.5438, 0.5397, 0.5325, 0.5264, 0.5211,
                0.5168, 0.5133, 0.5105, 0.5084, 0.5067};

    // Lagranzov interpolacioni polinom
    private static double lagrange(double x, double x1, double x2, double x3, double y1, double y2, double y3) {

        double l1 = (x - x2) * (x - x3) / ((x1 - x2) * (x1 - x3));
        double l2 = (x - x1) * (x - x3) / ((x2 - x1) * (x2 - x3));
        double l3 = (x - x1) * (x - x2) / ((x3 - x1) * (x3 - x2));

        return y1 * l1 + y2 * l2 + y3 * l3;
    }

    /**
     * Aproksimaija tablicnih podataka G7 modela Lagranzovim polinomom drugog
     * stepena
     *
     * @param mah mahov broj
     * @return Drag coefficient
     */
    public static double g7(double mah) {

        if (mah < 0.65) {   // donja granica
            return 0.1194;
        }

        if (mah >= 3.2) {   // gornja granica 
            return 0.2258;
        }

        int index = 0;
        while (g7x[index] < mah) {
            index++;
        }

        double L = lagrange(
                mah,
                g7x[index - 1], g7x[index], g7x[index + 1],
                g7y[index - 1], g7y[index], g7y[index + 1]
        );

        return L;
    }

    //
    public static double g1(double mah) {

        if (mah < 0.7) {   // donja granica
            return 0.2034;
        }

        if (mah >= 3.2) {   // gornja granica 
            return 0.5067;
        }

        int index = 0;
        while (g1x[index] < mah) {
            index++;
        }

        double L = lagrange(
                mah,
                g1x[index - 1], g1x[index], g1x[index + 1],
                g1y[index - 1], g1y[index], g1y[index + 1]
        );

        return L;
    }

    /**
     * Gustina vazduha
     *
     * @param P pritisak u KPa
     * @param T temperatura u C
     * @return gustina vazduha u kg/m3
     */
    public static double ro(double P, double T) {
        return P * M / (R * (273.15 + T));
    }

    public static double speedOfSound(double T) {
        return 20.045 * sqrt(273.15 + T);
    }

    // test
    public static void main(String[] args) {

        double mah = 2.9;
        double dc = g1(mah);
        System.out.printf("%6.3f  %6.4f\n", mah, dc);
        System.out.printf("%6.4f\n", ro(P0, T0));
        System.out.printf("%6.4f\n", speedOfSound(T0));
    }
}
