package mx.unison.svg.util;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;


public class Polyline {
    private final List<Point2D.Double> pointList;

    public Polyline(List<Point2D.Double> pointList) {
        this.pointList = pointList;
    }



    public Polyline(String[] paresCoordenadas) {
        this.pointList = Arrays.stream(paresCoordenadas)
                               .map(par -> {
                                   String[] parSeparado = par.trim().split(",");
                                   return new Point2D.Double(Double.parseDouble(parSeparado[0]),
                                                             Double.parseDouble(parSeparado[1]));
                               }).toList();
    }



    public void draw(Graphics2D g2d) {
        if (pointList.isEmpty()) {
            return;
        }

        Path2D path2D = new Path2D.Double();
        // Ubica el inicio del trazo.
        path2D.moveTo(pointList.getFirst().x, pointList.getFirst().y);
        // Traza todos los puntos de la lista
        pointList.forEach(point -> path2D.lineTo(point.x, point.y));
        // Dibuja el trazo definido con path2D
        g2d.draw(path2D);
    }
}
