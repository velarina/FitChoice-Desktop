package fitChoice.models;

public class Nutritionist {

    private String nutritionistID;
    private String nutritionistName;
    private String nutritionistEmail;
    private String password;
    private String specialization;
    private String certification;
    private Integer yearsOfExperience;

    public Nutritionist(String nutritionistID, String nutritionistName, String nutritionistEmail, String password, String specialization, String certification, Integer yearsOfExperience) {
        this.nutritionistID = nutritionistID;
        this.nutritionistName = nutritionistName;
        this.nutritionistEmail = nutritionistEmail;
        this.password = password;
        this.specialization = specialization;
        this.certification = certification;
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getNutritionistID() {
        return nutritionistID;
    }

    public void setNutritionistID(String nutritionistID) {
        this.nutritionistID = nutritionistID;
    }

    public String getNutritionistName() {
        return nutritionistName;
    }

    public void setNutritionistName(String nutritionistName) {
        this.nutritionistName = nutritionistName;
    }

    public String getNutritionistEmail() {
        return nutritionistEmail;
    }

    public void setNutritionistEmail(String nutritionistEmail) {
        this.nutritionistEmail = nutritionistEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }
}
