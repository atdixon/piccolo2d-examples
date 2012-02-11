package atdixon.java2d.example;

import edu.umd.cs.piccolo.util.PPaintContext;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class Issue174Reduced implements Printable {

    public static void main(String[] args) {
        Issue174Reduced demo = new Issue174Reduced();
        demo.print();
    }

    public void print() {
        final PrinterJob printJob = PrinterJob.getPrinterJob();
        final PageFormat pageFormat = printJob.defaultPage();
        final Book book = new Book();
        book.append(this, pageFormat);
        printJob.setPageable(book);

        if (printJob.printDialog()) {
            try {
                printJob.print();
            }
            catch (final PrinterException e) {
                throw new RuntimeException("Error Printing", e);
            }
        }
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex != 0) {
            return NO_SUCH_PAGE;
        }

        if (!(graphics instanceof Graphics2D)) {
            throw new IllegalArgumentException("Provided graphics context is not a Graphics2D object");
        }

        final Graphics2D g2 = (Graphics2D) graphics;

        g2.setClip(0, 0, (int) pageFormat.getWidth(), (int) pageFormat.getHeight());
        g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        final PPaintContext pc = new PPaintContext(g2);
        pc.setRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);

        final Rectangle2D rect = new Rectangle2D.Double(0, 0, 200, 200);
        final AffineTransform xform = new AffineTransform(0.001, 0, 0, 0.001, 0, 0);

        // use the same path to fill and stroke -- still, they diverge
        Path2D.Double path = new Path2D.Double();
        path.append(rect, false);

        // transform for printing
        g2.transform(xform);

        // draw the path -- stroke size doesn't seem to matter, use 1
        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        g2.setColor(Color.red);
        g2.draw(path);

        // draw the rectangle
        g2.setColor(Color.blue);
        g2.fill(path);

        return PAGE_EXISTS;
    }
}
