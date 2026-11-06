package mx.unison.svg.display;

import mx.unison.svg.data.ShapeFactory;
import mx.unison.svg.util.SVGShape;
import mx.unison.svg.util.Text;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.logging.Logger;

public class TagsHandler  extends DefaultHandler {
    private static final Logger LOG = Logger.getLogger( TagsHandler.class.getName() );

    // Tamaño predeterminado de un documento SVG sin dimensiones
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 150;

    private final ArrayList<SVGShape> shapes = new ArrayList<>();
    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;

    // Figura que se está leyendo y nombre de su elemento.
    private SVGShape currentShape = null;
    private String currentTag = null;

    // Contenido del <text> que se está leyendo.
    private final StringBuilder textBuffer = new StringBuilder();

    /**
     * @return figuras válidas del documento, en el orden en que aparecen
     */
    public ArrayList<SVGShape> getShapes() {
        return shapes;
    }

    /**
     * @return ancho del documento
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return alto del documento
     */
    public int getHeight() {
        return height;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (localName.equals("svg")) {
            readDimensions(atts);
        }
        // Un elemento dentro de una figura (p. ej. <tspan>) no es otra figura.
        if (currentShape != null) {
            return;
        }
        String tagName = localName.toLowerCase();

        currentShape = ShapeFactory.create(tagName, atts);
        if (currentShape != null) {
            LOG.info("Starting element " + tagName);
            currentTag = tagName;
        }
    }

    // Toma width y height de <svg>; si faltan o no son válidos usa las medidas de
    // viewBox y, en último caso, el tamaño predeterminado.
    private void readDimensions(Attributes atts) {
        double viewW = DEFAULT_WIDTH;
        double viewH = DEFAULT_HEIGHT;

        String viewBox = atts.getValue("viewBox");
        if (viewBox != null) {
            String[] values = viewBox.trim().split("[\\s,]+");
            if (values.length == 4) {
                viewW = positiveNumber(values[2], viewW);
                viewH = positiveNumber(values[3], viewH);
            }
        }
        width = (int) Math.ceil(positiveNumber(atts.getValue("width"), viewW));
        height = (int) Math.ceil(positiveNumber(atts.getValue("height"), viewH));
        LOG.info("SVG Document\n\tWidth: " + width + " , Height: " + height);
    }

    private double positiveNumber(String value, double defaultValue) {
        double number = ShapeFactory.parseNumber(value, defaultValue);
        return number > 0 ? number : defaultValue;
    }

    /**
     * Termina la figura en lectura. Se agrega a la lista al cerrarse para conocer
     * todo su contenido; las figuras no se anidan, así que el orden del documento
     * se conserva. Una figura no válida se descarta sin afectar a las demás.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (currentShape == null || !localName.toLowerCase().equals(currentTag)) {
            return;
        }

        // Al cerrar </text> se guarda el contenido acumulado en la figura.
        if (currentShape instanceof Text text) {
            text.setTexto(textBuffer.toString());
            LOG.info("Text content: " + text.getTexto());
            textBuffer.setLength(0);
        }

        if (currentShape.isValid()) {
            shapes.add(currentShape);
        } else {
            LOG.warning("Ignoring invalid element: " + currentTag);
        }
        currentShape = null;
        currentTag = null;
    }

    /**
     * Recibe el texto que aparece entre etiquetas. SAX puede entregar el
     * contenido de un elemento en varios fragmentos, por eso se acumula en
     * {@code textBuffer} hasta que se cierra {@code </text>}. Los caracteres de
     * elementos anidados (p. ej. {@code <tspan>}) también se incluyen.
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentShape instanceof Text) {
            textBuffer.append(ch, start, length);
        }
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        LOG.severe("OOPS" + e.getMessage());
    }
}
