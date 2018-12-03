package ridersadvisor.com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sudhir Singh on 28,March,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class MySiteImageVo implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;


    @SerializedName("allocation_id")
    @Expose
    private String allocation_id;

    @SerializedName("site_id")
    @Expose
    private String siteId;

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("lattitude")
    @Expose
    private String lattitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("indate")
    @Expose
    private String indate;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("capture_time")
    @Expose
    private String capture_time;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("isOnlyImage")
    @Expose
    private boolean isOnlyImage;

    @SerializedName("isTaskComplete")
    @Expose
    private boolean isTaskComplete;

    @SerializedName("type")
    @Expose
    private String type;


    public MySiteImageVo() {

    }

    public MySiteImageVo(String siteId,String allocation_id, String image, String capture_time, String lattitude, String longitude,
                         String name, String remarks,boolean isOnlyImage,boolean isTaskComplete,String type) {
        this.siteId = siteId;
        this.allocation_id=allocation_id;
        this.image = image;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.name = name;
        this.capture_time = capture_time;
        this.remarks = remarks;
        this.isOnlyImage=isOnlyImage;
        this.isTaskComplete=isTaskComplete;
        this.type=type;
    }

    public String getAllocation_id() {
        return allocation_id;
    }

    public void setAllocation_id(String allocation_id) {
        this.allocation_id = allocation_id;
    }

    public boolean isTaskComplete() {
        return isTaskComplete;
    }

    public void setTaskComplete(boolean taskComplete) {
        isTaskComplete = taskComplete;
    }

    public boolean isOnlyImage() {
        return isOnlyImage;
    }

    public void setOnlyImage(boolean onlyImage) {
        isOnlyImage = onlyImage;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCapture_time() {
        return capture_time;
    }

    public void setCapture_time(String capture_time) {
        this.capture_time = capture_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getIndate() {
        return indate;
    }

    public void setIndate(String indate) {
        this.indate = indate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
