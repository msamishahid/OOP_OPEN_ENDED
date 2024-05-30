module com.labtasks.ooplab14 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.labtasks.ooplab14 to javafx.fxml;
    exports com.labtasks.ooplab14;
}