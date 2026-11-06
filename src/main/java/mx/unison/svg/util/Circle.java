package mx.unison.svg.util;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Circle extends SVGShape {
    private final double cx;
    private final double cy;
    private double radio;

    public Circle(double radio) {
        this(0.0, 0.0, radio);
    }

    /**
     * Crea un círculo con todos sus atributos geométricos.
     *
     * @param cx    coordenada x del centro
     * @param cy    coordenada y del centro
     * @param radio radio del círculo
     */
    public Circle(double cx, double cy, double radio) {
        this.cx = cx;
        this.cy = cy;
        this.radio = radio;
    }

    public double area() {
        return Math.PI * Math.pow(radio, 2.0);
    }

    public double perimeter() {
        return 2.0 * Math.PI * radio;
    }

    /**
     * @return {@code true} si el radio es positivo
     */
    @Override
    public boolean isValid() {
        return radio > 0;
    }

    @Override
    protected Shape toAwtShape() {
        return new Ellipse2D.Double(cx - radio, cy - radio, radio * 2, radio * 2);
    }

    public double getRadio() {
        return radio;
    }

    public void setRadio(double radio) {
        this.radio = radio;
    }
}
