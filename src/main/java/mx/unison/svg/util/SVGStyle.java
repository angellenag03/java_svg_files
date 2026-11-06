package mx.unison.svg.util;

import java.awt.*;

/**
 * Atributos de presentación de una figura SVG. Un color {@code null} equivale
 * al valor {@code none}.
 *
 * @param fill        color de relleno, o {@code null} para no rellenar
 * @param stroke      color del contorno, o {@code null} para no trazarlo
 * @param strokeWidth grosor del contorno
 */
public record SVGStyle(Color fill, Color stroke, float strokeWidth) {
    /** Relleno predeterminado en SVG. */
    public static final Color DEFAULT_FILL = Color.BLACK;
    /** Grosor de contorno predeterminado en SVG. */
    public static final float DEFAULT_STROKE_WIDTH = 1.0f;

    // Límite de inglete predeterminado en SVG (stroke-miterlimit).
    private static final float MITER_LIMIT = 4.0f;

    /**
     * Crea el estilo predeterminado de SVG: relleno negro y sin contorno.
     */
    public SVGStyle() {
        this(DEFAULT_FILL, null, DEFAULT_STROKE_WIDTH);
    }

    /**
     * Pinta una figura: primero el relleno y después el contorno, igual que SVG.
     *
     * @param g2d   contexto gráfico donde se dibuja
     * @param shape figura que se va a pintar
     */
    public void paint(Graphics2D g2d, Shape shape) {
        if (fill != null) {
            g2d.setColor(fill);
            g2d.fill(shape);
        }
        if (stroke != null && strokeWidth > 0) {
            g2d.setColor(stroke);
            // Los extremos y uniones son los predeterminados de SVG, no los de BasicStroke.
            g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, MITER_LIMIT));
            g2d.draw(shape);
        }
    }
}
