/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ballistic;

import ballistic.Balistic.StandardProjectile;

/**
 *
 * @author vidak
 */
public class StandardBullet implements Bullet {

    private final StandardProjectile standardProjectile;
    private double BC;
    private final double k;
    private final double c; // brzina zvuka

    //private double m;
    //private double cal;
    public StandardBullet(StandardProjectile sp, double balisticCoefficient, double P, double T) {

        this.standardProjectile = StandardProjectile.G1;
        this.BC = balisticCoefficient * 703.06958; // konverzija lib/inch^2 -> kg/m^2

        k = (Math.PI / 8) * Balistic.ro(P, T);
        c = Balistic.speedOfSound(T);
    }

    /**
     *
     * @param velosity brzina za koju se izracunava K
     * @return
     */
    @Override
    public double getK(double velosity) {

        double K = 0;
        double mah = velosity / c;

        switch (standardProjectile) {

            case G1:
                K = k * Balistic.g1(mah) / BC;
                break;

            case G7:
                K = k * Balistic.g7(mah) / BC;
                break;
        }

        return K;
    }

}
