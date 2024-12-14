package fitChoice.models;

import java.util.UUID;

public class Products {
    private String productsID;
    private String productsName;
    private String productsBrand;
    private String categories;
    private String ingredients;
    private String nutrients;

    public Products(String productsID, String productsName, String productsBrand, String categories, String ingredients, String nutrients) {
        this.productsID = productsID;
        this.productsName = productsName;
        this.productsBrand = productsBrand;
        this.categories = categories;
        this.ingredients = ingredients;
        this.nutrients = nutrients;
    }

    public static String generateID() {
        return UUID.randomUUID().toString(); // Generates a unique ID
    }

    public String getProductsID() {
        return productsID;
    }

    public void setProductsID(String productsID) {
        this.productsID = productsID;
    }

    public String getProductsName() {
        return productsName;
    }

    public void setProductsName(String productsName) {
        this.productsName = productsName;
    }

    public String getProductsBrand() {
        return productsBrand;
    }

    public void setProductsBrand(String productsBrand) {
        this.productsBrand = productsBrand;
    }

    public String getcategories() {
        return categories;
    }

    public void setcategories(String categories) {
        this.categories = categories;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getNutrients() {
        return nutrients;
    }

    public void setNutrients(String nutrients) {
        this.nutrients = nutrients;
    }
}
