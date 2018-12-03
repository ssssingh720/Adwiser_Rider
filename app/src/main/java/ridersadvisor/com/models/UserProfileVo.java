package ridersadvisor.com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Madhumita on 05-11-2015.
 */
public class UserProfileVo extends BaseVO implements Serializable {

    @SerializedName("userid")
    @Expose
    private String userId;
    @SerializedName("username")
    @Expose
    private String name;
    @SerializedName("useremail")
    @Expose
    private String userEmail;
    @SerializedName("usertoken")
    @Expose
    private String token;
    @SerializedName("access")
    @Expose
    private String access;

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
