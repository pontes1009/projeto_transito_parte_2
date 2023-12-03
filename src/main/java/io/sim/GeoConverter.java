/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.sim;

import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class GeoConverter {

    private static MathTransform transform;

    static {
        try {
            // Define o sistema de coordenadas de origem (WGS 84)
            CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");

            // Define o sistema de coordenadas de destino (UTM zona 33N)
            CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:32633"); // Exemplo para UTM zona 33N

            // Cria a transformação de coordenadas
            transform = CRS.findMathTransform(sourceCRS, targetCRS);
        } catch (FactoryException e) {
            e.printStackTrace();
        }
    }

    public static double[] convertToGeo(double x, double y) {
        try {
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
            Coordinate coordinate = new Coordinate(x, y);
            Point point = geometryFactory.createPoint(coordinate);

            // Transforma do sistema de coordenadas de origem para o sistema de coordenadas de destino
            Point targetPoint = (Point) JTS.transform(point, transform);

            double longitude = targetPoint.getX();
            double latitude = targetPoint.getY();

            return new double[]{longitude, latitude};
        } catch (TransformException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        double x = 123456; // Substitua pelo valor de x
        double y = 654321; // Substitua pelo valor de y

        double[] geoCoordinates = convertToGeo(x, y);
        if (geoCoordinates != null) {
            double longitude = geoCoordinates[0];
            double latitude = geoCoordinates[1];
            System.out.println("Longitude: " + longitude + ", Latitude: " + latitude);
        } else {
            System.out.println("Erro na conversão.");
        }
    }
}
