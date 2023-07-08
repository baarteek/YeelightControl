module com.example.yeelightcontrol {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires closure.compiler.v20210601;


    opens com.example.yeelightcontrol to javafx.fxml;
    exports com.example.yeelightcontrol;
}