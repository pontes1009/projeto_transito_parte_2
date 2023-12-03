/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.sim;

import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer;

public class OtimizacaoViagem {

    public static void main(String[] args) {
        // Dados de exemplo
        double[] velocidades = {40, 50, 60, 55, 45, 50, 55, 65, 70, 60};
        double[] consumos = {0.1, 0.12, 0.15, 0.13, 0.18, 0.13, 0.17, 0.16, 0.17, 0.18};
        double[] gastos = {0.01, 0.01, 0.03, 0.04, 0.05, 0.04, 0.07, 0.07, 0.08, 0.09};

        // Limites de velocidade em cada ponto
        double velocidadeLimiteMin = 40;
        double velocidadeLimiteMax = 80;

        // Tempo total máximo de viagem
        double tempoTotalMaximo = 30; // exemplo: 30 horas

        // Chama a função de otimização
        otimizarViagem(velocidades, consumos, gastos, velocidadeLimiteMin, velocidadeLimiteMax, tempoTotalMaximo);
    }

    public static void otimizarViagem(double[] velocidades, double[] consumos, double[] gastos,
                                      double velocidadeLimiteMin, double velocidadeLimiteMax, double tempoTotalMaximo) {

        // Verificações para evitar valores nulos ou arrays de tamanhos diferentes
        if (velocidades == null || consumos == null || gastos == null ||
            velocidades.length != consumos.length || consumos.length != gastos.length) {
            throw new IllegalArgumentException("Os arrays de entrada não podem ser nulos e devem ter o mesmo tamanho.");
        }

        try {
            // Função objetivo (minimizar o custo total)
            LinearObjectiveFunction funcaoObjetivo = new LinearObjectiveFunction(gastos, 0);

            // Solucionador Simplex
            SimplexOptimizer optimizer = new SimplexOptimizer(1e-6, 1e-6);

            // Restrições: limites de velocidade em cada ponto
            LinearConstraint[] restricoesVelocidade = new LinearConstraint[velocidades.length * 2];
            int constraintIndex = 0;
            for (int i = 0; i < velocidades.length; i++) {
                restricoesVelocidade[constraintIndex++] = new LinearConstraint(new double[]{1}, Relationship.GEQ, velocidadeLimiteMin);
                restricoesVelocidade[constraintIndex++] = new LinearConstraint(new double[]{1}, Relationship.LEQ, velocidadeLimiteMax);
            }

            // Restrição: tempo total de viagem
            LinearConstraint restricaoTempo = new LinearConstraint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, Relationship.LEQ, tempoTotalMaximo);

            // Otimização
            org.apache.commons.math3.optim.PointValuePair resultado = optimizer.optimize(
                    new ObjectiveFunction(funcaoObjetivo),
                    new org.apache.commons.math3.optim.linear.LinearConstraintSet(restricoesVelocidade),
                    GoalType.MINIMIZE
            );

            // Exibe os resultados
            System.out.println("Resultados da otimização:");
            System.out.println("Custo mínimo: " + resultado.getValue());

            double[] velocidadesOtimizadas = resultado.getPoint();
            for (int i = 0; i < velocidadesOtimizadas.length; i++) {
                System.out.println("Velocidade no ponto " + (i + 1) + ": " + velocidadesOtimizadas[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
