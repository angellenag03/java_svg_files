package mx.unison.svg.display;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TagsHandler  extends DefaultHandler {
    public static final Logger LOG = Logger.getLogger( TagsHandler.class.getName() );

    private ArrayList<Properties> shapes;
    private Properties currentShape = null;
    
    private static final Set<String> SUPPORTED_SHAPES = Set.of(
        "line", "ellipse", "circle", "polyline", "rect", "text"
    );
    
    public TagsHandler() {
        super();
        shapes = new ArrayList<>();
    }

    public ArrayList<Properties> getShapes() {
        return shapes;
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        super.setDocumentLocator(locator);
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
            super.startPrefixMapping(prefix, uri);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        super.endPrefixMapping(prefix);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        String tagName = null;

        if(localName == null || localName.isEmpty()) {
            tagName = qName.toLowerCase();
        } else {
            tagName = localName.toLowerCase();
        }
        // valores admitidos: line, ellipse, circle, polyline, rect
        // se añade un set para simplificar más el código
        if (SUPPORTED_SHAPES.contains(tagName)) {
            addShape(tagName, atts);
        }
    }
    
    // método de apoyo para startElement, en vez de tener que hacer ifs uno por uno
    private void addShape(String tagName, Attributes atts) {
        LOG.info("Starting element " + tagName);
            currentShape = new Properties();
            currentShape.put("type", tagName);

            copyAttributes(atts, currentShape);
            shapes.add(currentShape);
    }

    private void copyAttributes(Attributes atts, Properties myShape) {
        if(atts.getLength() != 0) {
            for(int i = 0; i < atts.getLength(); i++) {
                String name = atts.getLocalName(i);
                String value = atts.getValue(i);
                myShape.put(name, value);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        currentShape = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        LOG.log(Level.SEVERE, "OOPS" + e.getMessage());
    }
}
