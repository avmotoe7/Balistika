package ballistic;

public class BullitTrajectory extends ProjectileMotion {

    private final Bullet bullet;

    /**
     * Definisanje uslova pod kojima se vrsi proracun putanje
     *
     * @param v0     pocetna brzina projektila
     * @param theta  elevacija
     * @param h      visina u odnosu na cilj
     * @param bullet metak
     */
    public BullitTrajectory(double v0, double theta, double h, Bullet bullet) {
        super(v0, theta, h); // poziva update K
        this.bullet = bullet;
    }

    @Override
    protected void updateK() {
        this.K= bullet.getK(getV());
    }

}
