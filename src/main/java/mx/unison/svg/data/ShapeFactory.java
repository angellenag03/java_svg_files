package mx.unison.svg.data;

import mx.unison.svg.util.Circle;
import mx.unison.svg.util.ColorParser;
import mx.unison.svg.util.Ellipse;
import mx.unison.svg.util.Line;
import mx.unison.svg.util.Polyline;
import mx.unison.svg.util.Rect;
import mx.unison.svg.util.SVGShape;
import mx.unison.svg.util.SVGStyle;
import mx.unison.svg.util.Text;
import org.xml.sax.Attributes;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Construye las figuras a partir de los atributos de un elemento SVG. Un
 * atributo ausente o incorrecto se sustituye por su valor predeterminado, de
 * modo que un elemento con datos erróneos nunca detiene el análisis.
 */
public final class ShapeFactory {
    private static final Logger LOG = Logger.getLogger(ShapeFactory.class.getName());

    private ShapeFactory() {
    }

    /**
     * Crea la figura que corresponde a un elemento SVG.
     *
     * @param tagName nombre del elemento, en minúsculas
     * @param atts    atributos del elemento
     * @return la figura, o {@code null} si el elemento no es una figura admitida
     */
    public static SVGShape create(String tagName, Attributes atts) {
        SVGShape shape = switch (tagName) {
            case "line" -> new Line(
                    number(atts, "x1"),
                    number(atts, "y1"),
                    number(atts, "x2"),
                    number(atts, "y2"));
            case "circle" -> new Circle(
                    number(atts, "cx"),
                    number(atts, "cy"),
                    number(atts, "r"));
            case "ellipse" -> new Ellipse(
                    number(atts, "cx"),
                    number(atts, "cy"),
                    number(atts, "rx"),
                    number(atts, "ry"));
            case "rect" -> new Rect(
                    number(atts, "x"),
                    number(atts, "y"),
                    number(atts, "width"),
                    number(atts, "height"),
                    parseNumber(atts.getValue("rx"), Rect.AUTO_RADIUS),
                    parseNumber(atts.getValue("ry"), Rect.AUTO_RADIUS));
            case "polyline" -> new Polyline(parsePoints(atts.getValue("points")));
            // Text sustituye un tamaño de fuente no positivo por el predeterminado.
            case "text" -> new Text(
                    number(atts, "x"),
                    number(atts, "y"),
                    atts.getValue("font-family"),
                    number(atts, "font-size"),
                    "");
            default -> null;
        };
        if (shape != null) {
            shape.setStyle(parseStyle(atts));
        }
        return shape;
    }

    /**
     * Convierte el valor de un atributo numérico.
     *
     * @param value        valor del atributo; puede ser {@code null}
     * @param defaultValue valor que se usa si el atributo falta o no es un número finito
     * @return el número interpretado
     */
    public static double parseNumber(String value, double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            double number = Double.parseDouble(value.trim());
            if (Double.isFinite(number)) {
                return number;
            }
        } catch (NumberFormatException e) {
            // Se reporta abajo, igual que un valor no finito.
        }
        LOG.warning("Invalid number: " + value);
        return defaultValue;
    }

    // En SVG las coordenadas y dimensiones ausentes valen 0.
    private static double number(Attributes atts, String name) {
        return parseNumber(atts.getValue(name), 0.0);
    }

    // Las coordenadas pueden separarse con comas, espacios o ambos. Si hay un dato
    // incorrecto o una coordenada sin pareja, se conservan los vértices anteriores.
    private static List<Point2D.Double> parsePoints(String points) {
        List<Point2D.Double> pointList = new ArrayList<>();
        if (points == null || points.isBlank()) {
            return pointList;
        }

        String[] coordenadas = points.trim().split("[\\s,]+");
        try {
            for (int i = 0; i + 1 < coordenadas.length; i += 2) {
                pointList.add(new Point2D.Double(Double.parseDouble(coordenadas[i]),
                                                 Double.parseDouble(coordenadas[i + 1])));
            }
        } catch (NumberFormatException e) {
            LOG.warning("Invalid points: " + points);
            return pointList;
        }
        if (coordenadas.length % 2 != 0) {
            LOG.warning("Odd number of coordinates: " + points);
        }
        return pointList;
    }

    private static SVGStyle parseStyle(Attributes atts) {
        Color fill = parsePaint(atts.getValue("fill"), SVGStyle.DEFAULT_FILL);
        Color stroke = parsePaint(atts.getValue("stroke"), null);

        double strokeWidth = parseNumber(atts.getValue("stroke-width"), SVGStyle.DEFAULT_STROKE_WIDTH);
        if (strokeWidth < 0) {
            LOG.warning("Invalid stroke width: " + strokeWidth);
            strokeWidth = SVGStyle.DEFAULT_STROKE_WIDTH;
        }
        return new SVGStyle(fill, stroke, (float) strokeWidth);
    }

    // Devuelve null para "no pintar", que es distinto de un color inválido.
    private static Color parsePaint(String value, Color defaultColor) {
        if (value == null) {
            return defaultColor;
        }
        String paint = value.trim().toLowerCase();
        if (paint.equals("none") || paint.equals("transparent")) {
            return null;
        }
        Color color = ColorParser.parse(paint);
        if (color == null) {
            LOG.warning("Unknown color: " + value);
            return defaultColor;
        }
        return color;
    }
}
