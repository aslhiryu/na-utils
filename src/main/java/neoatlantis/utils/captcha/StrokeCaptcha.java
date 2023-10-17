package neoatlantis.utils.captcha;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Random;
import neoatlantis.utils.captcha.interfaces.CaptchaPainter;

/**
 * Dibujador de Captchas que realiza la imegen con stroke.
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class StrokeCaptcha implements CaptchaPainter {
    private float[] rads=new float[]{0, 0.45f, -0.45f, 0.63f, -0.63f};
    Stroke[] strokes = new Stroke[] { new BasicStroke(4.0f), new NullStroke(), new DoubleStroke(8.0f, 2.0f), new SloppyStroke(2.0f, 3.0f)};
    Color[] colores=new Color[]{Color.BLACK, Color.BLUE, Color.ORANGE, Color.MAGENTA, Color.DARK_GRAY, Color.GREEN, Color.PINK, Color.RED};
    int[] tams=new int[]{60, 80, 100, 120};

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
        GlyphVector gv;
        Shape shape;
        int espa;

        //define la fuente a utilizar
        Font f=new Font("Serif", Font.BOLD, 80);

        //calculo el espacio por letra
        Rectangle2D rec=f.getMaxCharBounds(new FontRenderContext(new AffineTransform(), true, true));
        int ancho=(int)((letras.length*2+1)*(rec.getWidth()/3));
        int alto=(int)(rec.getHeight()+(rec.getHeight()/2));

        //defino la imagen
        BufferedImage image=new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g=(Graphics2D)image.createGraphics();

        //dibujo el fondo
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, ancho, alto);
        espa=(int)(ancho/(letras.length+2));
        g.translate(espa, alto/3*2);

        //dibujo las letras
        for(int i=0; i<letras.length; i++){
            f=new Font("Serif", Font.BOLD, this.tams[ Math.abs(r.nextInt()%this.tams.length)]);
            
            trans=new AffineTransform();
            trans.setToRotation(this.rads[ Math.abs(r.nextInt()%this.rads.length) ]);
            gv = f.deriveFont(trans).createGlyphVector(g.getFontRenderContext(), ""+letras[i]);
            shape = gv.getOutline();

            g.setColor(this.colores[ Math.abs(r.nextInt()%this.colores.length) ]);
            g.setStroke(this.strokes[ Math.abs(r.nextInt()%this.strokes.length) ]);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.draw(shape);
            g.translate(espa, 0);
        }

        //finalizo la imagen
        g.dispose();

        return image;
    }
    
}

class NullStroke implements Stroke {
  public Shape createStrokedShape(Shape s) {
    return s;
  }
}

class DoubleStroke implements Stroke {
  BasicStroke stroke1, stroke2; // the two strokes to use

  public DoubleStroke(float width1, float width2) {
    stroke1 = new BasicStroke(width1); // Constructor arguments specify
    stroke2 = new BasicStroke(width2); // the line widths for the strokes
  }

  public Shape createStrokedShape(Shape s) {
    // Use the first stroke to create an outline of the shape
    Shape outline = stroke1.createStrokedShape(s);
    // Use the second stroke to create an outline of that outline.
    // It is this outline of the outline that will be filled in
    return stroke2.createStrokedShape(outline);
  }
}

class ControlPointsStroke implements Stroke {
  float radius; // how big the control point markers should be

  public ControlPointsStroke(float radius) {
    this.radius = radius;
  }

  public Shape createStrokedShape(Shape shape) {
    // Start off by stroking the shape with a thin line. Store the
    // resulting shape in a GeneralPath object so we can add to it.
    GeneralPath strokedShape = new GeneralPath(new BasicStroke(1.0f)
        .createStrokedShape(shape));

    // Use a PathIterator object to iterate through each of the line and
    // curve segments of the shape. For each one, mark the endpoint and
    // control points (if any) by adding a rectangle to the GeneralPath
    float[] coords = new float[6];
    for (PathIterator i = shape.getPathIterator(null); !i.isDone(); i
        .next()) {
      int type = i.currentSegment(coords);
      Shape s = null, s2 = null, s3 = null;
      switch (type) {
      case PathIterator.SEG_CUBICTO:
        markPoint(strokedShape, coords[4], coords[5]); // falls through
      case PathIterator.SEG_QUADTO:
        markPoint(strokedShape, coords[2], coords[3]); // falls through
      case PathIterator.SEG_MOVETO:
      case PathIterator.SEG_LINETO:
        markPoint(strokedShape, coords[0], coords[1]); // falls through
      case PathIterator.SEG_CLOSE:
        break;
      }
    }

    return strokedShape;
  }

  /** Add a small square centered at (x,y) to the specified path */
  void markPoint(GeneralPath path, float x, float y) {
    path.moveTo(x - radius, y - radius); // Begin a new sub-path
    path.lineTo(x + radius, y - radius); // Add a line segment to it
    path.lineTo(x + radius, y + radius); // Add a second line segment
    path.lineTo(x - radius, y + radius); // And a third
    path.closePath(); // Go back to last moveTo position
  }
}

class SloppyStroke implements Stroke {
  BasicStroke stroke;

  float sloppiness;

  public SloppyStroke(float width, float sloppiness) {
    this.stroke = new BasicStroke(width); // Used to stroke modified shape
    this.sloppiness = sloppiness; // How sloppy should we be?
  }

  public Shape createStrokedShape(Shape shape) {
    GeneralPath newshape = new GeneralPath(); // Start with an empty shape

    // Iterate through the specified shape, perturb its coordinates, and
    // use them to build up the new shape.
    float[] coords = new float[6];
    for (PathIterator i = shape.getPathIterator(null); !i.isDone(); i
        .next()) {
      int type = i.currentSegment(coords);
      switch (type) {
      case PathIterator.SEG_MOVETO:
        perturb(coords, 2);
        newshape.moveTo(coords[0], coords[1]);
        break;
      case PathIterator.SEG_LINETO:
        perturb(coords, 2);
        newshape.lineTo(coords[0], coords[1]);
        break;
      case PathIterator.SEG_QUADTO:
        perturb(coords, 4);
        newshape.quadTo(coords[0], coords[1], coords[2], coords[3]);
        break;
      case PathIterator.SEG_CUBICTO:
        perturb(coords, 6);
        newshape.curveTo(coords[0], coords[1], coords[2], coords[3],
            coords[4], coords[5]);
        break;
      case PathIterator.SEG_CLOSE:
        newshape.closePath();
        break;
      }
    }

    // Finally, stroke the perturbed shape and return the result
    return stroke.createStrokedShape(newshape);
  }

  // Randomly modify the specified number of coordinates, by an amount
  // specified by the sloppiness field.
  void perturb(float[] coords, int numCoords) {
    for (int i = 0; i < numCoords; i++)
      coords[i] += (float) ((Math.random() * 2 - 1.0) * sloppiness);
  }
}
