package ridersadvisor.com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sudhir Singh on 22,March,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class MasterDataVo extends BaseVO implements Serializable{

    @SerializedName("media_owner")
    @Expose
    private ArrayList<MediaOwnerVo> mediaOwner = null;
    @SerializedName("site_facing")
    @Expose
    private ArrayList<MediaOwnerVo> siteFacing = null;
    @SerializedName("site_type")
    @Expose
    private ArrayList<MediaOwnerVo> siteType = null;
    @SerializedName("site_position")
    @Expose
    private ArrayList<MediaOwnerVo> sitePosition = null;
    @SerializedName("illumination_status")
    @Expose
    private ArrayList<IlluminationStatusVo> illuminationStatusVos = null;
    @SerializedName("lighting_type")
    @Expose
    private ArrayList<MediaOwnerVo> lightingType = null;

    @SerializedName("location")
    @Expose
    private ArrayList<MediaOwnerVo> location = null;


    public ArrayList<MediaOwnerVo> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<MediaOwnerVo> location) {
        this.location = location;
    }

    public ArrayList<MediaOwnerVo> getMediaOwner() {
        return mediaOwner;
    }

    public void setMediaOwner(ArrayList<MediaOwnerVo> mediaOwner) {
        this.mediaOwner = mediaOwner;
    }

    public ArrayList<MediaOwnerVo> getSiteFacing() {
        return siteFacing;
    }

    public void setSiteFacing(ArrayList<MediaOwnerVo> siteFacing) {
        this.siteFacing = siteFacing;
    }

    public ArrayList<MediaOwnerVo> getSiteType() {
        return siteType;
    }

    public void setSiteType(ArrayList<MediaOwnerVo> siteType) {
        this.siteType = siteType;
    }

    public ArrayList<MediaOwnerVo> getSitePosition() {
        return sitePosition;
    }

    public void setSitePosition(ArrayList<MediaOwnerVo> sitePosition) {
        this.sitePosition = sitePosition;
    }

    public ArrayList<IlluminationStatusVo> getIlluminationStatusVos() {
        return illuminationStatusVos;
    }

    public void setIlluminationStatusVos(ArrayList<IlluminationStatusVo> illuminationStatusVos) {
        this.illuminationStatusVos = illuminationStatusVos;
    }

    public ArrayList<MediaOwnerVo> getLightingType() {
        return lightingType;
    }

    public void setLightingType(ArrayList<MediaOwnerVo> lightingType) {
        this.lightingType = lightingType;
    }
}
