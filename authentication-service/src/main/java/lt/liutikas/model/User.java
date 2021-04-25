package lt.liutikas.model;

import javax.persistence.*;

@Entity(name = "_USER")
@Table(schema = "AUTHENTICATION_SERVICE")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer userId;
    @Column(nullable = false)
    private String passwordHash;
    @Column(nullable = false)
    private String email;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
