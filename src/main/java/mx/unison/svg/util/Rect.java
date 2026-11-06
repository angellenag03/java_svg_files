package mx.unison.svg.util;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class Rect extends SVGShape {
    /** Indica que un radio no se especificó y debe tomar el valor del otro. */
    public static final double AUTO_RADIUS = -1.0;

    private final double x;
    private final double y;
    private double width;
    private double height;
    private final double rx;
    private final double ry;

    public Rect(double width, double height) {
        this(0.0, 0.0, width, height, AUTO_RADIUS, AUTO_RADIUS);
    }

    /**
     * Crea un rectángulo con todos sus atributos geométricos. Un radio negativo
     * equivale a {@link #AUTO_RADIUS}: si solo se indica un radio, SVG lo usa en
     * ambos ejes.
     *
     * @param x      coordenada x de la esquina superior izquierda
     * @param y      coordenada y de la esquina superior izquierda
     * @param width  ancho
     * @param height alto
     * @param rx     radio horizontal de las esquinas
     * @param ry     radio vertical de las esquinas
     */
    public Rect(double x, double y, double width, double height, double rx, double ry) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rx = rx >= 0 ? rx : Math.max(ry, 0.0);
        this.ry = ry >= 0 ? ry : Math.max(rx, 0.0);
    }

    public double area() {
        return width * height;
    }

    public double perimeter() {
        return (width * 2) + (height * 2);
    }

    /**
     * @return {@code true} si el ancho y el alto son positivos
     */
    @Override
    public boolean isValid() {
        return width > 0 && height > 0;
    }

    @Override
    protected Shape toAwtShape() {
        double cornerX = Math.min(rx, width / 2);
        double cornerY = Math.min(ry, height / 2);

        if (cornerX > 0 && cornerY > 0) {
            // RoundRectangle2D recibe el diámetro de las esquinas, no el radio.
            return new RoundRectangle2D.Double(x, y, width, height, cornerX * 2, cornerY * 2);
        }
        return new Rectangle2D.Double(x, y, width, height);
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
