/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.sim;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class RouteParser {

    public static List<Point> parseRoute(String edges) {
        String[] edgeTokens = edges.split(" ");
        List<Point> points = new ArrayList<>();

        for (String edgeToken : edgeTokens) {
            String cleanedEdgeToken = edgeToken.replaceAll("[^\\d-]", "");
            //System.out.println(edgeToken+" <<");
            if (cleanedEdgeToken != null && cleanedEdgeToken.matches("\\d+")) {
                try {
                    int edgeId = Integer.parseInt(cleanedEdgeToken);
                    Point point = new Point(edgeId, 0);
                    points.add(point);
                    //System.out.println(point.x);
                } catch (NumberFormatException e) {
                    // cleanedEdgeToken não é um número válido
//                    System.err.println("Erro ao analisar o token da aresta: " + cleanedEdgeToken);
                    // Você pode escolher lidar com o erro de acordo com a lógica do seu programa
                }
            } else {
                // cleanedEdgeToken é nulo ou não é um número válido
//                System.err.println("Token da aresta inválido: " + cleanedEdgeToken);
                // Você pode escolher lidar com o erro de acordo com a lógica do seu programa
            }
        }

        return points;
    }
}
