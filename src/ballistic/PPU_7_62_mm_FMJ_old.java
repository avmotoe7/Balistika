package ballistic;

import java.util.function.Function;

public class PPU_7_62_mm_FMJ_old extends Bullet_old {

    public PPU_7_62_mm_FMJ_old() {
        super(7.62, 8, PPU_7_62_mm_FMJ_old::Cd);
    }

    /**
     * Koeficijent otpora vazduha u zavisnosti od mahovog broja Lineariyovani G7
     * model
     *
     * @param MaxNumber Mahov broj
     * @return Drag koeficijent
     */
    public static double Cd(double MaxNumber) {

        double y0 = 0.1198;
        double x1 = 0.9;
        double y1 = 0.1464;
        double x2 = 1.05;
        double x3 = 3;
        double y2 = 0.5045;
        double y3 = 0.378;

        if (MaxNumber >= x2) {
            return (MaxNumber - x2) * (y3 - y2) / (x3 - x2) + y2;
        }

        if (MaxNumber < x2 && MaxNumber > x1) {
            return (MaxNumber - x1) * (y2 - y1) / (x2 - x1) + y1;
        }

        return MaxNumber * (y1 - y0) / x1 + y0;
    }

    /**
     * Funkcija vraca koeficijenat K (dv/dt = -Kv^2) estimaija K je izvrsena na
     * osnovu podataka iz balistickih tablica
     *
     * @param v brzina projektila
     * @param x udaljenost od usta cevi
     * @return koeficijenat K
     */
    @Override
    public double getK(double velocity) {
        Function<Double, Double> dv_po_dx = x -> 2 * 0.000458905 * x - 0.905826371;
        Function<Double, Double> v = x -> 720 - 0.905826371 * x + 0.000458905 * x * x;
        double e = 1e-3;
        double x = 0;
        
        double dy = v.apply(x)-velocity;
        while(dy>e){
            x = x - dy/dv_po_dx.apply(x);
            dy = v.apply(x)-velocity;
        }
        
        return    dv_po_dx.apply(x)/velocity;     
//double dv_po_dx = 2 * 0.000458904995145815 * x - 0.905826371431999;
        //return dv_po_dx / v;
    }
}
