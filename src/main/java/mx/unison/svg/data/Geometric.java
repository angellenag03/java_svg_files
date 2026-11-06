package mx.unison.svg.data;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import mx.unison.svg.util.Line;
import mx.unison.svg.util.Circle;
import mx.unison.svg.util.Ellipse;
import mx.unison.svg.util.Polyline;
import mx.unison.svg.util.Rect;
import mx.unison.svg.util.Text;


public class Geometric extends DefaultHandler {
    private final static Logger LOG = Logger.getLogger(Geometric.class.getSimpleName());

    // Elemento <text> en lectura (null fuera de <text>) y su contenido acumulado.
    private Text currentText = null;
    private final StringBuilder textBuffer = new StringBuilder();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (ShapeFactory.create(localName, attributes)) {
            case Line line -> {
                // Una linea vertical no tiene pendiente definida
                String pendiente = line.getX1() == line.getX2()
                        ? "indefinida" : String.format("%6.2f", line.m());
                System.out.printf("Linea\n\tLongitud: %6.2f , pendiente: %s\n", line.lenght(), pendiente);
            }
            case Circle circle -> System.out.printf("Circulo\n\tArea: %6.2f , perimetro: %6.2f\n",
                    circle.area(),
                    circle.perimeter());
            case Rect rect -> System.out.printf("Rectangulo\n\tArea: %6.2f , perimetro: %6.2f\n",
                    rect.area(),
                    rect.perimeter());
            case Ellipse ellipse -> System.out.printf("Elipse\n\tArea: %6.2f , perimetro: %6.2f\n",
                    ellipse.area(),
                    ellipse.perimeter());
            case Polyline polyline -> System.out.printf("Polilinea\n\tVertices: %d , longitud: %6.2f\n",
                    polyline.getPoints().size(),
                    polyline.length());
            case Text text -> {
                currentText = text;
                textBuffer.setLength(0);
            }
            case null, default -> {
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // El contenido del <text> puede llegar en varios fragmentos.
        if (currentText != null) {
            textBuffer.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("text") && currentText != null) {
            currentText.setTexto(textBuffer.toString());
            System.out.printf("Texto\n\tPosicion: (%6.2f, %6.2f), fuente: %s %6.2f\n\tContenido: \"%s\" (%d caracteres)\n",
                    currentText.getX(),
                    currentText.getY(),
                    currentText.getFontFamily(),
                    currentText.getFontSize(),
                    currentText.getTexto(),
                    currentText.getTexto().length());
            currentText = null;
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            LOG.severe("Falta el nombre del documento SVG. Uso:\njava Geometric <filename>");
            System.exit(1);
        }

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        SAXParser saxParser = null;

        try {
            saxParser = factory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            LOG.severe( e.getMessage());
        }

        File xmlFile = new File(args[0]);

        Geometric handler = new Geometric();

        try {
            saxParser.parse(xmlFile, handler);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            LOG.severe( e.getMessage());
        }
    }
}
