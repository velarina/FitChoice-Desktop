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

public class NutritionistRegistrationCRUD extends Application {
    private HBox topBar;
    private VBox mainContainer;
    private GridPane grid;
    private Button logoutButton, addButton, updateButton, deleteButton,AdminResgistrationCRUD, NutritionistRegistrationCRUD;
    private TextField nutritionistNameField, nutritionistEmailTextField, passwordTextField,specializationField, certificationTextField,yearsOfExperienceTextField;
    private TableView<Nutritionist> table;
    private ArrayList<Nutritionist> ListNutritionist;
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
        mainContainer.getChildren().addAll(NutritionistCRUDForm(), tableNutritionist());
        mainContainer.setSpacing(20);

        bp.setTop(topBar);
        bp.setCenter(mainContainer);

        Scene scene = new Scene(bp, 1000, 600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Nutritionist registration CRUD");
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
        NutritionistRegistrationCRUD = new Button("Health Issue CRUD"); // Initialize the button
        NutritionistRegistrationCRUD.setStyle(buttonStyle);
        NutritionistRegistrationCRUD.setOnAction(event -> {
            NutritionistRegistrationCRUD NutritionistRegistrationCRUDScreen = new NutritionistRegistrationCRUD();
            try {
                NutritionistRegistrationCRUDScreen.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        AdminResgistrationCRUD = new Button("Product Approval"); // Initialize the button
        AdminResgistrationCRUD.setStyle(buttonStyle);
        AdminResgistrationCRUD.setOnAction(event -> {
            AdminResgistrationCRUD AdminResgistrationCRUDScreen = new AdminResgistrationCRUD();
            try {
                AdminResgistrationCRUDScreen.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        addButton = new Button("Add Nutritionist");
        addButton.setStyle("-fx-background-color: #47663B; -fx-text-fill: #FFFFFF; -fx-font-size: 12;");
        updateButton = new Button("Update Nutritionist");
        updateButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 12;");
        deleteButton = new Button("Delete Nutritionist");
        deleteButton.setStyle("-fx-background-color: #EA324C; -fx-text-fill: #FFFFFF; -fx-font-size: 12;");


        addButton.setOnMouseClicked(e -> handleAddAction());
        updateButton.setOnMouseClicked(e -> handleUpdateAction());
        deleteButton.setOnMouseClicked(e -> handleDeleteAction());
    }

    private void handleAddAction() {
        String nutritionistName = nutritionistNameField.getText();
        String nutritionistEmail = nutritionistEmailTextField.getText();
        String password = passwordTextField.getText();
        String specialization = specializationField.getText();
        String certification = certificationTextField.getText();
        Integer yearsOfExperience = Integer.parseInt(yearsOfExperienceTextField.getText());
        String nutritionistID = Products.generateID();
        insertData(nutritionistID, nutritionistName, nutritionistEmail, password, specialization, certification, yearsOfExperience);
        refreshTable();
    }

    private void handleUpdateAction() {
        Nutritionist selectedNutritionist = table.getSelectionModel().getSelectedItem();
        if (selectedNutritionist != null) {
            String nutritionistID = selectedNutritionist.getNutritionistID();
            String nutritionistName = nutritionistNameField.getText();
            String nutritionistEmail = nutritionistEmailTextField.getText();
            String password = passwordTextField.getText();
            String specialization = specializationField.getText();
            String certification = certificationTextField.getText();
            Integer yearsOfExperience = Integer.parseInt(yearsOfExperienceTextField.getText());

            updateData(nutritionistID, nutritionistName, nutritionistEmail, password, specialization, certification,yearsOfExperience);
            refreshTable();
        }
    }

    private void handleDeleteAction() {
        Nutritionist selectedNutritionist = table.getSelectionModel().getSelectedItem();
        if (selectedNutritionist != null) {
            deleteData(selectedNutritionist.getNutritionistID());
            refreshTable();
        }
    }

    private void topBarContainer() {
        topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #FFFFFF;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(NutritionistRegistrationCRUD,AdminResgistrationCRUD,spacer, logoutButton);
    }

    private VBox NutritionistCRUDForm() {
        grid = new GridPane();

        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 20, 0, 20));
        grid.setAlignment(Pos.CENTER_LEFT);

        Label nutritionistNameLabel = new Label("nutritionist Name:");
        nutritionistNameField = new TextField();
        nutritionistNameField.setPrefWidth(200);
        nutritionistNameField.setPrefHeight(24);

        Label nutritionistEmailLabel = new Label("nutritionist Email:");
        nutritionistEmailTextField = new TextField();
        nutritionistEmailTextField.setPrefWidth(200);
        nutritionistEmailTextField.setPrefHeight(24);

        Label passwordLabel = new Label("password:");
        passwordTextField = new TextField();
        passwordTextField.setPrefWidth(200);
        passwordTextField.setPrefHeight(24);

        Label specializationLable = new Label("specialization:");
        specializationField = new TextField();
        specializationField.setPrefWidth(200);
        specializationField.setPrefHeight(24);

        Label certificationLabel = new Label("certification:");
        certificationTextField = new TextField();
        certificationTextField.setPrefWidth(200);
        certificationTextField.setPrefHeight(24);

        Label yearsOfExperienceLabel = new Label("yearsOfExperience:");
        yearsOfExperienceTextField = new TextField();
        yearsOfExperienceTextField.setPrefWidth(200);
        yearsOfExperienceTextField.setPrefHeight(24);

        HBox btnBox = new HBox(10, addButton, updateButton, deleteButton);
        btnBox.setAlignment(Pos.CENTER_LEFT);
        btnBox.setPadding(new Insets(20, 20, 20, 20));

        grid.add(nutritionistNameLabel, 0, 0);
        grid.add(nutritionistNameField, 0, 1);
        grid.add(nutritionistEmailLabel, 1, 0);
        grid.add(nutritionistEmailTextField, 1, 1);
        grid.add(passwordLabel, 2, 0);
        grid.add(passwordTextField, 2, 1);
        grid.add(specializationLable, 0, 2);
        grid.add(specializationField, 0, 3);
        grid.add(certificationLabel, 1, 2);
        grid.add(certificationTextField, 1, 3);
        grid.add(yearsOfExperienceLabel, 2, 2);
        grid.add(yearsOfExperienceTextField, 2, 3);

        VBox NutritionistFormContainer = new VBox(grid, btnBox);
        NutritionistFormContainer.setSpacing(10);

        return NutritionistFormContainer;
    }

    private VBox tableNutritionist() {
        table = new TableView<>();
        ListNutritionist = new ArrayList<>();

        TableColumn<Nutritionist, String> colnutritionistID = new TableColumn<>("nutritionistID");
        colnutritionistID.setCellValueFactory(new PropertyValueFactory<>("nutritionistID"));
        TableColumn<Nutritionist, String> colnutritionistName = new TableColumn<>("nutritionistName");
        colnutritionistName.setCellValueFactory(new PropertyValueFactory<>("nutritionistName"));
        TableColumn<Nutritionist, String> colnutritionistEmail = new TableColumn<>("nutritionistEmail");
        colnutritionistEmail.setCellValueFactory(new PropertyValueFactory<>("nutritionistEmail"));
        TableColumn<Nutritionist, String> colpassword = new TableColumn<>("password");
        colpassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        TableColumn<Nutritionist, String> colspecialization = new TableColumn<>("specialization");
        colspecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        TableColumn<Nutritionist, String> colcertification = new TableColumn<>("certification");
        colcertification.setCellValueFactory(new PropertyValueFactory<>("certification"));
        TableColumn<Nutritionist, String> colyearsOfExperience = new TableColumn<>("yearsOfExperience");
        colyearsOfExperience.setCellValueFactory(new PropertyValueFactory<>("yearsOfExperience"));

        table.getColumns().addAll(colnutritionistID, colnutritionistName, colnutritionistEmail, colpassword,colspecialization, colcertification, colyearsOfExperience);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        refreshTable();

        VBox paddedTableContainer = new VBox();
        paddedTableContainer.getChildren().add(table);
        paddedTableContainer.setPadding(new Insets(0, 20, 20, 20));

        return paddedTableContainer;
    }

    private void refreshTable() {
        ListNutritionist.clear();
        getData();
        table.setItems(FXCollections.observableArrayList(ListNutritionist));

        nutritionistNameField.clear();
        nutritionistEmailTextField.clear();
        passwordTextField.clear();
        specializationField.clear();
        certificationTextField.clear();
        yearsOfExperienceTextField.clear();
    }

    private void getData() {
        ListNutritionist.clear();
        String query = "SELECT * FROM nutritionists";
        connect.rs = connect.execQuery(query);

        try {
            while (connect.rs.next()) {
                String nutritionistID = connect.rs.getString("nutritionistID");
                String nutritionistName = connect.rs.getString("nutritionistName");
                String nutritionistEmail = connect.rs.getString("nutritionistEmail");
                String password = connect.rs.getString("password");
                String specialization = connect.rs.getString("specialization");
                String certification = connect.rs.getString("certification");
                Integer yearsOfExperience = connect.rs.getInt("yearsOfExperience");

                ListNutritionist.add(new Nutritionist(nutritionistID, nutritionistName, nutritionistEmail, password,specialization,certification,yearsOfExperience));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertData(String nutritionistID, String nutritionistName, String nutritionistEmail, String password, String specialization, String certification, Integer yearsOfExperience) {
        String query = "INSERT INTO nutritionists (nutritionistID, nutritionistName, nutritionistEmail, password, specialization, certification, yearsOfExperience) VALUES ('" + nutritionistID + "', '" + nutritionistName + "', '" + nutritionistEmail + "', '" + password + "', '" + specialization + "', '" + certification + "',"+yearsOfExperience+")";
        connect.execUpdate(query);
    }

    private void updateData(String nutritionistID, String nutritionistName, String nutritionistEmail, String password, String specialization, String certification, Integer yearsOfExperience) {
        String query = "UPDATE nutritionists SET nutritionistName = '" + nutritionistName + "', '" + nutritionistEmail + "', '" + password + "', '" + specialization + "', '" + certification + "', " + yearsOfExperience + " WHERE nutritionistID = '" + nutritionistID + "'";
        connect.execUpdate(query);
    }

    private void deleteData(String nutritionistID) {
        String query = "DELETE FROM nutritionists WHERE nutritionistID = '" + nutritionistID + "'";
        connect.execUpdate(query);
    }
}
