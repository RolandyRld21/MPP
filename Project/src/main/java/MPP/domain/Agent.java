package MPP.domain;

public class Agent extends Entity<Long>{

    private String username;

    private String password;

    private String agency;

    public Agent(String username, String password, String agency) {
        this.username = username;
        this.password = password;
        this.agency = agency;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }
}
