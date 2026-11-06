package mx.unison.svg.util;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;


public class Polyline extends SVGShape {
    private final List<Point2D.Double> pointList;

    public Polyline(List<Point2D.Double> pointList) {
        this.pointList = pointList;
    }

    /**
     * @return longitud total de los segmentos de la polilínea
     */
    public double length() {
        double total = 0.0;
        for (int i = 1; i < pointList.size(); i++) {
            total += pointList.get(i - 1).distance(pointList.get(i));
        }
        return total;
    }

    /**
     * @return {@code true} si hay al menos dos vértices para formar un segmento
     */
    @Override
    public boolean isValid() {
        return pointList.size() >= 2;
    }

    // La figura queda abierta. Al rellenarla, Java 2D cierra el contorno de forma
    // implícita, igual que SVG, sin trazar el segmento de cierre.
    @Override
    protected Shape toAwtShape() {
        Path2D path2D = new Path2D.Double();
        path2D.moveTo(pointList.getFirst().x, pointList.getFirst().y);
        for (Point2D.Double point : pointList.subList(1, pointList.size())) {
            path2D.lineTo(point.x, point.y);
        }
        return path2D;
    }

    /**
     * @return vértices de la polilínea, en orden
     */
    public List<Point2D.Double> getPoints() {
        return pointList;
    }
}
