module es.um.tds.gestionGastos {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    

    
    opens es.um.tds.gestionGastos.vista to javafx.fxml;
    opens es.um.tds.gestionGastos.modelo to javafx.base;
}