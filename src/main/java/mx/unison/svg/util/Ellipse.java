package mx.unison.svg.util;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ellipse extends SVGShape {
    private final double cx;
    private final double cy;
    private double rx;
    private double ry;

    public Ellipse(double rx, double ry) {
        this(0.0, 0.0, rx, ry);
    }

    /**
     * Crea una elipse con todos sus atributos geométricos.
     *
     * @param cx coordenada x del centro
     * @param cy coordenada y del centro
     * @param rx radio horizontal
     * @param ry radio vertical
     */
    public Ellipse(double cx, double cy, double rx, double ry) {
        this.cx = cx;
        this.cy = cy;
        this.rx = rx;
        this.ry = ry;
    }

    public double area() {
        return Math.PI * rx * ry;
    }

    public double perimeter() {
        // Aproximación de Ramanujan para el perímetro de una elipse
        return Math.PI * (3 * (rx + ry) - Math.sqrt((3 * rx + ry) * (rx + 3 * ry)));
    }

    /**
     * @return {@code true} si ambos radios son positivos
     */
    @Override
    public boolean isValid() {
        return rx > 0 && ry > 0;
    }

    // Ellipse2D recibe la región rectangular que delimita a la elipse.
    @Override
    protected Shape toAwtShape() {
        return new Ellipse2D.Double(cx - rx, cy - ry, rx * 2, ry * 2);
    }

    public double getRx() {
        return rx;
    }

    public void setRx(double rx) {
        this.rx = rx;
    }

    public double getRy() {
        return ry;
    }

    public void setRy(double ry) {
        this.ry = ry;
    }
}
