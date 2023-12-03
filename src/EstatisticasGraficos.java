import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class EstatisticasGraficos {

    public static void main(String[] args) {
        // Dados de exemplo
        double[] velocidades = {40, 50, 60, 55, 45, 50, 55, 65, 70, 60};

        // Crie um gráfico de dispersão
        JFreeChart scatterChart = createScatterChart(velocidades);
        displayChart(scatterChart, "Gráfico de Dispersão");

        // Calcule e exiba as estatísticas descritivas
        calculateAndDisplayStatistics(velocidades);
    }

    private static JFreeChart createScatterChart(double[] data) {
        XYSeries series = new XYSeries("Pontos");
        for (int i = 0; i < data.length; i++) {
            series.add(i + 1, data[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);

        return ChartFactory.createScatterPlot(
                "Gráfico de Dispersão",
                "Ponto",
                "Velocidade",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );
    }

    private static void displayChart(JFreeChart chart, String title) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(title);
            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(560, 370));
            frame.setContentPane(chartPanel);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static void calculateAndDisplayStatistics(double[] data) {
        DescriptiveStatistics stats = new DescriptiveStatistics();

        for (double value : data) {
            stats.addValue(value);
        }

        // Média
        double media = stats.getMean();
        System.out.println("Média: " + media);

        // Desvio padrão
        double desvioPadrao = stats.getStandardDeviation();
        System.out.println("Desvio Padrão: " + desvioPadrao);

        // Polarização (bias)
        double polarizacao = stats.getMean() - stats.getPopulationVariance();
        System.out.println("Polarização (bias): " + polarizacao);

        // Precisão
        double precisao = 1 / stats.getStandardDeviation();
        System.out.println("Precisão: " + precisao);

        // Incerteza
        double incerteza = stats.getStandardDeviation() / Math.sqrt(stats.getN());
        System.out.println("Incerteza: " + incerteza);
    }
}
