package es.um.tds.gestionGastos.interfaz;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;
import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class ControladorDashboard {

    @FXML private Label lblTotalMes;
    @FXML private Label lblPromedioDiario;
    @FXML private Label lblCategoriaTop;
    @FXML private PieChart pieChartGastos;
    @FXML private BarChart<String, Number> barChartGastos;

    private ControladorPrincipal controlador = ControladorPrincipal.getInstancia();

    @FXML
    public void initialize() {
        controlador.getGastosObservable().addListener((ListChangeListener<Gasto>) change -> {
            actualizarDashboard();
        });
        actualizarDashboard();
    }

    private void actualizarDashboard() {
        List<Gasto> gastos = controlador.getGastosObservable();
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        LocalDate finMes = hoy.withDayOfMonth(hoy.lengthOfMonth());

        // Gasto total del mes
        List<Gasto> gastosMes = gastos.stream()
            .filter(g -> !g.getFecha().isBefore(inicioMes) && !g.getFecha().isAfter(finMes))
            .collect(Collectors.toList());
        double totalMes = gastosMes.stream().mapToDouble(Gasto::getCantidad).sum();
        lblTotalMes.setText(String.format("%.2f EUR", totalMes));

        // Promedio Diario
        int diasMes = hoy.lengthOfMonth();
        double promedio = diasMes > 0 ? totalMes / diasMes : 0.0;
        lblPromedioDiario.setText(String.format("%.2f EUR", promedio));

        // Categoría Top (histórica)
        Map<Categoria, Double> gastosPorCategoria = gastos.stream()
            .collect(Collectors.groupingBy(Gasto::getCategoria, Collectors.summingDouble(Gasto::getCantidad)));
        String catTop = "N/A";
        double maxGasto = -1;
        for (Map.Entry<Categoria, Double> entry : gastosPorCategoria.entrySet()) {
            if (entry.getValue() > maxGasto) {
                maxGasto = entry.getValue();
                catTop = entry.getKey().getNombre();
            }
        }
        lblCategoriaTop.setText(catTop);

        // Pie Chart
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (Map.Entry<Categoria, Double> entry : gastosPorCategoria.entrySet()) {
            if (entry.getValue() > 0) {
                pieData.add(new PieChart.Data(entry.getKey().getNombre(), entry.getValue()));
            }
        }
        pieChartGastos.setData(pieData);

        // Bar Chart (Últimas 4 semanas)
        barChartGastos.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Gastos Recientes");

        for (int i = 3; i >= 0; i--) {
            LocalDate finSemana = hoy.minusWeeks(i);
            LocalDate inicioSemana = finSemana.minusDays(6);

            double sumaSemana = gastos.stream()
                .filter(g -> !g.getFecha().isBefore(inicioSemana) && !g.getFecha().isAfter(finSemana))
                .mapToDouble(Gasto::getCantidad).sum();
            String label = inicioSemana.getDayOfMonth() + "/" + inicioSemana.getMonthValue() + 
                           " - " + finSemana.getDayOfMonth() + "/" + finSemana.getMonthValue();
            series.getData().add(new XYChart.Data<>(label, sumaSemana));
        }
        barChartGastos.getData().add(series);
    }
}