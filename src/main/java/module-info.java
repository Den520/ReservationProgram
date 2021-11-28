module reservation.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.common;
    requires javafx.swing;

    opens reservation.app to javafx.fxml;
    exports reservation.app;
}