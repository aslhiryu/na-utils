package neoAtlantis.utils.codeBars;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import neoAtlantis.utils.codeBars.exceptions.CodeBarException;

/**
 * Clase utilitaria que permite la generación de codigos QR
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class QrUtils {
    public static InputStream createBasicQR(int size, String cadena) throws CodeBarException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        try{
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(cadena, BarcodeFormat.QR_CODE, size, size, hintMap);

            BufferedImage image = new BufferedImage(byteMatrix.getWidth(), byteMatrix.getWidth(), BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, byteMatrix.getWidth(), byteMatrix.getWidth());
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < byteMatrix.getWidth(); i++) {
                for (int j = 0; j < byteMatrix.getWidth(); j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            ImageIO.write(image, "png", os);

            return new ByteArrayInputStream(os.toByteArray());
        }
        catch(Exception ex){
            throw new CodeBarException(ex);
        }
    }
    
}
