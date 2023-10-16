package pruebas;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import neoAtlantis.utils.captcha.PointLineCaptcha;
import neoAtlantis.utils.captcha.StrokeCaptcha;
import neoAtlantis.utils.captcha.interfaces.CaptchaPainter;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class PruebaCaptcha extends JPanel {
    
    public PruebaCaptcha() {
        
        {
            this.setBackground(new java.awt.Color(240, 255, 255));
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        CaptchaPainter cap=new StrokeCaptcha();        
        Image image=cap.paint("CaptCha");

        if(image != null){
            g.drawImage(image, 0, 0, this);
        }
    }

    public static void main(String[] args) {
        PruebaCaptcha panel = new PruebaCaptcha();
        
        JFrame frm=new JFrame("Captcha Demo");
        frm.add(panel, BorderLayout.CENTER);
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //setBounds(0, 0, screenSize.width-30, screenSize.height-30);
        frm.setSize(new Dimension(800, 400));
        frm.setVisible(true);
    }
}
