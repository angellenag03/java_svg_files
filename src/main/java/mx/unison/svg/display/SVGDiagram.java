package mx.unison.svg.display;

import mx.unison.svg.util.SVGShape;

import javax.swing.*;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

public class SVGDiagram extends JComponent {

    private static final Logger LOG = Logger.getLogger(SVGDiagram.class.getName());

    private final int svgW; // ancho del dibujo
    private final int svgH;  // alto del dibujo

    // figuras leidas del archivo SVG, en el orden del documento
    private final ArrayList<SVGShape> shapes;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(svgW, svgH);
    }


    public SVGDiagram(File file) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);

        TagsHandler tagsHandler = new TagsHandler();

        try {
            LOG.info("Parsing SVG file: " + file.getAbsolutePath());
            factory.newSAXParser().parse(file, tagsHandler);
        } catch (Exception e) {
            LOG.severe("Error in SVG file: " + e.getMessage());
            e.printStackTrace();
        }
        // Si el archivo tiene errores se conserva lo leído hasta ese punto
        svgW = tagsHandler.getWidth();
        svgH = tagsHandler.getHeight();
        shapes = tagsHandler.getShapes();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        LOG.info("Painting SVG Diagram");

        // Se trabaja sobre una copia para no alterar el contexto que recibe Swing
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            // Cada figura aplica su estilo: un error en una no afecta a las siguientes
            for (SVGShape shape : shapes) {
                try {
                    shape.draw(g2d);
                } catch (RuntimeException e) {
                    LOG.warning("Error drawing " + shape.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        } finally {
            g2d.dispose();
        }
    }

}
