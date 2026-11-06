package mx.unison.svg.util;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Line extends SVGShape {
    private double x1;
    private double x2;

    private double y1;
    private double y2;

    public Line() {
        super();
    }

    public Line(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public double lenght() {
        return Point2D.distance(x1, y1, x2, y2);
    }

    public double m() {
        if( x2 - x1 == 0.0 ) {
            throw new ArithmeticException("Division entre cero");
        }
        return (y2 - y1) / ( x2 - x1 );
    }

    @Override
    protected Shape toAwtShape() {
        return new Line2D.Double(x1, y1, x2, y2);
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    /**
     * @return coordenada x del extremo final
     */
    public double getX2() {
        return x2;
    }

    /**
     * @param x2 nueva coordenada x del extremo final
     */
    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }
}
