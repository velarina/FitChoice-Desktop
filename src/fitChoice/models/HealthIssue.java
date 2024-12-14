package fitChoice.models;

public class HealthIssue {
    private String healthIssueID;
    private String healthIssueName;
    private String healthIssueDesc;
    private String prohibition;

    public HealthIssue(String healthIssueID, String healthIssueName, String healthIssueDesc, String prohibition) {
        this.healthIssueID = healthIssueID;
        this.healthIssueName = healthIssueName;
        this.healthIssueDesc = healthIssueDesc;
        this.prohibition = prohibition;
    }

    public String getHealthIssueID() {
        return healthIssueID;
    }

    public void setHealthIssueID(String healthIssueID) {
        this.healthIssueID = healthIssueID;
    }

    public String getHealthIssueName() {
        return healthIssueName;
    }

    public void setHealthIssueName(String healthIssueName) {
        this.healthIssueName = healthIssueName;
    }

    public String getHealthIssueDesc() {
        return healthIssueDesc;
    }

    public void setHealthIssueDesc(String healthIssueDesc) {
        this.healthIssueDesc = healthIssueDesc;
    }

    public String getProhibition() {
        return prohibition;
    }

    public void setProhibition(String prohibition) {
        this.prohibition = prohibition;
    }
}
