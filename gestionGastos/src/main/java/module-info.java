module es.um.tds.gestionGastos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    
    opens es.um.tds.gestionGastos to javafx.fxml;
    opens es.um.tds.gestionGastos.interfaz to javafx.fxml;
    
    opens es.um.tds.gestionGastos.modelo to javafx.base;

    exports es.um.tds.gestionGastos;
    exports es.um.tds.gestionGastos.modelo;
    exports es.um.tds.gestionGastos.Controladores;
    exports es.um.tds.gestionGastos.interfaz;
}