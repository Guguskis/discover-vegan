package lt.liutikas.dto;

import lt.liutikas.model.UserType;

public class LoginResponseDto {

    private String token;
    private UserType userType;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
