package ridersadvisor.com.monitor_site.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sudhir Singh on 24,April,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class MonitorSiteDetailVo implements Serializable {

    @SerializedName("allocation_id")
    @Expose
    private String allocationId;
    @SerializedName("site_id")
    @Expose
    private String siteId;
    @SerializedName("superviser_id")
    @Expose
    private String superviserId;
    @SerializedName("superviser_name")
    @Expose
    private String superviserName;
    @SerializedName("rider_id")
    @Expose
    private String riderId;
    @SerializedName("rider_name")
    @Expose
    private String riderName;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("site_code")
    @Expose
    private String siteCode;
    @SerializedName("media_owner_id")
    @Expose
    private String mediaOwnerId;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("landmarks")
    @Expose
    private String landmarks;
    @SerializedName("locality")
    @Expose
    private String locality;
    @SerializedName("site_facing_id")
    @Expose
    private String siteFacingId;
    @SerializedName("lattitude")
    @Expose
    private String lattitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("monitoring_type")
    @Expose
    private String monitoringType;
    @SerializedName("mounting_charge")
    @Expose
    private String mountingCharge;
    @SerializedName("traffic_count")
    @Expose
    private String trafficCount;
    @SerializedName("vendor_type")
    @Expose
    private String vendorType;
    @SerializedName("site_type_id")
    @Expose
    private String siteTypeId;
    @SerializedName("site_size")
    @Expose
    private String siteSize;
    @SerializedName("site_angle")
    @Expose
    private String siteAngle;
    @SerializedName("road_direction")
    @Expose
    private String roadDirection;
    @SerializedName("illumination_status")
    @Expose
    private String illuminationStatus;
    @SerializedName("lighting_type")
    @Expose
    private String lightingType;
    @SerializedName("site_rate")
    @Expose
    private String siteRate;
    @SerializedName("license")
    @Expose
    private String license;
    @SerializedName("site_marketer")
    @Expose
    private String siteMarketer;
    @SerializedName("site_position_id")
    @Expose
    private String sitePositionId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("added_by")
    @Expose
    private String addedBy;
    @SerializedName("added_date")
    @Expose
    private String addedDate;
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

    @SerializedName("is_Image_captured")
    @Expose
    private boolean isImageCaptured;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("range")
    @Expose
    private float range;

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isImageCaptured() {
        return isImageCaptured;
    }

    public void setImageCaptured(boolean imageCaptured) {
        isImageCaptured = imageCaptured;
    }

    public String getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(String allocationId) {
        this.allocationId = allocationId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSuperviserId() {
        return superviserId;
    }

    public void setSuperviserId(String superviserId) {
        this.superviserId = superviserId;
    }

    public String getSuperviserName() {
        return superviserName;
    }

    public void setSuperviserName(String superviserName) {
        this.superviserName = superviserName;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
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

    public String getMediaOwnerId() {
        return mediaOwnerId;
    }

    public void setMediaOwnerId(String mediaOwnerId) {
        this.mediaOwnerId = mediaOwnerId;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    public String getSiteFacingId() {
        return siteFacingId;
    }

    public void setSiteFacingId(String siteFacingId) {
        this.siteFacingId = siteFacingId;
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

    public String getMonitoringType() {
        return monitoringType;
    }

    public void setMonitoringType(String monitoringType) {
        this.monitoringType = monitoringType;
    }

    public String getMountingCharge() {
        return mountingCharge;
    }

    public void setMountingCharge(String mountingCharge) {
        this.mountingCharge = mountingCharge;
    }

    public String getTrafficCount() {
        return trafficCount;
    }

    public void setTrafficCount(String trafficCount) {
        this.trafficCount = trafficCount;
    }

    public String getVendorType() {
        return vendorType;
    }

    public void setVendorType(String vendorType) {
        this.vendorType = vendorType;
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

    public String getRoadDirection() {
        return roadDirection;
    }

    public void setRoadDirection(String roadDirection) {
        this.roadDirection = roadDirection;
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

    public String getSiteRate() {
        return siteRate;
    }

    public void setSiteRate(String siteRate) {
        this.siteRate = siteRate;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getSiteMarketer() {
        return siteMarketer;
    }

    public void setSiteMarketer(String siteMarketer) {
        this.siteMarketer = siteMarketer;
    }

    public String getSitePositionId() {
        return sitePositionId;
    }

    public void setSitePositionId(String sitePositionId) {
        this.sitePositionId = sitePositionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
