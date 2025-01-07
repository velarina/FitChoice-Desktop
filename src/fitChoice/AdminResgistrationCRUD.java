package fitChoice;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;

public class AdminResgistrationCRUD extends Application {
    private HBox topBar;
    private VBox mainContainer;
    private GridPane grid;
    private Button logoutButton, addButton, updateButton, deleteButton, NutritionistRegistrationCRUD, AdminResgistrationCRUD;
    private TextField adminNameField, adminEmailTextField, passwordTextField;
    private ComboBox<String> permissionDropdown;
    private TableView<Admins> table;
    private ArrayList<Admins> listAdmin;
    private Connect connect = Connect.getInstance();
    private BorderPane bp;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        bp = new BorderPane();

        initializeButtons(stage);

        topBarContainer();

        mainContainer = new VBox();
        mainContainer.getChildren().addAll(productCRUDForm(), tableProduct());
        mainContainer.setSpacing(20);

        bp.setTop(topBar);
        bp.setCenter(mainContainer);

        Scene scene = new Scene(bp, 1000, 600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Admin Registration CRUD");
        stage.show();
    }

    private void initializeButtons(Stage stage) {
        logoutButton = new Button("Log Out");
        logoutButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #EA324C; -fx-font-size: 14px; -fx-font-weight: bold;");
        logoutButton.setOnAction(event -> {
            Main mainScreen = new Main();
            try {
                mainScreen.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        String buttonStyle = "-fx-background-color: #F3F7EE; -fx-text-fill: #000000; -fx-font-size: 14px;";
        NutritionistRegistrationCRUD = new Button("Nutritionist Registration"); // Initialize the button
        NutritionistRegistrationCRUD.setStyle(buttonStyle);
        NutritionistRegistrationCRUD.setOnAction(event -> {
            NutritionistRegistrationCRUD NutritionistRegistrationCRUDScreen = new NutritionistRegistrationCRUD();
            try {
                NutritionistRegistrationCRUDScreen.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        AdminResgistrationCRUD = new Button("Admin Resgistration"); // Initialize the button
        AdminResgistrationCRUD.setStyle(buttonStyle);
        AdminResgistrationCRUD.setOnAction(event -> {
            AdminResgistrationCRUD AdminResgistrationCRUDScreen = new AdminResgistrationCRUD();
            try {
                AdminResgistrationCRUDScreen.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        addButton = new Button("Add Admin");
        addButton.setStyle("-fx-background-color: #47663B; -fx-text-fill: #FFFFFF; -fx-font-size: 12;");
        updateButton = new Button("Update Admin");
        updateButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 12;");
        deleteButton = new Button("Delete Admin");
        deleteButton.setStyle("-fx-background-color: #EA324C; -fx-text-fill: #FFFFFF; -fx-font-size: 12;");

        addButton.setOnMouseClicked(e -> handleAddAction());
        updateButton.setOnMouseClicked(e -> handleUpdateAction());
        deleteButton.setOnMouseClicked(e -> handleDeleteAction());
    }

    private void handleAddAction() {
        String adminName = adminNameField.getText();
        String adminEmail = adminEmailTextField.getText();
        String password = passwordTextField.getText();
        String permissionValue = permissionDropdown.getValue(); // Use getValue()

        if (permissionValue == null) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please select a permission level.");
            return;
        }

        if (!validatePassword(password)) {
            showAlert(Alert.AlertType.ERROR, "Password Error", "Password must be at least 8 characters and contain alphanumeric characters.");
            return;
        }

        String adminID = Admins.generateID();
        insertData(adminID, adminName, adminEmail, password, permissionValue);
        refreshTable();
    }

    private boolean validatePassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasDigit = false;
        boolean hasAlpha = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (Character.isLetter(c)) {
                hasAlpha = true;
            }
        }

        return hasDigit && hasAlpha;
    }

    private void handleUpdateAction() {
        Admins selectedAdmin = table.getSelectionModel().getSelectedItem();
        if (selectedAdmin != null) {
            String adminID = selectedAdmin.getAdminID();
            String adminName = adminNameField.getText();
            String adminEmail = adminEmailTextField.getText();
            String password = passwordTextField.getText();
            String permissionValue = permissionDropdown.getValue();

            if (permissionValue == null) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please select a permission level.");
                return;
            }
            if (!validatePassword(password)) {
                showAlert(Alert.AlertType.ERROR, "Password Error", "Password must be at least 8 characters and contain alphanumeric characters.");
                return;
            }

            updateData(adminID, adminName, adminEmail, password, permissionValue);
            refreshTable();
        }
    }

    private void handleDeleteAction() {
        Admins selectedAdmin = table.getSelectionModel().getSelectedItem();
        if (selectedAdmin != null) {
            deleteData(selectedAdmin.getAdminID());
            refreshTable();
        }
    }

    private void topBarContainer() {
        topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #FFFFFF;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(AdminResgistrationCRUD, NutritionistRegistrationCRUD,spacer, logoutButton);
    }

    private VBox productCRUDForm() {
        grid = new GridPane();

        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 20, 0, 20));
        grid.setAlignment(Pos.CENTER_LEFT);

        Label adminNameLabel = new Label("Admin Name:");
        adminNameField = new TextField();

        Label adminEmailLabel = new Label("Admin Email:");
        adminEmailTextField = new TextField();

        Label passwordLabel = new Label("Password:");
        passwordTextField = new TextField();

        Label permissionLabel = new Label("Permission:");
        permissionDropdown = new ComboBox<>(); // Use class-level field
        permissionDropdown.getItems().addAll("adminProduct", "adminRegistrations");

        HBox btnBox = new HBox(10, addButton, updateButton, deleteButton);
        btnBox.setAlignment(Pos.CENTER_LEFT);
        btnBox.setPadding(new Insets(20, 20, 20, 20));

        grid.add(adminNameLabel, 0, 0);
        grid.add(adminNameField, 0, 1);
        grid.add(adminEmailLabel, 1, 0);
        grid.add(adminEmailTextField, 1, 1);
        grid.add(passwordLabel, 2, 0);
        grid.add(passwordTextField, 2, 1);
        grid.add(permissionLabel, 3, 0);
        grid.add(permissionDropdown, 3, 1);

        VBox productFormContainer = new VBox(grid, btnBox);
        productFormContainer.setSpacing(10);

        return productFormContainer;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private VBox tableProduct() {
        table = new TableView<>();
        listAdmin = new ArrayList<>();

        TableColumn<Admins, String> coladminID = new TableColumn<>("adminID");
        coladminID.setCellValueFactory(new PropertyValueFactory<>("adminID"));
        TableColumn<Admins, String> coladminName = new TableColumn<>("adminName");
        coladminName.setCellValueFactory(new PropertyValueFactory<>("adminName"));
        TableColumn<Admins, String> coladminEmail = new TableColumn<>("adminEmail");
        coladminEmail.setCellValueFactory(new PropertyValueFactory<>("adminEmail"));
        TableColumn<Admins, String> colpassword = new TableColumn<>("password");
        colpassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        TableColumn<Admins, String> colpermission = new TableColumn<>("permission");
        colpermission.setCellValueFactory(new PropertyValueFactory<>("permission"));

        table.getColumns().addAll(coladminID, coladminName, coladminEmail, colpassword, colpermission);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        refreshTable();

        VBox paddedTableContainer = new VBox();
        paddedTableContainer.getChildren().add(table);
        paddedTableContainer.setPadding(new Insets(0, 20, 20, 20));

        return paddedTableContainer;
    }

    private void refreshTable() {
        listAdmin.clear();
        getData();
        table.setItems(FXCollections.observableArrayList(listAdmin));

        adminNameField.clear();
        adminEmailTextField.clear();
        passwordTextField.clear();
    }

    private void getData() {
        listAdmin.clear();
        String query = "SELECT * FROM admins";
        connect.rs = connect.execQuery(query);

        try {
            while (connect.rs.next()) {
                String adminID = connect.rs.getString("adminID");
                String adminName = connect.rs.getString("adminName");
                String adminEmail = connect.rs.getString("adminEmail");
                String password = connect.rs.getString("password");
                String permission = connect.rs.getString("permission");

                // Convert approvalString to Enum
                Admins.permission permissionEnum = Admins.permission.valueOf(permission);

                listAdmin.add(new Admins(adminID, adminName, adminEmail, password, permissionEnum));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertData(String adminID, String adminName, String adminEmail, String password, String permissionEnum) {
        String query = "INSERT INTO admins (adminID, adminName, adminEmail, password, permission) VALUES ('" + adminID + "', '" + adminName + "', '" + adminEmail + "', '" + password + "', '" + permissionEnum + "')";
        connect.execUpdate(query);
    }

    private void updateData(String adminID, String adminName, String adminEmail, String password, String permissionEnum) {
        String query = "UPDATE admins SET adminName = '" + adminName + "', '" + adminEmail + "', '" + password + "', '" + permissionEnum + "' WHERE adminID = '" + adminID + "'";
        connect.execUpdate(query);
    }

    private void deleteData(String adminID) {
        String query = "DELETE FROM admins WHERE adminID = '" + adminID + "'";
        connect.execUpdate(query);
    }
}
