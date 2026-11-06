package mx.unison.svg.util;

import java.awt.*;

/**
 * Abstracción común de los elementos gráficos de un documento SVG.
 * <p>
 * Cada figura sabe construir su geometría; el relleno y el contorno se delegan
 * en su {@link SVGStyle}. Así el visor puede dibujar cualquier elemento sin
 * conocer su tipo concreto.
 */
public abstract class SVGShape {
    private SVGStyle style = new SVGStyle();

    /**
     * Construye la geometría de la figura para Java 2D.
     *
     * @return figura lista para rellenarse y trazarse
     */
    protected abstract Shape toAwtShape();

    /**
     * Indica si la figura tiene datos suficientes para representarse. Por
     * ejemplo, SVG no dibuja un rectángulo de ancho cero.
     *
     * @return {@code true} si la figura se puede dibujar
     */
    public boolean isValid() {
        return true;
    }

    /**
     * Dibuja la figura con su estilo. Una figura no válida no se dibuja.
     *
     * @param g2d contexto gráfico donde se dibuja
     */
    public void draw(Graphics2D g2d) {
        if (isValid()) {
            style.paint(g2d, toAwtShape());
        }
    }

    /**
     * @param style nuevo estilo de la figura
     */
    public void setStyle(SVGStyle style) {
        this.style = style;
    }
}
