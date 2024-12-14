package fitChoice;

import java.sql.SQLException;
import java.util.ArrayList;

import fitChoice.models.Products;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ProductCRUD extends Application {
    HBox topBar;
    VBox mainContainer;
    GridPane grid;
    Button logoutButton, addButton, updateButton, deleteButton;
    TextField productNameField, productBrandField, productIngredientField, productNutrientField, categoryField;
    TableView table;
    ArrayList<Products> listProducts;
    Connect connect = Connect.getInstance();
    BorderPane bp;

    public static void main(String[] args) {
        launch(args); // Launch the application
    }

    @Override
    public void start(Stage stage) {
        bp = new BorderPane();

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

        // Pass buttons to the top bar
        topBarContainer();

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

    public void addAction() {
        addButton.setOnMouseClicked(e -> {
            String productsName = productNameField.getText();
            String productsBrand = productBrandField.getText();
            String ingredients = productIngredientField.getText();
            String nutrients = productNutrientField.getText();
            String categories = categoryField.getText();

            String productsID = Products.generateID();
            insertData(productsID, productsName, productsBrand, ingredients, nutrients, categories);
            refreshTable();
        });

        updateButton.setOnMouseClicked(e->{
            Products selectedProduct = (Products)table.getSelectionModel().getSelectedItem();

            String productsID = selectedProduct.getProductsID();
            String productsName = productNameField.getText();
            String productsBrand = productBrandField.getText();
            String ingredients = productIngredientField.getText();
            String nutrients = productNutrientField.getText();
            String categories = categoryField.getText();

            updateData(productsID, productsName, productsBrand, ingredients, nutrients, categories);
            refreshTable();
        });

        deleteButton.setOnMouseClicked(e->{
            Products selectedProduct = (Products)table.getSelectionModel().getSelectedItem();

            String productsId = selectedProduct.getProductsID();
            deleteData(productsId);
            refreshTable();
        });
    }

    // Top bar with navigation buttons
    public void topBarContainer() {
        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #47663B;");

        logoutButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #EA324C; -fx-font-size: 14px; -fx-font-weight: bold;");

        // Create a spacer to push the "Log Out" button to the far-right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Add buttons and spacer to the topBar
        topBar.getChildren().addAll(spacer, logoutButton);

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
        TextField categoryField = new TextField();
        categoryField.setPrefWidth(400);
        categoryField.setPrefHeight(64);

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
        grid.add(categoryField, 2, 2);
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
        listProducts = new ArrayList<>();

        // Define columns
        TableColumn<Products, String> colProductsID = new TableColumn<>("productsID");
        colProductsID.setCellValueFactory(new PropertyValueFactory<>("productsID"));
        TableColumn<Products, String> colProductsName = new TableColumn<>("productsName");
        colProductsName.setCellValueFactory(new PropertyValueFactory<>("productsName"));
        TableColumn<Products, String> colProductsBrand = new TableColumn<>("productsBrand");
        colProductsBrand.setCellValueFactory(new PropertyValueFactory<>("productsBrand"));
        TableColumn<Products, String> colIngredients = new TableColumn<>("ingredients");
        colIngredients.setCellValueFactory(new PropertyValueFactory<>("ingredients"));
        TableColumn<Products, String> colNutrients = new TableColumn<>("nutrients");
        colNutrients.setCellValueFactory(new PropertyValueFactory<>("nutrients"));
        TableColumn<Products, String> colCategories = new TableColumn<>("categories");
        colCategories.setCellValueFactory(new PropertyValueFactory<>("categories"));

        // Add columns to the table
        table.getColumns().addAll(colProductsID, colProductsName, colProductsBrand, colIngredients, colNutrients, colCategories);
        ObservableList<Products> dataTabel = FXCollections.observableArrayList(listProducts);
        table.setItems(dataTabel);
        refreshTable();

        // Set column widths to fit the table evenly
        table.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double columnWidth = newWidth.doubleValue() / table.getColumns().size();
            for (TableColumn<?, ?> column : table.getColumns()) {
                column.setPrefWidth(columnWidth);
            }
        });

        // Set column resize policy
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        // Populate the ObservableList with Products objects
        for (int i = 0; i < 50; i++) {
            listProducts.add(new Products("ID" + i, "Product " + i, "Brand " + i, "Ingredients " + i, "Nutrients " + i, "Category " + i));
        }

        // Create VBox container for padding
        VBox paddedTableContainer = new VBox();
        paddedTableContainer.getChildren().add(table);
        paddedTableContainer.setPadding(new Insets(0, 20, 20, 20));

        return paddedTableContainer;
    }
    private void getData() {
        listProducts.clear();

        String query = "SELECT * from Products";
        connect.rs = connect.execQuery(query);

        try {
            while (connect.rs.next()) {
                String productsID = connect.rs.getString("productsID");
                String productsName = connect.rs.getString("productsName");
                String productsBrand = connect.rs.getString("productsBrand");
                String ingredients = connect.rs.getString("ingredients");
                String nutrients = connect.rs.getString("nutrients");
                String categories = connect.rs.getString("categories");

                listProducts.add(new Products(productsID,productsName,productsBrand,ingredients,nutrients,categories));
            }
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    //	Masukin array list ke tabel
    private void refreshTable() {
        getData();

        productNameField.clear();
        productBrandField.clear();
        productIngredientField.clear();
        productNutrientField.clear();
        categoryField.clear();

    }

    //	Insert
    private void insertData(String productsID, String productsName, String productsBrand, String categories, String ingredients, String nutrients) {
        String query =
                "INSERT INTO Products VALUES ('" +productsName +"','" +productsBrand +"','"+categories+"','"+ingredients+"','"+nutrients+"')";
        connect.execUpdate(query);
    }

    //	Update
    private void updateData(String productsID, String productsName, String productsBrand, String categories, String ingredients, String nutrients) {
        String query = "UPDATE Products SET productsName = '" +productsName +"','" +productsBrand +"','"+categories+"','"+ingredients+"','"+nutrients+"'";
        connect.execUpdate(query);
    }

    //	Delete
    private void deleteData(String productsID) {
        String query = "DELETE FROM Products WHERE nim = '"+productsID+"'";
        connect.execUpdate(query);
    }
}