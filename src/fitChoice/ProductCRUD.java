package fitChoice;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableCell;

public class ProductCRUD extends Application {
    private HBox topBar;
    private VBox mainContainer;
    private GridPane grid;
    private Button logoutButton, addButton, updateButton, deleteButton;
    private TextField productIDField, productNameField, productBrandField, productIngredientField, productNutrientField, categoryField, productImageField;
    private TableView<Products> table;
    private ArrayList<Products> listProducts;
    private Connect connect = Connect.getInstance();
    private BorderPane bp;
    private String adminID; // Store adminID from login

    public static void main(String[] args) {
        launch(args);
    }

    public ProductCRUD() {
        this.adminID = ""; // Default adminID
    }

    public ProductCRUD(String adminID) {
        this.adminID = adminID; // Set adminID from login
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

    private void topBarContainer() {
        topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #FFFFFF;");

        Label productLabel = new Label("Product");
        productLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(productLabel,spacer, logoutButton);
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

        addButton = new Button("Add Product");
        addButton.setStyle("-fx-background-color: #47663B; -fx-text-fill: #FFFFFF; -fx-font-size: 12;");
        updateButton = new Button("Update Product");
        updateButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 12;");
        deleteButton = new Button("Delete Product");
        deleteButton.setStyle("-fx-background-color: #EA324C; -fx-text-fill: #FFFFFF; -fx-font-size: 12;");

        addButton.setOnMouseClicked(e -> handleAddAction());
        updateButton.setOnMouseClicked(e -> handleUpdateAction());
        deleteButton.setOnMouseClicked(e -> handleDeleteAction());
    }

    private void handleAddAction() {
        if (areFieldsEmpty()) {
            showAlert("Please fill in all fields to add a product.");
            return;
        }
        String productsID = Products.generateID();
        String productsImage = productImageField.getText();
        String productsName = productNameField.getText();
        String productsBrand = productBrandField.getText();
        String ingredients = productIngredientField.getText();
        String nutrients = productNutrientField.getText();
        String categories = categoryField.getText();
        LocalDateTime createdAt = LocalDateTime.now();

        insertData(productsID, productsImage, productsName, productsBrand, categories, ingredients, nutrients, createdAt, adminID);
        refreshTable();
    }

    private void handleUpdateAction() {
        Products selectedProduct = table.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert("Please select a product to update.");
            return;
        }

        String productsID = selectedProduct.getProductsID();
        String productsImage = productImageField.getText();
        String productsName = productNameField.getText();
        String productsBrand = productBrandField.getText();
        String ingredients = productIngredientField.getText();
        String nutrients = productNutrientField.getText();
        String categories = categoryField.getText();
        LocalDateTime updatedAt = LocalDateTime.now();

        updateData(productsID, productsImage, productsName, productsBrand, categories, ingredients, nutrients, updatedAt, adminID);
        refreshTable();
    }

