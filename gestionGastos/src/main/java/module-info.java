module es.um.tds.gestionGastos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens es.um.tds.gestionGastos to javafx.fxml;
    opens es.um.tds.gestionGastos.Controladores to javafx.fxml;
    opens es.um.tds.gestionGastos.interfaz to javafx.fxml;
    opens es.um.tds.gestionGastos.modelo to javafx.base, com.fasterxml.jackson.databind;
    opens es.um.tds.gestionGastos.modelo.CuentaCompartida to com.fasterxml.jackson.databind;
    opens es.um.tds.gestionGastos.modelo.Alertas to com.fasterxml.jackson.databind;

    exports es.um.tds.gestionGastos;
    exports es.um.tds.gestionGastos.modelo;
    exports es.um.tds.gestionGastos.modelo.CuentaCompartida;
    exports es.um.tds.gestionGastos.modelo.Alertas;
    exports es.um.tds.gestionGastos.modelo.persistencia;
    exports es.um.tds.gestionGastos.Controladores;
    exports es.um.tds.gestionGastos.interfaz;
}
