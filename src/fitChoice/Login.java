package fitChoice;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends Application {
    private Scene scene;
    private Label loginLabel, emailLabel, passwordLabel;
    private TextField emailTF;
    private PasswordField passwordTF;
    private Button loginBtn, backBtn;
    private GridPane formContainer;

    private Connect connect = Connect.getInstance();
    private String role;

    // Default constructor for JavaFX
    public Login() {
        this.role = "Unknown";
    }

    // Constructor with role for customization
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

        backBtn.setStyle("-fx-background-color: #F3F7EE; -fx-text-fill: #47663B; -fx-border-color: #47663B; -fx-border-width: 2px; -fx-border-radius: 12px;");
        loginBtn.setStyle("-fx-background-color: #47663B; -fx-text-fill: #FFFFFF; -fx-border-radius: 12px;");
    }

    public void loginForm() {
        formContainer.getChildren().clear();

        formContainer.setAlignment(Pos.TOP_CENTER);
        formContainer.setHgap(10);
        formContainer.setVgap(20);
        formContainer.setPadding(new Insets(20));

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

    private void handleLogin(Stage stage) {
        String email = emailTF.getText();
        String password = passwordTF.getText();

        try {
            if (role.equals("Admin") && loginAdmin(email, password)) {
                // Query to get the permission of the admin
                String query = "SELECT permission FROM admins WHERE adminEmail = '" + email + "'";
                connect.rs = connect.execQuery(query);

                if (connect.rs.next()) {
                    String permission = connect.rs.getString("permission");
                    System.out.println("Admin Permission: " + permission);

                    if ("adminProduct".equals(permission)) {
                        ProductCRUD productCRUD = new ProductCRUD();
                        productCRUD.start(stage);
                    } else if ("adminRegistrations".equals(permission)) {
                        AdminResgistrationCRUD registrationCRUD = new AdminResgistrationCRUD();
                        registrationCRUD.start(stage);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Access Denied", "Unknown permission level: " + permission);
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Failed", "No admin found with the provided email.");
                }
            } else if (role.equals("Nutritionist") && loginNutritionist(email, password)) {
                ProductApproval productApproval = new ProductApproval();
                productApproval.start(stage);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred during login: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.");
        }
    }

    public boolean loginAdmin(String email, String password) throws SQLException {
        String query = "SELECT * FROM admins WHERE adminEmail = '" + email + "' AND password = '" + password + "'";
        connect.rs = connect.execQuery(query);
        return connect.rs.next();
    }

    public boolean loginNutritionist(String email, String password) throws SQLException {
        String query = "SELECT * FROM nutritionists WHERE nutritionistEmail = '" + email + "' AND password = '" + password + "'";
        connect.rs = connect.execQuery(query);
        return connect.rs.next();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void start(Stage stage) {
        init();
        loginForm();

        backBtn.setOnAction(event -> {
            Main mainScreen = new Main();
            try {
                mainScreen.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        loginBtn.setOnAction(event -> handleLogin(stage));

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
        launch(args);
    }
}
