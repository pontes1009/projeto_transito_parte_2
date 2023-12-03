/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.sim;

/**
 *
 * @author Vinicius
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AlphaBank {

    private Map<String, Account> accounts;
    private Account fuelStationAccount; // Conta da estação de combustível
    Account companyAccount;

    public AlphaBank() {
        this.accounts = new HashMap<>();
        this.fuelStationAccount = new Account("FuelStation", "fuel123"); // Cria uma conta para a estação de combustível
        this.companyAccount = new Account("Company", "company123"); // Cria uma conta para a estação de combustível
        companyAccount.setBalance(10000000);
        accounts.put("FuelStation", fuelStationAccount);
        accounts.put("Company", companyAccount);
    }

    public void createAccount(String username, String password) {
        if (!accounts.containsKey(username)) {
            Account account = new Account(username, password);
            accounts.put(username, account);
        } else {
            System.out.println("Nome de usuário já existente.");
        }
    }
    public void processTransaction(Account senderAccount, Account receiverAccount, int amount) throws IOException {
        if (senderAccount.getBalance() >= amount) {
            senderAccount.withdraw(amount);
            receiverAccount.deposit(amount);
            //System.out.println("Transação de R$" + (amount / 100.0) + " realizada de " + senderAccount.getUsername() + " para " + receiverAccount.getUsername());
        } else {
            System.out.println("Saldo insuficiente para a transação de " + senderAccount.getUsername());
        }
    }
    public Account getFuelStationAccount() {
        return fuelStationAccount;
    }

    public Account getAccount(String username) {
        return accounts.get(username);
    }
}
