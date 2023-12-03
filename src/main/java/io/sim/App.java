package io.sim;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Hello world!
 *
 */
public class App {
    
    public static void main(String[] args) {
        File file = new File("relatorio.csv");
        if(file.exists()){
            file.delete();
        }
        File file2 = new File("relatorio2.csv");
        if(file2.exists()){
            file2.delete();
        }
        List<Route> routes = generateRoutes(1);
        //List<Car> cars = generateCars(100);
        AlphaBank alphaBank = new AlphaBank();
        Company company = new Company(routes, alphaBank, 2);

        // Inicie a thread da empresa
        company.start();
    }

    private static List<Route> generateRoutes(int numberOfRoutes) {
        List<Route> routes = new ArrayList<>();
        String filePath = "data/dados3.xml";
        List<String> edgeStrings = parseRoutesFromXML(filePath);
        if (edgeStrings.size() > 5) {
            for (String edgeString : edgeStrings) {
                List<Point> points = RouteParser.parseRoute(edgeString);
                Route route = new Route(points, edgeString);
                routes.add(route);
            }
        }

        return routes;
    }

    public static List<String> parseRoutesFromXML(String filePath) {
        List<String> edgeStrings = new ArrayList<>();

        try {
            File file = new File(filePath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("vehicle");

            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node node = nodeList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    // Obtém o elemento <route> dentro do elemento <vehicle>
                    Node routeNode = element.getElementsByTagName("route").item(0);
                    if (routeNode != null && routeNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element routeElement = (Element) routeNode;
                        String edges = routeElement.getAttribute("edges");
                        if (!edges.isEmpty()) {
                            edgeStrings.add(edges);
                        } else {
                            System.out.println("Atributo 'edges' vazio para o veículo com ID: " + element.getAttribute("id"));
                        }
                    } else {
                        System.out.println("Elemento <route> não encontrado para o veículo com ID: " + element.getAttribute("id"));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return edgeStrings;
    }

}
/*


package io.sim;

public class App {
    public static void main( String[] args ) {

        EnvSimulator ev = new EnvSimulator();
        ev.start();

    }
}*/
