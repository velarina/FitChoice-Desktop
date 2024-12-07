package fitChoice;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class Main extends Application {
    Scene scene;
    Label label;
    Button nutritionistButton;
    Button adminButton;

    public Main() {
        label = new Label("Welcome to Fitchoice!");

        nutritionistButton = new Button("Nutritionist");
        adminButton = new Button("Admin");

        label.setFont(Font.font("Arial", 32));
        label.setStyle("-fx-font-weight: bold;");

        nutritionistButton.setFont(Font.font("Arial", 24));
        adminButton.setFont(Font.font("Arial", 24));

        nutritionistButton.setPrefWidth(360);
        nutritionistButton.setPrefHeight(50);
        adminButton.setPrefWidth(360);
        adminButton.setPrefHeight(50);

        nutritionistButton.setStyle("-fx-background-color: #F3F7EE; -fx-text-fill: #47663B; -fx-border-color: #47663B; -fx-border-width: 2px; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        adminButton.setStyle("-fx-background-color: #47663B; -fx-text-fill: #FFFFFF; -fx-border-radius: 12px; -fx-background-radius: 12px;");
    }

    @Override
    public void start(Stage stage) throws Exception {
        VBox buttonBox = new VBox(32);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(adminButton, nutritionistButton);

        VBox root = new VBox(64);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #E8ECD7;");
        root.getChildren().addAll(label, buttonBox);

        Scene scene = new Scene(root, 1000, 600);

        // Button Actions
        nutritionistButton.setOnAction(event -> {
            Login loginScreen = new Login("Nutritionist");
            try {
                loginScreen.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        adminButton.setOnAction(event -> {
            Login loginScreen = new Login("Admin");
            try {
                loginScreen.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("FitChoice");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
