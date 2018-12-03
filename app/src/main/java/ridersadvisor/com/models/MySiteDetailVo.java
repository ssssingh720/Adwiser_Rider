package ridersadvisor.com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sudhir Singh on 27,March,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class MySiteDetailVo implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("id")
    @Expose
    private String id;



    @SerializedName("site_code")
    @Expose
    private String siteCode;
    @SerializedName("rider_id")
    @Expose
    private String riderId;
    @SerializedName("media_owner_id")
    @Expose
    private String mediaOwnerId;
    @SerializedName("landmarks")
    @Expose
    private String landmarks;
    @SerializedName("locality")
    @Expose
    private String locality;

    @SerializedName("text_location")
    @Expose
    private String text_location;

    @SerializedName("lattitude")
    @Expose
    private String lattitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("site_facing_id")
    @Expose
    private String siteFacingId;
    @SerializedName("site_type_id")
    @Expose
    private String siteTypeId;
    @SerializedName("site_size")
    @Expose
    private String siteSize;
    @SerializedName("site_angle")
    @Expose
    private String siteAngle;
    @SerializedName("facingTraffic")
    @Expose
    private String facingTraffic;
    @SerializedName("illumination_status")
    @Expose
    private String illuminationStatus;
    @SerializedName("lighting_type")
    @Expose
    private String lightingType;
    @SerializedName("license")
    @Expose
    private String license;
    @SerializedName("site_position_id")
    @Expose
    private String sitePositionId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("user_created")
    @Expose
    private String userCreated;
    @SerializedName("added_by")
    @Expose
    private String addedBy;
    @SerializedName("added_date")
    @Expose
    private String addedDate;
    @SerializedName("user_updated")
    @Expose
    private String userUpdated;
    @SerializedName("modified_date")
    @Expose
    private String modifiedDate;
    @SerializedName("modify_by")
    @Expose
    private String modifyBy;
    @SerializedName("media_owner")
    @Expose
    private String mediaOwner;
    @SerializedName("site_facing")
    @Expose
    private String siteFacing;
    @SerializedName("site_type")
    @Expose
    private String siteType;
    @SerializedName("site_position")
    @Expose
    private String sitePosition;

    @SerializedName("site_position_second)")
    @Expose
    private String sitePositionII;

   @SerializedName("side_of_road")
    @Expose
    private String side_of_road;

 @SerializedName("site_pincode")
    @Expose
    private String site_pincode;



    @SerializedName("images")
    @Expose
    private ArrayList<MySiteImageVo> images = null;




    public MySiteDetailVo(String title, String id, String siteCode, String riderId, String mediaOwnerId,
                          String landmarks, String locality,String text_location, String lattitude, String longitude, String siteFacingId,
                          String siteTypeId, String siteSize, String siteAngle, String facingTraffic, String illuminationStatus,
                          String lightingType, String license, String sitePositionId, String status, String userCreated,
                          String addedBy, String addedDate, String userUpdated, String modifiedDate, String modifyBy,
                          String mediaOwner, String siteFacing, String siteType, String sitePosition,String sitePositionII,
                       String side_of_road,String site_pincode,   ArrayList<MySiteImageVo> images) {
        this.title = title;
        this.id = id;
        this.siteCode = siteCode;
        this.riderId = riderId;
        this.mediaOwnerId = mediaOwnerId;
        this.landmarks = landmarks;
        this.locality = locality;
        this.text_location = text_location;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.siteFacingId = siteFacingId;
        this.siteTypeId = siteTypeId;
        this.siteSize = siteSize;
        this.siteAngle = siteAngle;
        this.facingTraffic = facingTraffic;
        this.illuminationStatus = illuminationStatus;
        this.lightingType = lightingType;
        this.license = license;
        this.sitePositionId = sitePositionId;
        this.status = status;
        this.userCreated = userCreated;
        this.addedBy = addedBy;
        this.addedDate = addedDate;
        this.userUpdated = userUpdated;
        this.modifiedDate = modifiedDate;
        this.modifyBy = modifyBy;
        this.mediaOwner = mediaOwner;
        this.siteFacing = siteFacing;
        this.siteType = siteType;
        this.sitePosition = sitePosition;
        this.sitePositionII = sitePositionII;
        this.side_of_road=side_of_road;
        this.site_pincode=site_pincode;
        this.images = images;
    }

    public String getSide_of_road() {
        return side_of_road;
    }

    public void setSide_of_road(String side_of_road) {
        this.side_of_road = side_of_road;
    }

    public String getSite_pincode() {
        return site_pincode;
    }

    public void setSite_pincode(String site_pincode) {
        this.site_pincode = site_pincode;
    }

    public String getSitePositionII() {
        return sitePositionII;
    }

    public void setSitePositionII(String sitePositionII) {
        this.sitePositionII = sitePositionII;
    }

    public String getText_location() {
        return text_location;
    }

    public void setText_location(String text_location) {
        this.text_location = text_location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getMediaOwnerId() {
        return mediaOwnerId;
    }

    public void setMediaOwnerId(String mediaOwnerId) {
        this.mediaOwnerId = mediaOwnerId;
    }

    public String getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(String landmarks) {
        this.landmarks = landmarks;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSiteFacingId() {
        return siteFacingId;
    }

    public void setSiteFacingId(String siteFacingId) {
        this.siteFacingId = siteFacingId;
    }

    public String getSiteTypeId() {
        return siteTypeId;
    }

    public void setSiteTypeId(String siteTypeId) {
        this.siteTypeId = siteTypeId;
    }

    public String getSiteSize() {
        return siteSize;
    }

    public void setSiteSize(String siteSize) {
        this.siteSize = siteSize;
    }

    public String getSiteAngle() {
        return siteAngle;
    }

    public void setSiteAngle(String siteAngle) {
        this.siteAngle = siteAngle;
    }

    public String getFacingTraffic() {
        return facingTraffic;
    }

    public void setFacingTraffic(String facingTraffic) {
        this.facingTraffic = facingTraffic;
    }

    public String getIlluminationStatus() {
        return illuminationStatus;
    }

    public void setIlluminationStatus(String illuminationStatus) {
        this.illuminationStatus = illuminationStatus;
    }

    public String getLightingType() {
        return lightingType;
    }

    public void setLightingType(String lightingType) {
        this.lightingType = lightingType;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getSitePositionId() {
        return sitePositionId;
    }

    public void setSitePositionId(String sitePositionId) {
        this.sitePositionId = sitePositionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getUserUpdated() {
        return userUpdated;
    }

    public void setUserUpdated(String userUpdated) {
        this.userUpdated = userUpdated;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getMediaOwner() {
        return mediaOwner;
    }

    public void setMediaOwner(String mediaOwner) {
        this.mediaOwner = mediaOwner;
    }

    public String getSiteFacing() {
        return siteFacing;
    }

    public void setSiteFacing(String siteFacing) {
        this.siteFacing = siteFacing;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public String getSitePosition() {
        return sitePosition;
    }

    public void setSitePosition(String sitePosition) {
        this.sitePosition = sitePosition;
    }

    public ArrayList<MySiteImageVo> getImages() {
        return images;
    }

    public void setImages(ArrayList<MySiteImageVo> images) {
        this.images = images;
    }

}
