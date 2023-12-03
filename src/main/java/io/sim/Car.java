/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.sim;
import java.awt.Point;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import de.tudresden.sumo.cmd.Vehicle;

public class Car extends Vehicle {
    private String carId;
    private List<CarObserver> observers;
    private List<Route> routes;
    private double fuelTank; // Atributo privado para o tanque de combustível
    private ObjectOutputStream outputStream; // Para enviar dados para a Company
    private static final double INITIAL_FUEL = 10.0; // Valor inicial do tanque de combustível
    private double lastRead;
    private String nomeMotorista;
    public Car(String carId) {
        this.carId = carId;
        this.observers = new ArrayList<>();
        this.routes = new ArrayList<>();
        this.fuelTank = INITIAL_FUEL; // Inicializa o tanque de combustível      
        this.setLastRead(0);
    }

    public void addCarObserver(CarObserver observer) {
        observers.add(observer);
    }

    public void removeCarObserver(CarObserver observer) {
        observers.remove(observer);
    }

    public void moveTo(Point nextPoint) {
        System.out.println("Carro se movendo para o ponto: " + nextPoint);
        // Lógica para mover o carro para o próximo ponto, se necessário
    }

    public void executeRoute(Route route) {
        // Lógica para percorrer a rota
        for (Point point : route.getPoints()) {
            // Realiza ações nos pontos da rota
            // Implemente a lógica aqui
            this.setFuelTank(this.getFuelTank()-1);
            try {
                Thread.sleep(500);  // Simula o tempo que leva para percorrer um ponto
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Após percorrer a rota, notifica os observadores que a rota foi concluída
        notifyObservers(route);
    }

    private void notifyObservers(Route route) {
        for (CarObserver observer : observers) {
            observer.onRouteCompleted(this, route);
        }
    }

    public String getCarId() {
        return carId;
    }

    public double getFuelTank() {
        return fuelTank;
    }

    public void setFuelTank(double fuelTank) {
        //System.out.println("Gasolina = "+this.fuelTank);
        this.fuelTank = fuelTank;
    }

    public void addRoute(Route route) {
        getRoutes().add(route);
    }

    private void sendDataToCompany() {
        // Envia os dados do carro para a Company
//        try {
            // Cria um objeto para enviar dados (exemplo: uma string)
            String data = "Dados do carro " + carId;
            System.out.println(data);
//            getOutputStream().writeObject(data);
//            getOutputStream().flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * @return the routes
     */
    public List<Route> getRoutes() {
        return routes;
    }

    /**
     * @param routes the routes to set
     */
    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    /**
     * @return the outputStream
     */
    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * @param outputStream the outputStream to set
     */
    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * @return the nomeMotorista
     */
    public String getNomeMotorista() {
        return nomeMotorista;
    }

    /**
     * @param nomeMotorista the nomeMotorista to set
     */
    public void setNomeMotorista(String nomeMotorista) {
        this.nomeMotorista = nomeMotorista;
    }

    /**
     * @return the lastRead
     */
    public double getLastRead() {
        return lastRead;
    }

    /**
     * @param lastRead the lastRead to set
     */
    public void setLastRead(double lastRead) {
        this.lastRead = lastRead;
    }
}
