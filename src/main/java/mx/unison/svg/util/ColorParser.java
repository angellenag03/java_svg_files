package mx.unison.svg.util;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Interpreta los colores de un documento SVG: nombres web, {@code #rgb},
 * {@code #rrggbb}, {@code 0xrrggbb}, {@code rgb(...)} y {@code rgba(...)}.
 * <p>
 * Los nombres se leen de {@code colors.properties}, que se busca en el classpath
 * y después en el directorio de trabajo.
 */
public final class ColorParser {
    private static final Logger LOG = Logger.getLogger(ColorParser.class.getName());

    private static final String COLORS_FILE = "colors.properties";
    private static final Properties WEB_COLORS = loadColors();

    private ColorParser() {
    }

    // https://developer.mozilla.org/en-US/docs/Web/CSS/Reference/Values/named-color
    private static Properties loadColors() {
        Properties colors = new Properties();
        try (InputStream stream = openColors()) {
            colors.load(stream);
        } catch (IOException ex) {
            LOG.severe("Named colors are not available: " + ex.getMessage());
        }
        return colors;
    }

    private static InputStream openColors() throws IOException {
        InputStream resource = ColorParser.class.getResourceAsStream("/" + COLORS_FILE);
        if (resource != null) {
            return resource;
        }
        return new FileInputStream(new File(System.getProperty("user.dir"), COLORS_FILE));
    }

    /**
     * Convierte un valor de color a {@link Color}.
     *
     * @param value valor del color, por ejemplo {@code red}, {@code #f00} o
     *              {@code rgb(255,0,0)}
     * @return el color, o {@code null} si el valor no es válido
     */
    public static Color parse(String value) {
        String colorCode = value.trim().toLowerCase();

        try {
            if (colorCode.startsWith("#")) {
                return parseHex(colorCode.substring(1));
            }
            if (colorCode.startsWith("0x")) {
                return parseHex(colorCode.substring(2));
            }
            if (colorCode.startsWith("rgb")) {
                return parseRgb(colorCode);
            }
        } catch (NumberFormatException nfe) {
            return null;
        }

        String hex = WEB_COLORS.getProperty(colorCode);
        return hex == null ? null : parse(hex);
    }

    private static Color parseHex(String hex) {
        if (hex.length() == 3) {
            // "#f0a" equivale a "#ff00aa"
            hex = hex.replaceAll("(.)", "$1$1");
        }
        if (hex.length() != 6) {
            return null;
        }
        return new Color(Integer.parseInt(hex, 16));
    }

    // Codigo de color "rgb(0,255,0)", "rgb(0%,100%,0%)" o "rgba(0,255,0,0.5)"
    private static Color parseRgb(String colorCode) {
        int open = colorCode.indexOf('(');
        if (open < 0 || !colorCode.endsWith(")")) {
            return null;
        }
        String[] parts = colorCode.substring(open + 1, colorCode.length() - 1).trim().split("[\\s,]+");
        if (parts.length != 3 && parts.length != 4) {
            return null;
        }
        int r = parseChannel(parts[0]);
        int g = parseChannel(parts[1]);
        int b = parseChannel(parts[2]);
        double alpha = parts.length == 4 ? Double.parseDouble(parts[3]) : 1.0;
        int a = (int) Math.round(Math.clamp(alpha, 0.0, 1.0) * 255);
        return new Color(r, g, b, a);
    }

    private static int parseChannel(String channel) {
        double value = channel.endsWith("%")
                ? Double.parseDouble(channel.substring(0, channel.length() - 1)) * 255 / 100
                : Double.parseDouble(channel);
        return (int) Math.round(Math.clamp(value, 0.0, 255.0));
    }
}
