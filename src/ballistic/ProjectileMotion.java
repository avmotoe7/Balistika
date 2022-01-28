package ballistic;

import java.util.function.BiFunction;

import static java.lang.Math.*;

public class ProjectileMotion {

    private double h = 0;     // visina u odnosu na cilj u metrima
    private double v0;        // pocetna brzina

    private double theta;           // elevacija u MOA
    private double rx, ry;          // x i y komponenta polozaja
    private double vx, vy;          // x i y komponenta brzine

    // K = 0.5*(gustina vazduha)*(povrsina)*(koeficijent otpora)/(masa projektila)
    // koeficijenat otpora (Drag coefficient) je funkcija brzine, za standardne 
    // projektile od G1 do G7 postoje tablicne vrednosti.
    
    protected double K;

    private final double g = 9.81;  // gravitaciona konstanta

    // Kosijev problem oblika vx'= fx(vx,v) i vy'= fy(vy,v)
    // definisanje funkcija za x i y komponentu kretanja
    private final BiFunction<Double, Double, Double> fx = (Double q, Double w) -> -K * sqrt(q * q + w * w) * q;
    private final BiFunction<Double, Double, Double> fy = (Double q, Double w) -> -g - K * sqrt(q * q + w * w) * q;

    /**
     * Postavljanje pocetnih parametara kosog hica:
     * pocetna brzina, elevacija i visina u odnosu na cilj
     *
     * @param v0    pocetna brzina
     * @param theta elevacija u MOA, krug se deli na 360*60 MOA (minuta)
     * @param h     visina u odnosu na cilj ( '+' vise od cilja, '-' nize od cilja)
     * @param K     
     */
    public ProjectileMotion (double v0, double theta, double h, double K) {
        // K = 8.7956143813946E-4; // 6.5mm m=8g Cd=0.349 v0=805 m/s
        // K = 12.087859684900555E-4; // 7.62mm m=8g Cd=0.349 v0=720 m/s
        this.K = K;
        reset(v0, theta, h);
    }

    /**
     * Zasticeni konstruktor. Poziva se bez K koji se azurira
     * pre racunjana pozivom metodom updateK()
     *
     * @param v0    pocetna brzina
     * @param theta elevacija
     * @param h     visina u odnosu na cilj
     */
    protected ProjectileMotion(double v0, double theta, double h) {
        reset(v0, theta, h);
    }


    // postavljanje pocetnih parametara
    public void reset(double v0, double theta, double h) {
        this.theta = theta / 60; // ugao je u MOA, konverzija u stepene
        this.v0 = v0;
        this.h = h;

        rx = 0;
        ry = h;
        vx = v0 * cos(PI * this.theta / 180);
        vy = v0 * sin(PI * this.theta / 180);

    }

    /**
     * izracunaj novu vrednost koeficijenta K u zavisnosti od brzine i visine
     * poziva se na pocetku metoda next()
     * funkcija je prekolopljena u izvedeneim klsama
     */
    protected void updateK() {

    }

    // trenutna brzina
    public double getV() {
        return sqrt(vx * vx + vy * vy);
    }

    // trenutna visina
    public double getRY() {
        return ry;
    }

    // udaljenost od usta cevi
    public double getRX() {
        return rx;
    }

    // trenutni pad u odnosu na liniju lansiranja
    public double getDrop() {
        return rx * tan(PI * theta / 180) + h - ry;
    }

    // runge-kutta
    private double rk4(double q, double w, double h, BiFunction<Double, Double, Double> f) {
        double k1 = f.apply(q, w);
        double k2 = f.apply(q + h * k1 / 2, w);
        double k3 = f.apply(q + h * k2 / 2, w);
        double k4 = f.apply(q + h * k3, w);
        return q + h * (k1 + 2 * k2 + 2 * k3 + k4) / 6;
    }

    /**
     * Centralna funkcija klase. Racunanje polozaja tela i brzine
     * u trenutku t+dt. Reyultati trenutnog poloyaja i bryine se
     * cuvaju u privatnim clanovima klase.
     *
     * @param dt vremenski pomeraj
     * @return horizontalna udaljenost od mesta lansiranja
     */
    public double next(double dt) {

        updateK();  // izracunaj novu vrednost koeficijentau zavisnosti od brzine i visene

        double delta = dt / 2;

        double vx1 = rk4(vx, vy, delta, fx);
        double vy1 = rk4(vy, vx, delta, fy);

        double vx2 = rk4(vx1, vy1, delta, fx);
        double vy2 = rk4(vy1, vx1, delta, fy);

        // Simpson's rule
        rx += delta * (vx + 4 * vx1 + vx2) / 3;
        ry += delta * (vy + 4 * vy1 + vy2) / 3;

        vx = vx2;
        vy = vy2;

        return rx;  // horizontalna daljina
    }

    // za zadato rastojanje i elevaciju vraca
    // vertikalno odstupanje od centra mete
    public double dry(double distance, double theta) {
        reset(this.v0, theta, this.h);
        double dt = 1e-3;// 1 ms
        double d = 0;
        while (d <= distance) {
            d = next(dt);
        }
        return ry;
    }

    /**
     * Odredjivanje elevacije cevi za cilj na zadatom rastojanju
     * Koristi se Njutnova metoda za odredjivanje nula funkcije
     *
     * @param h        visina u odnosu na cilj
     * @param distance rastojanje do cilja
     * @return elevacija
     */
    public double getTheta(double h, double distance) {
        double theta_old;
        double theta_new;
        final double dtheta = 1.0 / (60 * 2000);    // prirast 1000 deo MOA
        double err;                                 // greska proracuna

        //this.h = h; // visina u odnosu na cilj nije potrebno jer se postavlja u getThetaSimple

        theta_old = getThetaSimple(h, distance);
        double r_od_theta, r_od_theta_plus_dtheta;

        do {
            r_od_theta = dry(distance, theta_old);
            r_od_theta_plus_dtheta = dry(distance, theta_old + dtheta);
            theta_new = theta_old - dtheta * r_od_theta / (r_od_theta_plus_dtheta - r_od_theta);
            err = Math.abs(theta_new - theta_old);
            theta_old = theta_new;
        }
        while (err > dtheta);

        return theta_new; // rezultat u MOA
    }

    /**
     * Odredjivanje elevacije cevi za cilj na zadatom rastojanju
     * Koristi se priblizno racunanje ugla theta ~ distance * dy
     * gde je dy odstupanje od cilja posle "probnog pucanja" sa
     * elevacijom 0.
     *
     * @param h        visina u odnosu na cilj
     * @param distance rastojanje do cilja
     * @return elevacija
     */
    public double getThetaSimple(double h, double distance) {
        this.h = h;
        double dy = dry(distance, 0);
        return -(dy / distance) * 60 * 180 / PI;
    }
}
