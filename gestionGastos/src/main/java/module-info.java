module es.um.tds.gestionGastos {
	requires javafx.controls;
	requires javafx.fxml;

	opens es.um.tds.gestionGastos to javafx.fxml;

	exports es.um.tds.gestionGastos;
}
