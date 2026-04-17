module es.um.tds.gestionGastos {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;

	opens es.um.tds.gestionGastos to javafx.fxml;
	opens es.um.tds.gestionGastos.modelo to javafx.base;

	exports es.um.tds.gestionGastos;
	exports es.um.tds.gestionGastos.modelo;
	exports es.um.tds.gestionGastos.modelo.Alertas;
	exports es.um.tds.gestionGastos.modelo.CuentaCompartida;
	exports es.um.tds.gestionGastos.Controladores;
}