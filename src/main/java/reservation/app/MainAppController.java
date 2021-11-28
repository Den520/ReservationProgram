package reservation.app;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
    String paternFormatTime = "[0-9]{2}:[0-9]{2}";
    private final HashMap<Integer, ButtonData> listOfButtons = new HashMap<>();

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

        createEventButton.setOnAction(event -> reserveWindow());

        filterApplyButton.setOnAction(event -> {
            if (!(beginFilter.getText().matches(paternFormatTime) & endFilter.getText().matches(paternFormatTime))) {
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

                ButtonData current_button;
                int i = 0;
                while (i < listOfButtons.size()) {
                    i++;
                    current_button = listOfButtons.get(i);
                    if (current_button.getBegin() < begin || current_button.getBegin() > end || current_button.getEnd() < begin || current_button.getEnd() > end) {
                        current_button.getButton_obj().setVisible(false);
                    }
                }
                filterResetButton.setVisible(true);
                filterApplyButton.setDisable(true);
                createEventButton.setDisable(true);
            }
        });

        filterResetButton.setOnAction(event -> {
            filterResetButton.setVisible(false);
            filterApplyButton.setDisable(false);
            createEventButton.setDisable(false);
            int i = 0;
            while (i < listOfButtons.size()) {
                i++;
                listOfButtons.get(i).getButton_obj().setVisible(true);
            }
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