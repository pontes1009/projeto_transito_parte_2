/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.sim;

/**
 *
 * @author Vinicius
 */
import de.tudresden.sumo.objects.SumoColor;
import it.polito.appeal.traci.SumoTraciConnection;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Company extends Thread {

    private ServerSocket serverSocket;
    private List<Route> routes;
    private AlphaBank alphaBank;
    private List<Car> cars;
    private List<Driver> drivers;
    private ObjectInputStream objectInputStream;
    private int numCars;
    private SumoTraciConnection sumo;
    private static Semaphore semaforoRota1 = new Semaphore(1);
    private static Semaphore semaforoRota2 = new Semaphore(1);

    public Company(List<Route> routes, AlphaBank alphaBank, int numCars) {
        this.routes = routes;
        this.alphaBank = alphaBank;
        this.numCars = numCars;
        this.cars = new ArrayList<>();
        this.drivers = new ArrayList<>();
        inicializa();
        distributeRoutesToDrivers();
    }

    public void startCarsAndDrivers() {
        int id = 1;
        int id2 = 1;
        System.out.println("Numero de motoristas: " + drivers.size());
        for (Driver driver : drivers) {
            //Thread carThread = new Thread(driver.getCar());
            Thread driverThread = new Thread(driver);
            //carThread.start();
            driver.getNextRoute();
            driverThread.start();

            if (driver.getCurrentRoute() == null) {
//                System.out.println("Nulo " + id2);
                //System.out.println(driver.getCar().getRoutes().size()+" mmmm");
                id2++;
            } else {
                if (driver.getCurrentRoute().getEdgeTokens() != null && id < 12) {
//                    System.out.println("rodando" + id);
//                    System.out.println(driver.getCurrentRoute().getEdgeTokens());
//                    System.out.println("rodando" + id);
                    Itinerary i1 = new Itinerary(driver.getCurrentRoute().getEdgeTokens(), id + "");
//                    System.out.println(driver.getCurrentRoute().getEdgeTokens());
                    id++;
                    if (i1.isOn() && id > 2) {
                        try {
                            String cor;

                            switch (id % 5) {
                                case 0:
                                    cor = "azul";
                                    break;
                                case 1:
                                    cor = "vermelho";
                                    break;
                                case 2:
                                    cor = "verde";
                                    break;
                                case 3:
                                    cor = "amarelo";
                                    break;
                                default:
                                    cor = "roxo";
                                    break;
                            }
//                            System.out.println("Iniciando Carro " + driver.getCar().getCarId());
                            startTransportService(i1, driver.getCar().getCarId(), driver.getName(), cor, sumo, driver);
                        } catch (Exception ex) {
                            Logger.getLogger(Company.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
//                    System.out.println("Edge nulo " + id);
                }

            }

        }
    }

    private void inicializa() {
        for (int i = 0; i < numCars; i++) {
            Car car = new Car("Car_" + (i + 1));
            Driver driver = new Driver("Driver_" + i, "Pass_" + i, alphaBank, car);
            car.setNomeMotorista(driver.getUsername());
            cars.add(car);
            drivers.add(driver);
        }
    }

    @Override
    public void run() {
        /* SUMO */
        String sumo_bin = "sumo-gui";
        String config_file = "map/map.sumo.cfg";

        // Sumo connection
        this.sumo = new SumoTraciConnection(sumo_bin, config_file);
        sumo.addOption("start", "1"); // auto-run on GUI show
        //sumo.addOption("quit-on-end", "1"); // auto-close on end-
        try {
            sumo.runServer(12345);
        } catch (IOException ex) {
            Logger.getLogger(Company.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Inicia o servidor para aceitar conexões dos carros
        try {
            serverSocket = new ServerSocket(8555); // Substitua pela porta desejada
            startCarsAndDrivers(); // Inicia os carros e motoristas

            while (true) {
                Socket clientSocket = serverSocket.accept();
                // Crie um novo objeto Thread para lidar com este cliente
                Thread carThread = new Thread(new CarHandler(clientSocket));
                // Inicie a thread do carro
                carThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void distributeRoutesToDrivers() {
        int totalDrivers = cars.size();
        int routesPerDriver = routes.size() / totalDrivers;

        int currentDriverIndex = 0;
        for (int i = 0; i < routes.size(); i++) {
            Car currentCar = cars.get(currentDriverIndex);
            currentCar.addRoute(routes.get(i));
            currentDriverIndex = (currentDriverIndex + 1) % totalDrivers;
        }
    }

    private static class CarHandler implements Runnable {

        private Socket clientSocket;

        public CarHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                // Inicialize fluxos de entrada e saída para o cliente
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

                // Lógica para lidar com a comunicação com o carro
                String message = (String) objectInputStream.readObject();
                //System.out.println("Mensagem recebida do cliente: " + message);

                // Responder ao cliente (se necessário)
                outputStream.writeObject("Mensagem recebida com sucesso!");

                // Feche os fluxos de entrada e saída e o socket quando não for mais necessário
                outputStream.close();
                objectInputStream.close();
                clientSocket.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private static void startTransportService(Itinerary itinerary, String carName, String driverName, String color, SumoTraciConnection sumo, Driver driver) throws IOException, InterruptedException {
        try {
            // Obtenha o semáforo correspondente à rota atual
            Semaphore semaforoAtual = (itinerary.equals(itinerary)) ? semaforoRota1 : semaforoRota2;

            semaforoAtual.acquire();

            // fuelType: 1-diesel, 2-gasoline, 3-ethanol, 4-hybrid
            int fuelType = 2;
            int fuelPreferential = 2;
            double fuelPrice = 3.40;
            int personCapacity = 1;
            int personNumber = 1;

            SumoColor carColor = getColorFromString(color);

            Auto car = new Auto(true, carName, carColor, driverName, sumo, 500, fuelType, fuelPreferential, fuelPrice, personCapacity, personNumber, driver);
            TransportService transportService = new TransportService(true, carName, itinerary, car, sumo);

            Thread itineraryThread = new Thread(() -> {
                try {
                    transportService.start();
                    Thread.sleep(5000);
                    car.start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaforoAtual.release();
                }
            });

            itineraryThread.start();
        } catch (Exception ex) {
            Logger.getLogger(EnvSimulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static SumoColor getColorFromString(String color) {
        switch (color.toLowerCase()) {
            case "verde":
                return new SumoColor(0, 255, 0, 126);
            case "vermelho":
                return new SumoColor(255, 0, 0, 126);
            case "azul":
                return new SumoColor(0, 0, 255, 126);
            case "amarelo":
                return new SumoColor(255, 255, 0, 126);
            case "roxo":
                return new SumoColor(128, 0, 128, 126);
            default:
                return new SumoColor(0, 0, 0, 126);
        }
    }
}
