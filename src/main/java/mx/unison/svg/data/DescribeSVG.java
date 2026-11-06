package mx.unison.svg.data;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.logging.Logger;

public class DescribeSVG {
    private static final Logger logger = Logger.getLogger(DescribeSVG.class.getName());

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java DescribeSVG <svg-file>");
            System.exit(1);
        }

        // Configurar factory para crear un parser SAX
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);

        File svgFilePath = new File(args[0]);
        System.out.println("Describing SVG file: " + svgFilePath);
        SAXParser parser = null;
        try {
            // Crear un parser SAX
            parser = factory.newSAXParser();
        } catch (Exception e) {
            logger.severe("Error creating SAX parser: " +e.getMessage());
            System.exit(1);
        }

        // Crear un manejador de eventos para procesar el documento SVG
        SVGDocumentHandler handler = new SVGDocumentHandler();

        try {
            //  Parsear el archivo SVG usando el manejador de eventos
            System.out.println("Parsing...");
            parser.parse(svgFilePath, handler);
        } catch (Exception e) {
            logger.severe("Error parsing SVG file: " +e.getMessage());
            System.exit(2);
        }
    }
}
