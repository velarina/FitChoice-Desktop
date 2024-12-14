package fitChoice.models;

public class Member {
    private String memberID;
    private String memberName;
    private String memberEmail;
    private String password;
    private Enum gender;
    private Integer age;

    enum permission {
        Male, Female
    }

    public Member(String memberID, String memberName, String memberEmail, String password, Enum gender, Integer age) {
        this.memberID = memberID;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.password = password;
        this.gender = gender;
        this.age = age;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Enum getGender() {
        return gender;
    }

    public void setGender(Enum gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
