package es.um.tds.gestionGastos.Controladores;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class ControladorDashboard {

    @FXML private PieChart pieChartGastos;
    @FXML private BarChart<String, Number> barChartGastos;

    @FXML
    public void initialize() {
        // Para el PieChart
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
            new PieChart.Data("Alimentación", 450),
            new PieChart.Data("Transporte", 120),
            new PieChart.Data("Ocio", 200)
        );
        pieChartGastos.setData(pieData);

        // Para el BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Gastos 2025");
        series.getData().add(new XYChart.Data<>("Semana 1", 300));
        series.getData().add(new XYChart.Data<>("Semana 2", 450));
        barChartGastos.getData().add(series);
    }
    
    public void actualizarGraficos(String categoriaFiltro, double nuevoValor) {
        // Aquí iría tu lógica real de base de datos, por ahora simulamos:
        ObservableList<PieChart.Data> nuevosDatos = FXCollections.observableArrayList(
            new PieChart.Data(categoriaFiltro, nuevoValor),
            new PieChart.Data("Otros", 1000 - nuevoValor)
        );
        pieChartGastos.setData(nuevosDatos);
    }
}