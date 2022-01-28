package ballistic;

/**
 * standardna atmsfera:
 * temperatura     15 C
 * pritisak        100 KPa
 */
public class PPU_7_62_mm_FMJ extends RealBullet {
    
    /**
     *  Konstrukcija projektila na osnovu prametara sredine
     * 
     * @param P     atmosferski pritisak sredine
     * @param T     temperature sredine
     */
    public PPU_7_62_mm_FMJ(double P,double T) {
        
        // referentni uslovi pucanja 15 C i 101.325 kPa
        // aproksimaija brzine u zavisnosti od razdaljine polinomom drugog stepena
        super(720.0,-0.899628250389242,0.000450126432656117,Balistic.P0,Balistic.T0,P,T);
    }
}
