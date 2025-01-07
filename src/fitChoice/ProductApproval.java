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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableCell;


import java.sql.SQLException;
import java.util.ArrayList;

public class ProductApproval extends Application {
    private HBox topBar;
    private VBox mainContainer;
    private GridPane grid;
    private Button logoutButton, declinedButton, highButton, mediumButton, lowButton, producApprovaltButton,healthIssueCRUDButton;
    private TextField productIdField, CommentField;
    private TableView<Products> table;
    private ArrayList<Products> listProducts;
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

        table.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        productIdField.setText(newValue.getProductsID());
                    } else {
                        productIdField.setText("");
                    }
                }
        );

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
        producApprovaltButton = new Button("Product Approval"); // Initialize the button
        producApprovaltButton.setStyle(buttonStyle);
        producApprovaltButton.setOnAction(event -> {
            ProductApproval ProductApprovalScreen = new ProductApproval();
            try {
                ProductApprovalScreen.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
        producApprovaltButton.setStyle("-fx-background-color: #F3F7EE; -fx-text-fill: #000000; -fx-font-size: 14px;");
        producApprovaltButton.setOnAction(event -> {
            ProductApproval productApprovalScreen = new ProductApproval();
            try {
                productApprovalScreen.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        declinedButton = new Button("Declined");
        declinedButton.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF; -fx-font-size: 12;");
        declinedButton.setPrefWidth(80);
        highButton = new Button("High");
        highButton.setStyle("-fx-background-color: #EA324C; -fx-text-fill: #000000; -fx-font-size: 12;");
        highButton.setPrefWidth(80);
        mediumButton = new Button("Medium");
        mediumButton.setStyle("-fx-background-color: #F2AC42; -fx-text-fill: #000000; -fx-font-size: 12;");
        mediumButton.setPrefWidth(80);
        lowButton = new Button("Low");
        lowButton.setStyle("-fx-background-color: #00B38A; -fx-text-fill: #000000; -fx-font-size: 12;");
        lowButton.setPrefWidth(80);


        declinedButton.setOnAction(e -> updateApproval(Products.approval.Declined));
        highButton.setOnAction(e -> updateApproval(Products.approval.High));
        mediumButton.setOnAction(e -> updateApproval(Products.approval.Medium));
        lowButton.setOnAction(e -> updateApproval(Products.approval.Low));
    }

    private void updateApproval(Products.approval approvalValue) {
        Products selectedProduct = table.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            String productsID = selectedProduct.getProductsID();
            String comment = CommentField.getText();

            updateData(productsID, comment, approvalValue.toString());
            refreshTable();
        } else {
            showAlert(Alert.AlertType.WARNING, "No Product Selected", "Please select a product to approve.");
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

        Label CommentLabel = new Label("Comment :");
        CommentField = new TextField();
        CommentField.setPrefWidth(600);

        HBox btnBox = new HBox(10, declinedButton, highButton, mediumButton,lowButton );
        btnBox.setAlignment(Pos.CENTER_LEFT);
        btnBox.setPadding(new Insets(20, 20, 20, 20));

        Label productIdLabel = new Label("Product ID:");
        productIdField = new TextField();

        grid.add(productIdLabel, 0, 0);
        grid.add(productIdField, 0, 1);
        grid.add(CommentLabel, 1, 0);
        grid.add(CommentField, 1, 1);

        VBox productFormContainer = new VBox(grid, btnBox);
        productFormContainer.setSpacing(10);

        return productFormContainer;
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


    private void refreshTable() {
        listProducts.clear();
        getData();
        table.setItems(FXCollections.observableArrayList(listProducts));

        CommentField.clear();
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

    private void updateData(String productsID, String comments, String approvalEnum) {
        String query = "UPDATE products SET comments = '" + comments + "', approval = '" + approvalEnum + "' WHERE productsID = '" + productsID + "'";
        connect.execUpdate(query);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
