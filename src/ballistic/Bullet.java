/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ballistic;

/**
 * Svaki projektil ima osobinu da vrati K
 * 
 * K = 0.5*(gustina vazduha)*(povrsina)*(koeficijent otpora)/(masa projektila)
 * koeficijenat otpora (Drag coefficient) je funkcija brzine, za standardne 
 * projektile od G1 do G7 postoje tablicne vrednosti.
 * 
 */
public interface Bullet {
        // vraca K
        public double getK(double velosity);     
}
