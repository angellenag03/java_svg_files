package mx.unison.svg.util;

import java.awt.*;

/**
 * Representa un elemento SVG {@code <text>}.
 * <p>
 * Guarda la posición de la línea base ({@code x}, {@code y}), la familia y el
 * tamaño de la fuente, y el contenido textual que aparece entre las etiquetas
 * de apertura y cierre del elemento.
 */
public class Text {
    /** Tamaño de fuente predeterminado en SVG ("medium"). */
    public static final double DEFAULT_FONT_SIZE = 16.0;
    /** Familia de fuente predeterminada. */
    public static final String DEFAULT_FONT_FAMILY = "serif";

    private double x;
    private double y;
    private String fontFamily;
    private double fontSize;
    private String texto;

    /**
     * Crea un texto vacío en el origen con la fuente predeterminada.
     */
    public Text() {
        this(0.0, 0.0, DEFAULT_FONT_FAMILY, DEFAULT_FONT_SIZE, "");
    }

    /**
     * Crea un texto en la posición indicada con la fuente predeterminada.
     *
     * @param x     coordenada x de la línea base
     * @param y     coordenada y de la línea base
     * @param texto contenido textual del elemento
     */
    public Text(double x, double y, String texto) {
        this(x, y, DEFAULT_FONT_FAMILY, DEFAULT_FONT_SIZE, texto);
    }

    /**
     * Crea un texto con todos sus atributos.
     *
     * @param x          coordenada x de la línea base
     * @param y          coordenada y de la línea base
     * @param fontFamily familia de fuente (atributo {@code font-family})
     * @param fontSize   tamaño de fuente (atributo {@code font-size})
     * @param texto      contenido textual del elemento
     */
    public Text(double x, double y, String fontFamily, double fontSize, String texto) {
        this.x = x;
        this.y = y;
        setFontFamily(fontFamily);
        setFontSize(fontSize);
        setTexto(texto);
    }

    /**
     * Normaliza el contenido textual como lo hace SVG por omisión
     * ({@code xml:space="default"}): los saltos de línea, tabuladores y espacios
     * consecutivos se reducen a un solo espacio y se eliminan los espacios
     * iniciales y finales.
     *
     * @param raw texto tal como lo entrega el analizador SAX
     * @return texto normalizado, o cadena vacía si {@code raw} es {@code null}
     */
    public static String normalize(String raw) {
        if (raw == null) {
            return "";
        }
        return raw.replaceAll("\\s+", " ").trim();
    }

    /**
     * Construye la fuente de Java 2D que corresponde a los atributos
     * {@code font-family} y {@code font-size}. Si {@code font-family} es una
     * lista (p. ej. {@code "Arial, sans-serif"}) se usa la primera familia, y las
     * familias genéricas de CSS se traducen a las fuentes lógicas de Java.
     *
     * @return fuente para dibujar el texto
     */
    public Font toFont() {
        String family = fontFamily.split(",")[0].trim().replace("\"", "").replace("'", "");
        String javaFamily = switch (family.toLowerCase()) {
            case "sans-serif" -> Font.SANS_SERIF;
            case "serif" -> Font.SERIF;
            case "monospace" -> Font.MONOSPACED;
            default -> family;
        };
        return new Font(javaFamily, Font.PLAIN, 1).deriveFont((float) fontSize);
    }

    /**
     * Dibuja el texto usando el color actual de {@code g2d}. Las coordenadas
     * {@code x}, {@code y} se interpretan como la posición de la línea base,
     * igual que en SVG.
     *
     * @param g2d contexto gráfico donde se dibuja
     */
    public void draw(Graphics2D g2d) {
        if (texto.isEmpty()) {
            return;
        }
        Font previous = g2d.getFont();
        g2d.setFont(toFont());
        g2d.drawString(texto, (float) x, (float) y);
        g2d.setFont(previous);
    }

    /**
     * @return coordenada x de la línea base
     */
    public double getX() {
        return x;
    }

    /**
     * @param x nueva coordenada x de la línea base
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return coordenada y de la línea base
     */
    public double getY() {
        return y;
    }

    /**
     * @param y nueva coordenada y de la línea base
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return familia de fuente
     */
    public String getFontFamily() {
        return fontFamily;
    }

    /**
     * @param fontFamily nueva familia de fuente; si es nula o vacía se usa
     *                   {@link #DEFAULT_FONT_FAMILY}
     */
    public void setFontFamily(String fontFamily) {
        this.fontFamily = (fontFamily == null || fontFamily.isBlank())
                ? DEFAULT_FONT_FAMILY : fontFamily.trim();
    }

    /**
     * @return tamaño de fuente
     */
    public double getFontSize() {
        return fontSize;
    }

    /**
     * @param fontSize nuevo tamaño de fuente; si no es positivo se usa
     *                 {@link #DEFAULT_FONT_SIZE}
     */
    public void setFontSize(double fontSize) {
        this.fontSize = fontSize > 0 ? fontSize : DEFAULT_FONT_SIZE;
    }

    /**
     * @return contenido textual del elemento (ya normalizado)
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Asigna el contenido textual, normalizando los espacios en blanco.
     *
     * @param texto contenido entre las etiquetas {@code <text>} y {@code </text>}
     */
    public void setTexto(String texto) {
        this.texto = normalize(texto);
    }

    @Override
    public String toString() {
        return String.format("Text[x=%s, y=%s, font-family=%s, font-size=%s, texto=\"%s\"]",
                x, y, fontFamily, fontSize, texto);
    }
}
