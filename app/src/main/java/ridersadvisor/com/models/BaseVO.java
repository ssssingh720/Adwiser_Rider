package ridersadvisor.com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseVO {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("property_site_id")
    @Expose
    private String property_site_id;
    @SerializedName("image")
    @Expose
    private String image;



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProperty_site_id() {
        return property_site_id;
    }

    public void setProperty_site_id(String property_site_id) {
        this.property_site_id = property_site_id;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}