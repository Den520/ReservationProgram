package reservation.app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class MainAppController {

    public void reserveWindow() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("reserve-window.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Бронирование переговорки");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(page));

            ReserveWindowController controller = loader.getController();
            controller.setMainController(this);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void detailWindow(ButtonData button) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HelloApplication.class.getResource("detail-window.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Детали мероприятия");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(page));

            DetailWindowController controller = loader.getController();
            controller.initDataOfButtons(button);
            controller.setMainController(this);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean createEvent(ButtonData current_button, String event_name, String participants_list, String day_field, String begin_time, String end_time) {
        int indicator_of_error = 0;

        if (Objects.equals(event_name, "")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Упс, кажется вы не ввели название мероприятия");
            alert.setContentText("Введите, например, \"Главное событие дня\".");
            alert.showAndWait();
            indicator_of_error = 1;
        }

        if (Objects.equals(participants_list, "")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Упс, кажется вы никого не позвали на ваше мероприятие...или вы собираетесь разговаривать с самим собой?");
            alert.setContentText("Раздайте приглашения и введите участников.");
            alert.showAndWait();
            indicator_of_error = 1;
        }

        if (!day_field.matches("[0-9]+")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Что-то не так с датой мероприятия...");
            alert.setContentText("Пожалуйста, введите любое число месяца, которое входит в наш календарь (от 1 до 28).");
            alert.showAndWait();
            indicator_of_error = 1;
        }
        else {
            int day = Integer.parseInt(day_field);
            if (!(day >= 1 & day <= 28)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Что-то не так с датой мероприятия...");
                alert.setContentText("Пожалуйста, введите любое число месяца, которое входит в наш календарь (от 1 до 28).");
                alert.showAndWait();
                indicator_of_error = 1;
            }
        }

        if (!(begin_time.matches(patternFormatTime) & end_time.matches(patternFormatTime))) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Упс, кажется вы ошиблись с форматом времени.");
            alert.setContentText("Время должно быть введено в формате (чч:мм). Например, начало в 05:30, а конец в 10:00.");
            alert.showAndWait();
            indicator_of_error = 1;
        }

        String[] b = begin_time.split(":");
        String[] e = end_time.split(":");

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
            int day_of_week = Integer.parseInt(day_field);
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
                saveAsPng(getPane(select_week));
                File file = new File("my_snapshot.png");
                BufferedImage image = ImageIO.read(file);
                int placeIsEmpty = 1;
                while (Y_point < Y_layout + btnHeight) {
                    placeIsEmpty *= image.getRGB(X_point, Math.toIntExact(Math.round(Y_point)));
                    placeIsEmpty *= image.getRGB(X_point, Math.toIntExact(Math.round(Y_point)) + 1);
                    Y_point += 1;
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
            if (current_button != null) {
                getPane(current_button.getWeek()).getChildren().remove(current_button.getButton_obj());
                getHashMap().remove(Integer.parseInt(current_button.getDay()) + " " + current_button.getBegin());
            }
            Button btn = new Button(event_name);
            ButtonData button = new ButtonData(btn, event_name, participants_list, day_field, select_week, begin_time, begin, end_time, end);
            getPane(select_week).getChildren().add(btn);
            btn.setLayoutX(X_layout);
            btn.setLayoutY(Y_layout);
            btn.setPrefSize(124, btnHeight);
            getHashMap().put(day_field + " " + begin_time, button);

            btn.setOnAction(eventForButton -> detailWindow(button));
            return true;
        }
        return false;
    }

    public HashMap getHashMap() {return listOfButtons;}

    public AnchorPane getPane(int week) {
        return switch (week) {
            case 1 -> pane_week_1;
            case 2 -> pane_week_2;
            case 3 -> pane_week_3;
            case 4 -> pane_week_4;
            default -> null;
        };
    }

    public void filterResetButton() {
        filterResetButton.fire();
    }

    public void saveAsPng(Node node) throws IOException {
        getPane(current_pane).setVisible(false);
        node.setVisible(true);
        saveAsPng(node, new SnapshotParameters());
        node.setVisible(false);
        getPane(current_pane).setVisible(true);
    }

    public void saveAsPng(Node node, SnapshotParameters ssp) throws IOException {
        WritableImage image = node.snapshot(ssp, null);
        File file = new File("my_snapshot.png");
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
    }

    int current_pane = 1;
    String patternFormatTime = "[0-9]{2}:[0-9]{2}";
    private final HashMap<String, ButtonData> listOfButtons = new HashMap<>();

    @FXML
    private Button createEventButton;

    @FXML
    private Button filterApplyButton;

    @FXML
    private Button filterResetButton;

    @FXML
    private Button last_week_button;

    @FXML
    private Button next_week_button;

    @FXML
    private AnchorPane pane_week_1;

    @FXML
    private AnchorPane pane_week_2;

    @FXML
    private AnchorPane pane_week_3;

    @FXML
    private AnchorPane pane_week_4;

    @FXML
    private TextField beginFilter;

    @FXML
    private TextField endFilter;

    @FXML
    void initialize() {
        createEvent(null,"Тестовое событие 1", "Мусаев Тариэль, Шишкин Вадим, Варкентин Мария", "2", "10:00", "12:00");
        createEvent(null,"Тестовое событие 2", "Бадыгин Роман, Мусаев Тариэль, Насыров Сергей, Машков Никита", "10", "14:20", "15:10");

        createEventButton.setOnAction(event -> reserveWindow());

        filterApplyButton.setOnAction(event -> {
            if (!(beginFilter.getText().matches(patternFormatTime) & endFilter.getText().matches(patternFormatTime))) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Упс, кажется вы ошиблись с форматом времени.");
                alert.setContentText("Время должно быть введено в формате (чч:мм). Например, от 05:30 до 10:00.");
                alert.showAndWait();
            }
            else {
                String[] b = beginFilter.getText().split(":");
                String[] e = endFilter.getText().split(":");
                int begin = Integer.parseInt(b[0]) * 60 + Integer.parseInt(b[1]);
                int end = Integer.parseInt(e[0]) * 60 + Integer.parseInt(e[1]);

                listOfButtons.forEach((key, value) -> {
                    ButtonData current_button = listOfButtons.get(key);
                    if (current_button.getBegin() < begin || current_button.getBegin() > end || current_button.getEnd() < begin || current_button.getEnd() > end) {
                        current_button.getButton_obj().setVisible(false);
                    }
                });

                filterResetButton.setVisible(true);
                filterApplyButton.setDisable(true);
                createEventButton.setDisable(true);
            }
        });

        filterResetButton.setOnAction(event -> {
            filterResetButton.setVisible(false);
            filterApplyButton.setDisable(false);
            createEventButton.setDisable(false);

            listOfButtons.forEach((key, value) -> listOfButtons.get(key).getButton_obj().setVisible(true));

            beginFilter.clear();
            endFilter.clear();
        });

        last_week_button.setOnAction(event -> {
            switch (current_pane) {
                case 4 -> {
                    pane_week_4.setVisible(false);
                    pane_week_3.setVisible(true);
                    next_week_button.setVisible(true);
                }
                case 3 -> {
                    pane_week_3.setVisible(false);
                    pane_week_2.setVisible(true);
                }
                case 2 -> {
                    pane_week_2.setVisible(false);
                    pane_week_1.setVisible(true);
                    last_week_button.setVisible(false);
                }
            }
            current_pane--;
        });

        next_week_button.setOnAction(event -> {
            switch (current_pane) {
                case 1 -> {
                    pane_week_1.setVisible(false);
                    pane_week_2.setVisible(true);
                    last_week_button.setVisible(true);
                }
                case 2 -> {
                    pane_week_2.setVisible(false);
                    pane_week_3.setVisible(true);
                }
                case 3 -> {
                    pane_week_3.setVisible(false);
                    pane_week_4.setVisible(true);
                    next_week_button.setVisible(false);
                }
            }
            current_pane++;
        });
    }
}