package fitChoice;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class Login extends Application {
    Scene scene;
    Label loginLabel, emailLabel, passwordLabel;
    TextField emailTF;
    PasswordField passwordTF;
    Button loginBtn, backBtn;
    GridPane formContainer;

    private String role;

    // Constructor to accept the role (Admin or Nutritionist)
    public Login(String role) {
        this.role = role;
    }

    public void init() {
        formContainer = new GridPane();

        loginLabel = new Label("Login as " + role);
        loginLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        emailLabel = new Label("Email");
        emailLabel.setFont(Font.font("Arial", 16));

        passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font("Arial", 16));

        emailTF = new TextField();
        emailTF.setPrefWidth(360);
        emailTF.setPrefHeight(40);

        passwordTF = new PasswordField();
        passwordTF.setPrefWidth(360);
        passwordTF.setPrefHeight(40);

        loginBtn = new Button("Login");
        loginBtn.setPrefWidth(360);
        loginBtn.setPrefHeight(50);
        loginBtn.setFont(Font.font("Arial", 18));

        backBtn = new Button("Back");
        backBtn.setPrefWidth(360);
        backBtn.setPrefHeight(50);
        backBtn.setFont(Font.font("Arial", 18));

        backBtn.setStyle("-fx-background-color: #F3F7EE; -fx-text-fill: #47663B; -fx-border-color: #47663B; -fx-border-width: 2px; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        loginBtn.setStyle("-fx-background-color: #47663B; -fx-text-fill: #FFFFFF; -fx-border-radius: 12px; -fx-background-radius: 12px;");
    }

    public void loginForm(){
        formContainer.getChildren().clear();

        formContainer.setAlignment(Pos.TOP_CENTER);
        formContainer.setHgap(10);
        formContainer.setVgap(20);
        formContainer.setPadding(new Insets(20));

        GridPane.setHalignment(loginLabel, HPos.CENTER);
        formContainer.add(loginLabel, 0, 0, 2, 1);

        VBox emailBox = new VBox(5);
        emailBox.setAlignment(Pos.CENTER_LEFT);
        emailBox.getChildren().addAll(emailLabel, emailTF);

        VBox passwordBox = new VBox(5);
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        passwordBox.getChildren().addAll(passwordLabel, passwordTF);

        formContainer.add(emailBox, 0, 1, 2, 1);
        formContainer.add(passwordBox, 0, 2, 2, 1);

        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginBtn, backBtn);

        formContainer.add(buttonBox, 0, 3, 2, 1);
    }

    @Override
    public void start(Stage stage) throws Exception {
        init();
        loginForm();

        backBtn.setOnAction(event -> {
            try {
                Main mainScreen = new Main();
                mainScreen.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        loginBtn.setOnAction(event -> {
            try {
                ProductCRUD productCRUD = new ProductCRUD();
                productCRUD.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #E8ECD7;");
        root.getChildren().addAll(formContainer);

        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch the application
    }
}
