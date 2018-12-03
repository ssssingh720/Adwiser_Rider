package ridersadvisor.com.monitor_site.adapters;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.ArrayList;

import ridersadvisor.com.models.MySiteImageVo;

/**
 * Created by Sudhir Singh on 25,April,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class MyItem implements ClusterItem {

    private LatLng mPosition;
    private String mTitle;
    private String landmarks;
    private String mediaOwner;
    private String mSnippet;
    private String images;

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public MyItem(double lat, double lng, String title,String landmarks,String mediaOwner,String images,
                  String snippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        this.landmarks=landmarks;
        this.mediaOwner=mediaOwner;
        this.images=images;
        mSnippet = snippet;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getMediaOwner() {
        return mediaOwner;
    }

    public String getLandmarks() {
        return landmarks;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }


}
