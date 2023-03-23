

public class JobResult {
    private String jobTitle;
    private String companyName;
    private String location;
    private String datePosted;

    public JobResult(String jobTitle, String companyName, String location, String datePosted) {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.location = location;
        this.datePosted = datePosted;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLocation() {
        return location;
    }

    public String getDatePosted() {
        return datePosted;
    }
}