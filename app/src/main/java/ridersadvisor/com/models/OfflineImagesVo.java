package ridersadvisor.com.models;

import android.net.Uri;

/**
 * Created by Sudhir Singh on 12,April,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class OfflineImagesVo {

    private String userId;
    private String userToken;
    private String propertySiteId;
    private Uri imageUri;
    private String imageName;
    private String imageLongitute;
    private String imageLatitude;

    public OfflineImagesVo(String userId, String userToken, String propertySiteId, Uri imageUri, String imageName, String imageLongitute, String imageLatitude) {
        this.userId = userId;
        this.userToken = userToken;
        this.propertySiteId = propertySiteId;
        this.imageUri = imageUri;
        this.imageName = imageName;
        this.imageLongitute = imageLongitute;
        this.imageLatitude = imageLatitude;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getPropertySiteId() {
        return propertySiteId;
    }

    public void setPropertySiteId(String propertySiteId) {
        this.propertySiteId = propertySiteId;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageLongitute() {
        return imageLongitute;
    }

    public void setImageLongitute(String imageLongitute) {
        this.imageLongitute = imageLongitute;
    }

    public String getImageLatitude() {
        return imageLatitude;
    }

    public void setImageLatitude(String imageLatitude) {
        this.imageLatitude = imageLatitude;
    }
}
