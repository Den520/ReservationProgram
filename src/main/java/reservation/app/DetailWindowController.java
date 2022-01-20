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

public class DetailWindowController {

    MainAppController controller;
    public void setMainController(MainAppController controller) {
        this.controller = controller;
    }

    String paternFormatTime = "[0-9]{2}:[0-9]{2}";

    ButtonData current_button;

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
    private Button delete_button;

    @FXML
    void initialize() {
        reserve_button.setOnAction(event -> {
            current_button.getButton_obj().setVisible(false);
            if (controller.createEvent(current_button, event_name.getText(), participants_list.getText(), day_field.getText(), begin_time.getText(), end_time.getText())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успех!");
                alert.setHeaderText("Ваше мероприятие успешно отредактировано!");
                alert.setContentText(null);
                alert.showAndWait();
                reserve_button.getScene().getWindow().hide();
            }
            controller.filterResetButton();
        });

        delete_button.setOnAction(event -> {
            controller.getPane(current_button.getWeek()).getChildren().remove(current_button.getButton_obj());
            controller.getHashMap().remove(current_button.getDay() + " " + current_button.getBegin_time());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Успех!");
            alert.setHeaderText("Ваше мероприятие успешно удалено!");
            alert.setContentText(null);
            alert.showAndWait();
            delete_button.getScene().getWindow().hide();
        });
    }
    void initDataOfButtons(ButtonData button) {
        event_name.setText(button.getEvent_name());
        participants_list.setText(button.getParticipants_list());
        day_field.setText(button.getDay());
        begin_time.setText(button.getBegin_time());
        end_time.setText(button.getEnd_time());
        current_button = button;
    }
}