/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.sim;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BotPayment extends Thread {
    private AlphaBank alphaBank;
    private Account driverAccount;
    private Account fuelStationAccount;
    private int paymentAmount;

    public BotPayment(AlphaBank alphaBank, Account driverAccount, Account fuelStationAccount, int paymentAmount) {
        this.alphaBank = alphaBank;
        this.driverAccount = driverAccount;
        this.fuelStationAccount = fuelStationAccount;
        this.paymentAmount = paymentAmount;
    }

    @Override
    public void run() {
        try {
            // Realize o pagamento à Fuel Station usando alphaBank.processTransaction()
            // Certifique-se de implementar esse método corretamente na classe AlphaBank.
            alphaBank.processTransaction(driverAccount, fuelStationAccount, paymentAmount);
            //System.out.println("Pagamento realizado à Fuel Station: R$" + paymentAmount / 100.0);
        } catch (IOException ex) {
            Logger.getLogger(BotPayment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
