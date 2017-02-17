/**
 * Created by sanju on 10/31/2016.
 */

public class Sample {


    /**
     * status : ok
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0Nzc5NTc2MzgsImV4cCI6MTUwOTQ5MzYzOCwianRpIjoiMWc2MUFST1pLbERYTTdYa1pZVGFuQyIsInVzZXIiOjJ9.5diai8-3k193XW1rTCFPiyIC042XCwJyyTjLWxf3wNQ
     * userId : 2
     * userEmail : user@test.com
     * userFname : Alice
     * userLname : Tom
     * userRole : USER
     */

    private String status;
    private String token;
    private int userId;
    private String userEmail;
    private String userFname;
    private String userLname;
    private String userRole;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFname() {
        return userFname;
    }

    public void setUserFname(String userFname) {
        this.userFname = userFname;
    }

    public String getUserLname() {
        return userLname;
    }

    public void setUserLname(String userLname) {
        this.userLname = userLname;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
