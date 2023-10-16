/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebas;

import neoAtlantis.utils.data.DataUtils;

/**
 *
 * @author desarrollo.alberto
 */
public class PruebaDataUtils {
    public static void main(String[] args) {
        //System.out.println("=> "+DataUtils.validateEMail("algo.algo@algo.co.mx"));
        //System.out.println("=> "+DataUtils.validateRFC("SALA780903KL9"));
        System.out.println("=> "+DataUtils.validateIP("255.199.99.001"));
    }
}
