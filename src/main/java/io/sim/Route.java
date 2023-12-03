/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.sim;

/**
 *
 * @author vinicius
 */
import java.awt.Point;
import java.util.List;

public class Route {
    private String edgeTokens;
    private List<Point> points;
    private int nextPointIndex;

    public Route(List<Point> points, String edgeTokens) {
        this.points = points;
        this.nextPointIndex = 0;
        this.edgeTokens = edgeTokens;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public synchronized Point getNextPoint() {
        if (nextPointIndex < points.size()) {
            Point nextPoint = points.get(nextPointIndex);
            nextPointIndex++;
            return nextPoint;
        }
        return null; // Retorna null se a rota estiver completa
    }
    public List<Point> getPoints() {
        return points;
    }
    public double calculateDistance() {
        double distance = 0.0;

        for (int i = 0; i < points.size() - 1; i++) {
            Point currentPoint = points.get(i);
            Point nextPoint = points.get(i + 1);

            // Calcula a distância euclidiana entre os pontos
            double deltaX = nextPoint.getX() - currentPoint.getX();
            double deltaY = nextPoint.getY() - currentPoint.getY();
            distance += Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        }

        return distance;
    }
    // Outros métodos e propriedades necessários podem ser adicionados aqui

    /**
     * @return the edgeTokens
     */
    public String getEdgeTokens() {
        return edgeTokens;
    }

    /**
     * @param aEdgeTokens the edgeTokens to set
     */
    public void setEdgeTokens(String aEdgeTokens) {
        edgeTokens = aEdgeTokens;
    }
}
