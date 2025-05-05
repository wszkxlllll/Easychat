package Ecommon;

import java.io.Serializable;
/*
 *表示一个用户信息
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String passwd;

    public User() {}
    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }

    public String getUserId() {
        return userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }


}
