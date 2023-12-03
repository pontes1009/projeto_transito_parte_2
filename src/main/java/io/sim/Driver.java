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
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Driver extends Thread {

    private String username;
    private String password;
    private Account account;
    private Car car;
    private AlphaBank alphaBank;
    private Route currentRoute;
    private List<Route> executadas;

    public Driver(String username, String password, AlphaBank alphaBank, Car car) {
        this.username = username;
        this.password = password;
        this.alphaBank = alphaBank;
        this.account = new Account(username, password);
        this.car = car;
    }

    public void setRoute(Route route) {
        this.setCurrentRoute(route);
    }

    @Override
    public void run() {
//        getNextRoute();
//        while (getCurrentRoute() != null) {
//            // Simula o motorista percorrendo a rota
//            // Cada iteração representa um ponto na rota
//            System.out.println("dirigindo "+getCar().getNomeMotorista());
//            Point nextPoint = getCurrentRoute().getNextPoint();
//            if (nextPoint != null) {
//                try {
//                    getCar().moveTo(nextPoint);
//                    int distance = ThreadLocalRandom.current().nextInt(1, 10); // Distância percorrida em km
//                    int payment = distance * 587; // Pagamento para a Fuel Station em centavos
//                    FuelStationPaymentBot paymentBot = new FuelStationPaymentBot(payment);
//                    paymentBot.start();
//                    alphaBank.processTransaction(account, alphaBank.getFuelStationAccount(), payment);
//                    System.out.println("Motorista " + getUsername() + " percorreu " + distance + " km.");
//                    
//                } catch (IOException ex) {
//                    Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            } else {
//                System.out.println("Motorista " + getUsername() + " chegou ao destino.");
//                break;
//            }
//
//            // Ao finalizar a rota atual, adicionar à lista executadas
//            if (nextPoint == null) {
//                executadas.add(getCurrentRoute());
//            }
//
//            // Obter a próxima rota do carro
//            getNextRoute();
//        }
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the car
     */
    public Car getCar() {
        return car;
    }

    /**
     * @param car the car to set
     */
    public void setCar(Car car) {
        this.car = car;
    }

    public void getNextRoute() {
        // Verificar se ainda há rotas disponíveis no carro
        if (!getCar().getRoutes().isEmpty()) {
            // Obter a próxima rota do carro
            Route proximaRota = getCar().getRoutes().remove(0);

            // Armazenar a rota em currentRoute
            setRoute(proximaRota);
            System.out.println("Pegou uma nova rota");
        } else {
            // Não há mais rotas disponíveis para este carro
            System.out.println("Sem rotas disponíveis");
            setRoute(null);
        }
    }

    /**
     * @return the currentRoute
     */
    public Route getCurrentRoute() {
        return currentRoute;
    }

    /**
     * @param currentRoute the currentRoute to set
     */
    public void setCurrentRoute(Route currentRoute) {
        this.currentRoute = currentRoute;
    }
    public void abastecer() {
        // ... (lógica para abastecer o carro com a quantidade de litros especificada)
        int quantidade = this.getAccount().getBalance()/587;
        if(quantidade > 45){
            quantidade  = 45;
        }
        // Calcular o valor total a ser pago à Fuel Station (R$5,87 por litro)
        int totalPayment = quantidade * 587;
        this.getCar().setFuelTank(this.getCar().getFuelTank()+quantidade);
        // Inicializar e iniciar um novo BotPayment para realizar o pagamento
        BotPayment botPayment = new BotPayment(alphaBank, getAccount(), alphaBank.getFuelStationAccount(), totalPayment);
        botPayment.start();
        
        // ... (restante do código para abastecer o carro)
    }
    public void receberPagamento(int quilometrosRodados) {
        try {
            // Calcular o valor total a ser pago ao motorista (R$3,25 por quilômetro rodado)
            int totalPayment = quilometrosRodados * 325;
            
            // Realizar o pagamento ao motorista usando alphaBank.processTransaction()
            // Certifique-se de implementar esse método corretamente na classe AlphaBank.
            alphaBank.processTransaction(alphaBank.getAccount("Company"), getAccount(), totalPayment);
            
           // System.out.println("Motorista recebeu pagamento de R$" + totalPayment / 100.0 + " por percorrer " + quilometrosRodados + " km.");
        } catch (IOException ex) {
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the account
     */
    public Account getAccount() {
        return account;
    }
}