    private void handleDeleteAction() {
        Products selectedProduct = table.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            deleteData(selectedProduct.getProductsID());
            refreshTable();
        } else {
            showAlert("Please select a product to delete.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product CRUD");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean areFieldsEmpty() {
        return productNameField.getText().isEmpty() ||
                productBrandField.getText().isEmpty() ||
                categoryField.getText().isEmpty() ||
                productIngredientField.getText().isEmpty() ||
                productNutrientField.getText().isEmpty();
    }

    private VBox tableProduct() {
        table = new TableView<>();
        listProducts = new ArrayList<>();

        TableColumn<Products, String> colProductsID = new TableColumn<>("productsID");
        colProductsID.setCellValueFactory(new PropertyValueFactory<>("productsID"));

        TableColumn<Products, String> colProductsImage = new TableColumn<>("Image");
        colProductsImage.setCellValueFactory(new PropertyValueFactory<>("productsName"));
        colProductsImage.setCellFactory(column -> new TableCell<Products, String>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null); // Clear both graphic and text if the cell is empty
                    return; // Exit early if the cell is empty
                }

                if (imageUrl == null || imageUrl.isEmpty()) {
                    setGraphic(null);
                    setText("No Image");
                    return; // Exit early if the URL is null or empty
                }

                // Only proceed with image loading if imageUrl is valid
                Image image = null;
                try {
                    image = new Image(imageUrl, true);

                    Image finalImage = image;

                    image.errorProperty().addListener((obs, oldVal, newVal) -> {
                        if (newVal) {
                            System.err.println("Error loading image: " + imageUrl);
                            if (finalImage.getException() != null) {
                                System.err.println("Exception: " + finalImage.getException().getMessage());
                            }
                            setGraphic(null);
                            setText("Error");
                        }
                    });

                    image.progressProperty().addListener((obs, oldVal, newVal) -> {
                        if (newVal.doubleValue() == 1.0 && !finalImage.isError()) {
                            imageView.setImage(finalImage);
                            setGraphic(imageView);
                            setText(null);
                        }
                    });

                    if (image.getProgress() < 1) {
                        setText("Loading...");
                    }

                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid URL: " + imageUrl);
                    setGraphic(null);
                    setText("Invalid URL");
                }
            }
        });

        TableColumn<Products, String> colProductsName = new TableColumn<>("Name");
        colProductsName.setCellValueFactory(new PropertyValueFactory<>("productsImage"));

        TableColumn<Products, String> colProductsBrand = new TableColumn<>("Brand");
        colProductsBrand.setCellValueFactory(new PropertyValueFactory<>("productsBrand"));

        TableColumn<Products, String> colIngredients = new TableColumn<>("ingredients");
        colIngredients.setCellValueFactory(new PropertyValueFactory<>("ingredients"));

        TableColumn<Products, String> colNutrients = new TableColumn<>("nutrients");
        colNutrients.setCellValueFactory(new PropertyValueFactory<>("nutrients"));

        TableColumn<Products, String> colCategories = new TableColumn<>("categories");
        colCategories.setCellValueFactory(new PropertyValueFactory<>("categories"));

        TableColumn<Products, String> colApproval = new TableColumn<>("approval");
        colApproval.setCellValueFactory(new PropertyValueFactory<>("approval"));

        TableColumn<Products, String> colComment = new TableColumn<>("comments");
        colComment.setCellValueFactory(new PropertyValueFactory<>("comments"));

        table.getColumns().addAll(
                colProductsID, colProductsImage, colProductsName, colProductsBrand,
                colIngredients, colNutrients, colCategories, colApproval, colComment
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        refreshTable();

        VBox paddedTableContainer = new VBox();
        paddedTableContainer.getChildren().add(table);
        paddedTableContainer.setPadding(new Insets(0, 20, 20, 20));

        return paddedTableContainer;
    }


    private VBox productCRUDForm() {
        grid = new GridPane();

        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 20, 0, 20));
        grid.setAlignment(Pos.CENTER_LEFT);

        Label productNameLabel = new Label("Product Name:");
        productNameField = new TextField();
        productNameField.setPrefWidth(200);
        productNameField.setPrefHeight(24);

        Label productBrandLabel = new Label("Product Brand:");
        productBrandField = new TextField();
        productBrandField.setPrefWidth(200);
        productBrandField.setPrefHeight(24);

        Label categoryLabel = new Label("Category:");
        categoryField = new TextField();
        categoryField.setPrefWidth(200);
        categoryField.setPrefHeight(24);

        Label productImageLabel = new Label("Link Image:");
        productImageField = new TextField();
        productImageField.setPrefWidth(200);
        productImageField.setPrefHeight(24);

        Label productNutrientLabel = new Label("Product Nutrient:");
        productNutrientField = new TextField();
        productNutrientField.setPrefWidth(400);
        productNutrientField.setPrefHeight(200);

        Label productIngredientLabel = new Label("Product Ingredient:");
        productIngredientField = new TextField();
        productIngredientField.setPrefWidth(400);
        productIngredientField.setPrefHeight(200);

        HBox btnBox = new HBox(10, addButton, updateButton, deleteButton);
        btnBox.setAlignment(Pos.CENTER_LEFT);
        btnBox.setPadding(new Insets(20, 20, 20, 20));

        grid.add(productNameLabel, 0, 1);
        grid.add(productNameField, 0, 2);
        grid.add(productBrandLabel, 1, 1);
        grid.add(productBrandField, 1, 2);
        grid.add(categoryLabel, 2, 1);
        grid.add(categoryField, 2, 2);
        grid.add(productImageLabel, 3, 1);
        grid.add(productImageField, 3, 2);
        grid.add(productNutrientLabel, 0, 3);
        grid.add(productNutrientField, 0, 4, 2, 2);
        grid.add(productIngredientLabel, 2, 3);
        grid.add(productIngredientField, 2, 4, 3, 2);

        VBox productFormContainer = new VBox(grid, btnBox);
        productFormContainer.setSpacing(10);

        return productFormContainer;
    }


    private void refreshTable() {
        listProducts.clear();
        getData();
        table.setItems(FXCollections.observableArrayList(listProducts));

        productNameField.clear();
        productBrandField.clear();
        productIngredientField.clear();
        productNutrientField.clear();
        categoryField.clear();
        productImageField.clear();
    }

    private void getData() {
        listProducts.clear();
        String query = "SELECT * FROM products";
        connect.rs = connect.execQuery(query);

        try {
            while (connect.rs.next()) {
                String productsID = connect.rs.getString("productsID");
                String productsImage = connect.rs.getString("productsImage");
                String productsName = connect.rs.getString("productsName");
                String productsBrand = connect.rs.getString("productsBrand");
                String ingredients = connect.rs.getString("ingredients");
                String nutrients = connect.rs.getString("nutrients");
                String categories = connect.rs.getString("categories");
                String approval = connect.rs.getString("approval");
                String comments = connect.rs.getString("comments");


                Products.approval approvalEnum;
                if(approval==null){
                    approvalEnum = Products.approval.Waiting;
                }else {
                    approvalEnum = Products.approval.valueOf(approval);
                }

                listProducts.add(new Products(productsID, productsImage, productsName, productsBrand, ingredients, nutrients, categories,approvalEnum,comments));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertData(String productsID, String productsImage, String productsName, String productsBrand, String categories, String ingredients, String nutrients, LocalDateTime createdAt, String adminID) {
        String query = "INSERT INTO products (productsID, productsImage, productsName, productsBrand, categories, ingredients, nutrients, createdAt, adminID) " +
                "VALUES ('" + productsID + "', '" + productsImage + "', '" + productsName + "', '" + productsBrand + "', '" + categories + "', '" + ingredients + "', '" + nutrients + "', '" + createdAt + "', '" + adminID + "')";
        connect.execUpdate(query);
    }

    private void updateData(String productsID, String productsImage, String productsName, String productsBrand, String categories, String ingredients, String nutrients, LocalDateTime updatedAt, String adminID) {
        StringBuilder queryBuilder = new StringBuilder("UPDATE products SET ");
        if (!productsImage.isEmpty()) queryBuilder.append("productsImage = '").append(productsImage).append("', ");
        if (!productsName.isEmpty()) queryBuilder.append("productsName = '").append(productsName).append("', ");
        if (!productsBrand.isEmpty()) queryBuilder.append("productsBrand = '").append(productsBrand).append("', ");
        if (!categories.isEmpty()) queryBuilder.append("categories = '").append(categories).append("', ");
        if (!ingredients.isEmpty()) queryBuilder.append("ingredients = '").append(ingredients).append("', ");
        if (!nutrients.isEmpty()) queryBuilder.append("nutrients = '").append(nutrients).append("', ");
        queryBuilder.append("updatedAt = '").append(updatedAt).append("', adminID = '").append(adminID).append("' ");
        queryBuilder.append("WHERE productsID = '").append(productsID).append("'");
        connect.execUpdate(queryBuilder.toString());
    }

    private void deleteData(String productsID) {
        String query = "DELETE FROM products WHERE productsID = '" + productsID + "'";
        connect.execUpdate(query);
    }
}
