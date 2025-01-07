package fitChoice;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;

public class HealthIssueCRUD extends Application {
    private HBox topBar;
    private VBox mainContainer;
    private GridPane grid;
    private Button logoutButton, addButton, updateButton, deleteButton,healthIssueCRUDButton, producApprovaltButton;
    private TextField HealthIssueField, healthIssueDescTextField, prohibitionTextField;
    private TableView<HealthIssue> table;
    private ArrayList<HealthIssue> listHealthIssue;
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
        stage.setTitle("Product CRUD");
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
        healthIssueCRUDButton = new Button("Health Issue CRUD"); // Initialize the button
        healthIssueCRUDButton.setStyle(buttonStyle);
        healthIssueCRUDButton.setOnAction(event -> {
            HealthIssueCRUD HealthIssueCRUDScreen = new HealthIssueCRUD();
            try {
                HealthIssueCRUDScreen.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        producApprovaltButton = new Button("Product Approval"); // Initialize the button
        producApprovaltButton.setStyle(buttonStyle);
        producApprovaltButton.setOnAction(event -> {
            ProductApproval productApprovalScreen = new ProductApproval();
            try {
                productApprovalScreen.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        addButton = new Button("Add HealthIssue");
        addButton.setStyle("-fx-background-color: #47663B; -fx-text-fill: #FFFFFF; -fx-font-size: 12;");
        updateButton = new Button("Update HealthIssue");
        updateButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 12;");
        deleteButton = new Button("Delete HealthIssue");
        deleteButton.setStyle("-fx-background-color: #EA324C; -fx-text-fill: #FFFFFF; -fx-font-size: 12;");


        addButton.setOnMouseClicked(e -> handleAddAction());
        updateButton.setOnMouseClicked(e -> handleUpdateAction());
        deleteButton.setOnMouseClicked(e -> handleDeleteAction());
    }

    private void handleAddAction() {
        String healthIssueName = HealthIssueField.getText();
        String healthIssueDesc = healthIssueDescTextField.getText();
        String prohibition = prohibitionTextField.getText();

        String healthIssueID = HealthIssue.generateID();
        insertData(healthIssueID, healthIssueName, healthIssueDesc, prohibition);
        refreshTable();
    }

    private void handleUpdateAction() {
        HealthIssue selectedHealthIssue = table.getSelectionModel().getSelectedItem();
        if (selectedHealthIssue != null) {
            String healthIssueID = selectedHealthIssue.getHealthIssueID();
            String healthIssueName = HealthIssueField.getText();
            String healthIssueDesc = healthIssueDescTextField.getText();
            String prohibition = prohibitionTextField.getText();

            updateData(healthIssueID, healthIssueName, healthIssueDesc, prohibition);
            refreshTable();
        }
    }

    private void handleDeleteAction() {
        HealthIssue selectedHealthIssue = table.getSelectionModel().getSelectedItem();
        if (selectedHealthIssue != null) {
            deleteData(selectedHealthIssue.getHealthIssueID());
            refreshTable();
        }
    }

    private void topBarContainer() {
        topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #FFFFFF;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(producApprovaltButton,healthIssueCRUDButton,spacer, logoutButton);
    }

    private VBox productCRUDForm() {
        grid = new GridPane();

        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 20, 0, 20));
        grid.setAlignment(Pos.CENTER_LEFT);

        Label healthIssueNameLable = new Label("HealthIssue Name:");
        HealthIssueField = new TextField();
        HealthIssueField.setPrefWidth(200);
        HealthIssueField.setPrefHeight(24);

        Label healthIssueDescLabel = new Label("healthIssue Desc:");
        healthIssueDescTextField = new TextField();
        healthIssueDescTextField.setPrefWidth(200);
        healthIssueDescTextField.setPrefHeight(24);

        Label prohibitionLabel = new Label("prohibition:");
        prohibitionTextField = new TextField();
        prohibitionTextField.setPrefWidth(200);
        prohibitionTextField.setPrefHeight(24);


        HBox btnBox = new HBox(10, addButton, updateButton, deleteButton);
        btnBox.setAlignment(Pos.CENTER_LEFT);
        btnBox.setPadding(new Insets(20, 20, 20, 20));

        grid.add(healthIssueNameLable, 0, 1);
        grid.add(HealthIssueField, 0, 2);
        grid.add(healthIssueDescLabel, 1, 1);
        grid.add(healthIssueDescTextField, 1, 2);
        grid.add(prohibitionLabel, 2, 1);
        grid.add(prohibitionTextField, 2, 2);

        VBox productFormContainer = new VBox(grid, btnBox);
        productFormContainer.setSpacing(10);

        return productFormContainer;
    }

    private VBox tableProduct() {
        table = new TableView<>();
        listHealthIssue = new ArrayList<>();

        TableColumn<HealthIssue, String> colhealthIssueID = new TableColumn<>("healthIssueID");
        colhealthIssueID.setCellValueFactory(new PropertyValueFactory<>("healthIssueID"));
        TableColumn<HealthIssue, String> colhealthIssueName = new TableColumn<>("healthIssueName");
        colhealthIssueName.setCellValueFactory(new PropertyValueFactory<>("healthIssueName"));
        TableColumn<HealthIssue, String> colhealthIssueDesc = new TableColumn<>("healthIssueDesc");
        colhealthIssueDesc.setCellValueFactory(new PropertyValueFactory<>("healthIssueDesc"));
        TableColumn<HealthIssue, String> Colprohibition = new TableColumn<>("prohibition");
        Colprohibition.setCellValueFactory(new PropertyValueFactory<>("prohibition"));

        table.getColumns().addAll(colhealthIssueID, colhealthIssueName, colhealthIssueDesc, Colprohibition);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        refreshTable();

        VBox paddedTableContainer = new VBox();
        paddedTableContainer.getChildren().add(table);
        paddedTableContainer.setPadding(new Insets(0, 20, 20, 20));

        return paddedTableContainer;
    }

    private void refreshTable() {
        listHealthIssue.clear();
        getData();
        table.setItems(FXCollections.observableArrayList(listHealthIssue));

        HealthIssueField.clear();
        healthIssueDescTextField.clear();
        prohibitionTextField.clear();
    }

    private void getData() {
        listHealthIssue.clear();
        String query = "SELECT * FROM healthIssues";
        connect.rs = connect.execQuery(query);

        try {
            while (connect.rs.next()) {
                String healthIssueID = connect.rs.getString("healthIssueID");
                String healthIssueName = connect.rs.getString("healthIssueName");
                String healthIssueDesc = connect.rs.getString("healthIssueDesc");
                String prohibition = connect.rs.getString("prohibition");

                listHealthIssue.add(new HealthIssue(healthIssueID, healthIssueName, healthIssueDesc, prohibition));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertData(String healthIssueID, String healthIssueName, String healthIssueDesc, String prohibition) {
        String query = "INSERT INTO healthIssues (healthIssueID, healthIssueName, healthIssueDesc, prohibition) VALUES ('" + healthIssueID + "', '" + healthIssueName + "', '" + healthIssueDesc + "', '" + prohibition + "')";
        connect.execUpdate(query);
    }

    private void updateData(String healthIssueID, String healthIssueName, String healthIssueDesc, String prohibition) {
        String query = "UPDATE healthIssues SET healthIssueName = '" + healthIssueName +
                "', healthIssueDesc = '" + healthIssueDesc +
                "', prohibition = '" + prohibition +
                "' WHERE healthIssueID = '" + healthIssueID + "'";
        connect.execUpdate(query);
    }

    private void deleteData(String healthIssueID) {
        String query = "DELETE FROM healthIssues WHERE healthIssueID = '" + healthIssueID + "'";
        connect.execUpdate(query);
    }
}
