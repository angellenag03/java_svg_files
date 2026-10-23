package mx.unison.svg.util;

public class Ellipse {
    private double rx;
    private double ry;

    public Ellipse(double rx, double ry) {
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
