package zhuoyan.com.loginsample.modle;

import java.io.Serializable;

public class User implements Serializable {

    private String userName;
    private String userPwd;
    private String userTwoPwd;

    public User(String userName, String userPwd, String userTwoPwd) {
        this.userName = userName;
        this.userPwd = userPwd;
        this.userTwoPwd = userTwoPwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserTwoPwd() {
        return userTwoPwd;
    }

    public void setUserTwoPwd(String userTwoPwd) {
        this.userTwoPwd = userTwoPwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", userTwoPwd='" + userTwoPwd + '\'' +
                '}';
    }
}
