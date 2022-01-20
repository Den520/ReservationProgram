package reservation.app;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ReserveWindowController {

    MainAppController controller;
    public void setMainController(MainAppController controller) {
        this.controller = controller;
    }

    @FXML
    private TextField day_field;

    @FXML
    private TextField begin_time;

    @FXML
    private TextField end_time;

    @FXML
    private TextField event_name;

    @FXML
    private TextField participants_list;

    @FXML
    private Button reserve_button;

    @FXML
    void initialize() {
        reserve_button.setOnAction(event -> {
            if (controller.createEvent(null, event_name.getText(), participants_list.getText(), day_field.getText(), begin_time.getText(), end_time.getText())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успех!");
                alert.setHeaderText("Ваше мероприятие успешно добавлено!");
                alert.setContentText(null);
                alert.showAndWait();
                reserve_button.getScene().getWindow().hide();
            }
        });
    }
}
