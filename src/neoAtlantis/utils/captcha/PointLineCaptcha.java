package neoAtlantis.utils.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Random;
import neoAtlantis.utils.captcha.interfaces.CaptchaPainter;

/**
 * Dibujador de Captchas que realiza la imegen con lineas y puntos.
 * @author Hiryu (aslhiryu@gmail.com)
 * @version 1.0
 */
public class PointLineCaptcha implements CaptchaPainter {
    private float[] rads=new float[]{0, 0.45f, -0.45f, 0.63f, -0.63f};

    /**
     * Pinta el captcha.
     * @param cadena Cadena con la que se desea se genere el captcha
     * @return Imagen con el captcha
     */
    @Override
    public Image paint(String cadena) {
        char[] letras=cadena.toCharArray();
        AffineTransform trans;
        Random r=new Random(Calendar.getInstance().getTimeInMillis());

        //define la fuente a utilizar
        Font f=new Font("SansSerif", Font.BOLD, 80);

        //calculo el espacio por letra
        Rectangle2D rec=f.getMaxCharBounds(new FontRenderContext(new AffineTransform(), true, true));
        int ancho=(int)((letras.length*2+1)*(rec.getWidth()/3));
        int alto=(int)(rec.getHeight()+(rec.getHeight()/2));

        //defino la imagen
        BufferedImage image=new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics g=image.createGraphics();

        //dibujo el fondo
        g.setColor(new Color(255, 255, 176));
        g.fillRect(0, 0, ancho, alto);

        //dibuja unas lineas
        g.setColor(new Color(80, 80, 80));
        for(int i=0; i<(letras.length*20); i++){
            g.drawLine(r.nextInt(ancho), r.nextInt(alto), r.nextInt(ancho), r.nextInt(alto));
        }

        //dibujo las letras
        g.setColor(Color.BLACK);
        for(int i=0; i<letras.length; i++){
            trans=new AffineTransform();
            trans.setToRotation(this.rads[ Math.abs(r.nextInt()%this.rads.length) ]);
            g.setFont(f.deriveFont(trans));

            g.drawString(""+letras[i], (int)(ancho/(letras.length+2)*(i+1)), (int)rec.getHeight());
        }

        //finalizo la imagen
        g.dispose();

        return image;
    }
    
}
