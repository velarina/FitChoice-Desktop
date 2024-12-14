package fitChoice;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ProductCRUDCopy extends Application {
    VBox mainContainer;
    GridPane grid;
    TableView<String> table;
    BorderPane bp;

    // Top bar with navigation buttons
    public void topBarContainer(Button productButton, Button ingredientButton, Button nutrientButton, Button categoryButton, Button logoutButton) {
        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #47663B;");

        // Style buttons
        String buttonStyle = "-fx-background-color: #F3F7EE; -fx-text-fill: #47663B; -fx-font-size: 14px; -fx-font-weight: bold;";
        productButton.setStyle(buttonStyle);
        ingredientButton.setStyle(buttonStyle);
        nutrientButton.setStyle(buttonStyle);
        categoryButton.setStyle(buttonStyle);

        logoutButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #EA324C; -fx-font-size: 14px; -fx-font-weight: bold;");

        // Create a spacer to push the "Log Out" button to the far-right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Add buttons and spacer to the topBar
        topBar.getChildren().addAll(productButton, ingredientButton, nutrientButton, categoryButton, spacer, logoutButton);

        // Add the topBar to the BorderPane
        bp.setTop(topBar);
    }

    // Product CRUD form
    public VBox productCRUDForm() {
        grid = new GridPane();

        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 20, 0, 20));
        grid.setAlignment(Pos.CENTER_LEFT);

        Label productLabel = new Label("Product");
        productLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label productNameLabel = new Label("Product Name:");
        TextField productNameField = new TextField();
        productNameField.setPrefWidth(200);
        productNameField.setPrefHeight(24);

        Label productBrandLabel = new Label("Product Brand:");
        TextField productBrandField = new TextField();
        productBrandField.setPrefWidth(200);
        productBrandField.setPrefHeight(24);

        Label categoryLabel = new Label("Category:");
        ComboBox<String> categoryDropdown = new ComboBox<>();
        categoryDropdown.getItems().addAll("Category 1", "Category 2", "Category 3");
        categoryDropdown.setPrefWidth(200);
        categoryDropdown.setPrefHeight(24);

        Label imageLabel = new Label("Product Image:");
        Button uploadButton = new Button("Upload");
        uploadButton.setPrefWidth(200);
        uploadButton.setPrefHeight(24);

        Label productNutrientLabel = new Label("Product Nutrient:");
        TextField productNutrientField = new TextField();
        productNutrientField.setPrefWidth(400);
        productNutrientField.setPrefHeight(64);

        Label productIngredientLabel = new Label("Product Ingredient:");
        TextField productIngredientField = new TextField();
        productIngredientField.setPrefWidth(400);
        productIngredientField.setPrefHeight(64);

        Button addButton = new Button("Add Product");
        Button updateButton = new Button("Update Product");
        Button deleteButton = new Button("Delete Product");

        HBox btnBox = new HBox(10, addButton, updateButton, deleteButton);
        btnBox.setAlignment(Pos.CENTER_LEFT);
        btnBox.setPadding(new Insets(20, 20, 20, 20));

        grid.add(productLabel, 0, 0);
        grid.add(productNameLabel, 0, 1);
        grid.add(productNameField, 0, 2);
        grid.add(productBrandLabel, 1, 1);
        grid.add(productBrandField, 1, 2);
        grid.add(categoryLabel, 2, 1);
        grid.add(categoryDropdown, 2, 2);
        grid.add(imageLabel, 3, 1);
        grid.add(uploadButton, 3, 2);
        grid.add(productNutrientLabel, 0, 3);
        grid.add(productNutrientField, 0, 4, 2, 2);
        grid.add(productIngredientLabel, 2, 3);
        grid.add(productIngredientField, 2, 4, 3, 2);

        VBox productFormContainer = new VBox(grid, btnBox);
        productFormContainer.setSpacing(10);

        return productFormContainer;
    }

    // Table for displaying products
    public VBox tableProduct() {
        table = new TableView<>();

        // Define columns
        TableColumn<String, String> IDcolumn = new TableColumn<>("ID");
        TableColumn<String, String> imageColumn = new TableColumn<>("Image");
        TableColumn<String, String> nameColumn = new TableColumn<>("Name");
        TableColumn<String, String> brandColumn = new TableColumn<>("Brand");
        TableColumn<String, String> categoryColumn = new TableColumn<>("Category");
        TableColumn<String, String> ingredientColumn = new TableColumn<>("Ingredient");
        TableColumn<String, String> nutrientColumn = new TableColumn<>("Nutrient");
        TableColumn<String, String> approvalColumn = new TableColumn<>("Approval");
        TableColumn<String, String> commentColumn = new TableColumn<>("Comment");

        // Add columns to the table
        table.getColumns().addAll(IDcolumn, imageColumn, nameColumn, brandColumn, categoryColumn, ingredientColumn, nutrientColumn, approvalColumn,commentColumn);

        // Set column widths to fit the table evenly
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
    public void start(Stage stage) {
        bp = new BorderPane();

        // Define buttons
        Button productButton = new Button("Product");
        Button ingredientButton = new Button("Ingredient");
        Button nutrientButton = new Button("Nutrient");
        Button categoryButton = new Button("Category");
        Button logoutButton = new Button("Log Out");

        // Navigation actions
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
        mainContainer.getChildren().addAll(productCRUDForm(), tableProduct()); // Form and table are stacked vertically
        mainContainer.setSpacing(20);

        // Add main container to BorderPane center
        bp.setCenter(mainContainer);

        Scene scene = new Scene(bp, 1000, 600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Product CRUD");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch the application
    }
}