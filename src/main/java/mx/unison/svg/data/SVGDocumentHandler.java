package mx.unison.svg.data;

import mx.unison.svg.util.Text;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SVGDocumentHandler extends DefaultHandler {

    // Acumula el contenido de un <text>; es null fuera de <text>.
    private StringBuilder textBuffer = null;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (localName) {
            case "svg" -> System.out.printf("SVG Document\n\tWidth: %s , Height: %s\n",
                    attributes.getValue("width"),
                    attributes.getValue("height"));
            case "g" -> System.out.printf("Group\n\tOpacity: %s\n", attributes.getValue("opacity"));
            case "circle" -> System.out.printf("Circle\n\tCenter: (%s, %s), Radius: %s\n",
                    attributes.getValue("cx"),
                    attributes.getValue("cy"),
                    attributes.getValue("r"));
            case "rect" -> System.out.printf("Rectangle\n\tPosition: (%s, %s), Width: %s, Height: %s\n",
                    attributes.getValue("x"),
                    attributes.getValue("y"),
                    attributes.getValue("width"),
                    attributes.getValue("height"));
            case "ellipse" -> System.out.printf("Ellipse\n\tCenter: (%s, %s), RadiusX: %s, RadiusY: %s\n",
                    attributes.getValue("cx"),
                    attributes.getValue("cy"),
                    attributes.getValue("rx"),
                    attributes.getValue("ry"));
            case "text" -> {
                textBuffer = new StringBuilder();
                System.out.printf("Text\n\tPosition: (%s, %s)\n",
                        attributes.getValue("x"),
                        attributes.getValue("y"));
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // SAX puede dividir el contenido en varios fragmentos: se acumulan
        // y se imprimen juntos al cerrar </text>.
        if(textBuffer != null) {
            textBuffer.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(localName.equals("text") && textBuffer != null) {
            String textContent = Text.normalize(textBuffer.toString());
            System.out.printf("\tText Content: %s\n", textContent);
            textBuffer = null;
        }
    }
}
