/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.sim;

/**
 *
 * @author vinicius
 */
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private String username;
    private String password;
    private int balance;
    private StringBuilder transactionHistory;
    private Lock balanceLock;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 0;
        this.transactionHistory = new StringBuilder();
        this.balanceLock = new ReentrantLock();
    }

    public boolean authenticate(String password) {
        return this.getPassword().equals(password);
    }

    public int getBalance() {
        return balance;
    }

    public void deposit(int amount) {
        getBalanceLock().lock();
        try {
            setBalance(balance + amount);
            transactionHistory.append("Depósito de ").append(amount).append(" em ").append(System.nanoTime()).append("\n");
        } finally {
            getBalanceLock().unlock();
        }
    }

    public void withdraw(int amount) {
        getBalanceLock().lock();
        try {
            if (balance >= amount) {
                setBalance(balance - amount);
                transactionHistory.append("Saque de ").append(amount).append(" em ").append(System.nanoTime()).append("\n");
            } else {
                System.out.println("Saldo insuficiente para a transação.");
            }
        } finally {
            getBalanceLock().unlock();
        }
    }

    public String getTransactionHistory() {
        return transactionHistory.toString();
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
     * @return the balanceLock
     */
    public Lock getBalanceLock() {
        return balanceLock;
    }

    /**
     * @param balanceLock the balanceLock to set
     */
    public void setBalanceLock(Lock balanceLock) {
        this.balanceLock = balanceLock;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(int balance) {
        this.balance = balance;
    }
}

