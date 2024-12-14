package fitChoice;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class NutrientCRUD extends Application {
    VBox mainContainer;
    GridPane grid;
    VBox box;
    HBox btnBox;
    TableView<String> table;
    BorderPane bp;

    // Top bar with navigation buttons
    public void topBarContainer(Button productButton, Button ingredientButton, Button nutrientButton, Button categoryButton, Button logoutButton) {
        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #FFFFFF;");

        // Style buttons
        String buttonStyle = "-fx-background-color: #F3F7EE; -fx-text-fill: #47663B; -fx-font-size: 14px; -fx-font-weight: bold;";
        productButton.setStyle(buttonStyle);
        ingredientButton.setStyle(buttonStyle);
        nutrientButton.setStyle(buttonStyle);
        categoryButton.setStyle(buttonStyle);

        logoutButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #EA324C; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-color: #EA324C; -fx-border-width:1; fx-border-radius: 12; -fx-background-radius: 12px;");

        // Create a spacer to push the "Log Out" button to the far-right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Add buttons and spacer to the topBar
        topBar.getChildren().addAll(productButton, ingredientButton, nutrientButton, categoryButton, spacer, logoutButton);

        // Add the topBar to the BorderPane
        bp.setTop(topBar);
    }

    // Product CRUD form
    public VBox CRUDForm() {
        grid = new GridPane();
        box = new VBox(20);
        btnBox = new HBox(10);

        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.TOP_LEFT);

        Label nutritionsLabel = new Label("Nutritions");
        nutritionsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label nutritionsNameLabel = new Label("Nutritions Name:");
        TextField nutritionsNameField = new TextField();

        // Action Buttons
        Button addButton = new Button("Add Nutritions");
        Button updateButton = new Button("Update Nutritions");
        Button deleteButton = new Button("Delete Nutritions");

        btnBox.getChildren().addAll(addButton, updateButton, deleteButton);
        btnBox.setAlignment(Pos.TOP_LEFT);
        btnBox.setPadding(new Insets(10));

        // Add components to GridPane
        grid.add(nutritionsLabel, 0, 0);

        grid.add(nutritionsNameLabel, 0, 1);
        grid.add(nutritionsNameField, 0, 2);

        box.getChildren().addAll(grid, btnBox);
        bp.setLeft(box);

        VBox nutritionistFormContainer = new VBox(grid, btnBox);
        nutritionistFormContainer.setSpacing(10);

        return nutritionistFormContainer;
    }

    // Table for displaying products
    public VBox tableProduct() {
        table = new TableView<>();
        table.setPrefHeight(200);

        TableColumn<String, String> nutritionsIDColumn = new TableColumn<>("ID");
        TableColumn<String, String> nutritionsNameColumn = new TableColumn<>("Nutritions");
        TableColumn<String, String> productColumn = new TableColumn<>("Products");

        table.getColumns().addAll(nutritionsIDColumn, nutritionsNameColumn, productColumn);
        table.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double columnWidth = newWidth.doubleValue() / table.getColumns().size();
            for (TableColumn<?, ?> column : table.getColumns()) {
                column.setPrefWidth(columnWidth);
            }
        });

        // Set column resize policy
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        // Add some example data to test scrollbars
        for (int i = 0; i < 50; i++) {
            table.getItems().add("Row " + i);
        }

        // Create VBox container for padding
        VBox paddedTableContainer = new VBox();
        paddedTableContainer.getChildren().add(table);
        paddedTableContainer.setPadding(new Insets(0, 20, 20, 20));

        return paddedTableContainer;
    }
    @Override
    public void start(Stage stage) throws Exception {
        bp = new BorderPane(); // Initialize BorderPane

        Button productButton = new Button("Product");
        Button ingredientButton = new Button("Ingredient");
        Button nutrientButton = new Button("Nutrient");
        Button categoryButton = new Button("Category");
        Button logoutButton = new Button("Log Out");

        logoutButton.setOnAction(event -> {
            Main mainScreen = new Main();
            try {
                mainScreen.start(stage); // Navigate to Main class
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        ingredientButton.setOnAction(event -> {
            IngredientCRUD ingredientCRUD = new IngredientCRUD();
            try {
                ingredientCRUD.start(stage); // Navigate to IngredientCRUD class
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        nutrientButton.setOnAction(event -> {
            NutrientCRUD nutrientCRUD = new NutrientCRUD();
            try {
                nutrientCRUD.start(stage); // Navigate to NutrientCRUD class
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        categoryButton.setOnAction(event -> {
            CategoryCRUD categoryCRUD = new CategoryCRUD();
            try {
                categoryCRUD.start(stage); // Navigate to CategoryCRUD class
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Pass buttons to the top bar
        topBarContainer(productButton, ingredientButton, nutrientButton, categoryButton, logoutButton);

        // Create main container
        mainContainer = new VBox();
        mainContainer.getChildren().addAll(CRUDForm(), tableProduct()); // Form and table are stacked vertically
        mainContainer.setSpacing(20);

        // Add main container to BorderPane center
        bp.setCenter(mainContainer);

        topBarContainer(productButton, ingredientButton, nutrientButton, categoryButton, logoutButton);
        CRUDForm();
        tableProduct();

        // Create and set scene
        Scene scene = new Scene(bp, 1000, 600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Nutritions CRUD");
        stage.show();
    }
    public static void main(String[] args) {
        launch(args); // Launch the application
    }
}
