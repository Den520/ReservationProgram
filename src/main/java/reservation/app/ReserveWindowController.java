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

    String paternFormatTime = "[0-9]{2}:[0-9]{2}";

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

            int indicator_of_error = 0;

            if (Objects.equals(event_name.getText(), "")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Упс, кажется вы не ввели название мероприятия");
                alert.setContentText("Введите, например, \"Главное событие дня\".");
                alert.showAndWait();
                indicator_of_error = 1;
            }

            if (Objects.equals(participants_list.getText(), "")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Упс, кажется вы никого не позвали на ваше мероприятие...или вы собираетесь разговаривать с самим собой?");
                alert.setContentText("Раздайте приглашения и введите участников.");
                alert.showAndWait();
                indicator_of_error = 1;
            }

            if (!day_field.getText().matches("[0-9]+")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Что-то не так с датой мероприятия...");
                alert.setContentText("Пожалуйста, введите любое число месяца, которое входит в наш календарь (от 1 до 28).");
                alert.showAndWait();
                indicator_of_error = 1;
            }
            else {
                int day = Integer.parseInt(day_field.getText());
                if (!(day >= 1 & day <= 28)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Что-то не так с датой мероприятия...");
                    alert.setContentText("Пожалуйста, введите любое число месяца, которое входит в наш календарь (от 1 до 28).");
                    alert.showAndWait();
                    indicator_of_error = 1;
                }
            }

            if (!(begin_time.getText().matches(paternFormatTime) & end_time.getText().matches(paternFormatTime))) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Упс, кажется вы ошиблись с форматом времени.");
                alert.setContentText("Время должно быть введено в формате (чч:мм). Например, начало в 05:30, а конец в 10:00.");
                alert.showAndWait();
                indicator_of_error = 1;
            }

            String[] b = begin_time.getText().split(":");
            String[] e = end_time.getText().split(":");

            int begin = Integer.parseInt(b[0]) * 60 + Integer.parseInt(b[1]);
            int end = Integer.parseInt(e[0]) * 60 + Integer.parseInt(e[1]);

            if (end - begin < 30 || end - begin > 1440) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Вы точно прочитали, что написано внизу?");
                alert.setContentText("Минимальный промежуток бронирования - 30 минут, максимальный - 24 часа.");
                alert.showAndWait();
                indicator_of_error = 1;
            }

            int select_week = 0;
            int X_layout = 0;
            double Y_layout = 0;
            double btnHeight = 0;

            if (indicator_of_error == 0) {
                int day_of_week = Integer.parseInt(day_field.getText());
                select_week = 1;
                while (day_of_week > 7) {
                    day_of_week -= 7;
                    select_week++;
                }
                X_layout = 107 + (day_of_week - 1) * 125;
                Y_layout = 50 + begin * 0.5;
                btnHeight = (end - begin) * 0.5;

                int X_point = X_layout + 50;
                double Y_point = Y_layout;

                try {
                    controller.saveAsPng(controller.getPane(select_week));
                    File file = new File("my_snapshot.png");
                    BufferedImage image = ImageIO.read(file);
                    int placeIsEmpty = 1;
                    while (Y_point < Y_layout + btnHeight) {
                        placeIsEmpty *= image.getRGB(X_point, Math.toIntExact(Math.round(Y_point)));
                        placeIsEmpty *= image.getRGB(X_point, Math.toIntExact(Math.round(Y_point)) + 1);
                        Y_point += 15;
                    }
                    file.delete();
                    if (Math.abs(placeIsEmpty) != 1) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Время уже занято");
                        alert.setContentText("Пожалуйста, измените время мероприятия");
                        alert.showAndWait();
                        indicator_of_error = 1;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if (indicator_of_error == 0) {
                Button btn = new Button(event_name.getText());
                ButtonData button = new ButtonData(btn, event_name.getText(), participants_list.getText(), day_field.getText(), select_week, begin_time.getText(), begin, end_time.getText(), end);
                controller.getPane(select_week).getChildren().add(btn);
                btn.setLayoutX(X_layout);
                btn.setLayoutY(Y_layout);
                btn.setPrefSize(124, btnHeight);
                controller.getHashMap().put(controller.getHashMap().size() + 1, button);

                btn.setOnAction(eventForButton -> controller.detailWindow(button));

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
