package reservation.app;

import java.io.IOException;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelloController {

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private Text resultAuthorization;

    @FXML
    private Button signInButton;

    @FXML
    void initialize() {

        HashMap<String, String> loginsAndPasswords = new HashMap<>();

        loginsAndPasswords.put("admin", "admin");
        loginsAndPasswords.put("stratonovteacher", "4123");
        loginsAndPasswords.put("Musaev_Tariel", "parol");
        loginsAndPasswords.put("Badygin_Roman", "karlsberg");
        loginsAndPasswords.put("Varkentin_Mariya", "starostanagibator");

        signInButton.setOnAction(actionEvent -> {
            if (password_field.getText().equals(loginsAndPasswords.get(login_field.getText()))) {
                signInButton.getScene().getWindow().hide();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("MainApp.fxml"));
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setTitle("Расписание мероприятий");
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } else {
                resultAuthorization.setText("Неверный логин или пароль.");
                resultAuthorization.setFill(Color.BLACK);
                password_field.clear();
            }
        });
    }
}