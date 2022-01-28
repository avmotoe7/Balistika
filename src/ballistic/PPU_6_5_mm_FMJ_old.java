package ballistic;

public class PPU_6_5_mm_FMJ_old extends Bullet_old {

    public PPU_6_5_mm_FMJ_old() {
        super(6.5, 7.8, PPU_6_5_mm_FMJ_old::Cd);
    }

    /**
     * Koeficijent otpora vazduha u zavisnosti od mahovog broja
     * Lineariyovani G7 model
     *
     * @param MaxNumber Mahov broj
     * @return
     */
    public static double Cd(double MaxNumber) {

        double y0 = 0.1198;
        double x1 = 0.9;
        double y1 = 0.1464;
        double x2 = 1.05;
        double x3 = 3;
        double y2 = 0.425;
        double y3 = 0.30;

        if (MaxNumber >= x2)
            return (MaxNumber - x2) * (y3 - y2) / (x3 - x2) + y2;

        if (MaxNumber < x2 && MaxNumber > x1)
            return (MaxNumber - x1) * (y2 - y1) / (x2 - x1) + y1;

        return MaxNumber * (y1 - y0) / x1 + y0;
    }

    /**
     * Funkcija vraca koeficijenat K (dv/dt = -Kv^2)
     * estimaija K je izvrsena na osnovu podataka iz
     * balistickih tablica
     *
     * @param v brzina projektila
     * @param x udaljenost od usta cevi
     * @return koeficijenat K
     */
    @Override
    public double getK(double v) {
        //double dv_po_dx = 0.00052 * x - 0.74652;
        //double 
        return 1 / v;
    }


}
