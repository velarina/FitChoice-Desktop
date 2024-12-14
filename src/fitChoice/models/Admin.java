package fitChoice.models;

public class Admin {
    private String adminID;
    private String adminName;
    private String adminEmail;
    private String password;
    private Enum permission;

    enum permission {
        adminProduct, adminRegistrations
    }
    public Admin(String adminID, String adminName, String adminEmail, String password, Enum permission){
        this.adminID = adminID;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.password = password;
        this.permission = permission;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminI(String adminID) {
        this.adminID = adminID;
    }
    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminID = adminName;
    }
    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.adminEmail = password;
    }
    public Enum getPermission() {
        return permission;
    }

    public void setPermission(Enum permission) {
        this.permission = permission;
    }
}
