package fitChoice;

import java.util.Random;
import java.util.UUID;

public class Products {
    private String productsID;
    private String productsImage;
    private String productsName;
    private String productsBrand;
    private String categories;
    private String ingredients;
    private String nutrients;
    private Enum approval;
    private String comments;

    enum approval {
        Waiting, Declined, High, Medium, Low
    }

    public static String generateID() {
        Random random = new Random();
        int randomNumber = random.nextInt(100000);
        return String.format("PR%05d", randomNumber);
    }

    public Products(String productsID, String productsName, String productsImage, String productsBrand, String categories, String ingredients, String nutrients, Enum approval, String comments) {
        this.productsID = productsID;
        this.productsImage = productsImage;
        this.productsName = productsName;
        this.productsBrand = productsBrand;
        this.categories = categories;
        this.ingredients = ingredients;
        this.nutrients = nutrients;
        this.approval = approval;
        this.comments = comments;
    }

    public String getProductsID() {
        return productsID;
    }

    public void setProductsID(String productsID) {
        this.productsID = productsID;
    }

    public String getProductsImage() {
        return productsImage;
    }

    public void setProductsImage(String productsImage) {
        this.productsImage = productsImage;
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

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
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

    public Enum getApproval() {
        return approval;
    }

    public void setApproval(Enum approval) {
        this.approval = approval;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
