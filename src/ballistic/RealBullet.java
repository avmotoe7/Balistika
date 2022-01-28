package ballistic;

import java.util.function.Function;
import static java.lang.Math.sqrt;

/**
 * Konstrukcija parametara projektila iz rezultata 
 * merenja brzine u zavisnosti od rastojanja
 * 
 * Apkrosimacija brzine je izvrsena polinomom drugog stepena
 * P(x) = v0 + k1x + k2x^2
 * 
 */
public class RealBullet implements Bullet {

    private final Function<Double, Double> v;
    private final Function<Double, Double> dv_po_dx;
    private final double correction;
    
    /**
     * 
     * @param vo    pocetna brzina
     * @param k1    linearni clan
     * @param k2    kvadratni clan
     * @param Po    referentni pritisak u KPa
     * @param To    referentna temperatura u K
     * @param P     pritisak u KPa
     * @param T     temperatura u K
     */
    public RealBullet  (double vo, double k1, double k2, double Po, double To, double P, double T) {
        // aproksimacija krive brzine u zavisnosti od puta
        // aproksimacija izvoda brzine po putu
        v = x -> vo + k1 * x + k2 * x * x;
        dv_po_dx = x -> k1 + 2 * k2 * x;
        
        // korekcija za razliku od referentnih uslova atmosfere
        // provrtiti!!
        correction = (P / Po) * sqrt((To + 273.15) / (T + 273.15));
    }
    
    /**
     *  Izracunavanje parametra K iz referentne   
     *  brzine metka u zavisnosti od rastojanja
     * 
     * @param velosity  brzina projektila
     * @return          vrednost K u 
     */
    @Override
    public double getK(double velosity) {
        // Koristi Njutnovu metodu za nalazenje nula funkcije
        double e = 1e-3;
        double x = 0;
        double dv = v.apply(x) - velosity;

        while (dv > e) {
            x = x - dv / dv_po_dx.apply(x);
            dv = v.apply(x) - velosity;
        }
        
        return -correction * dv_po_dx.apply(x) / velosity;
    }
}
