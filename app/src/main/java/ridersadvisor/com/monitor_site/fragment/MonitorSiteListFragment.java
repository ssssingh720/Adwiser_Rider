package ridersadvisor.com.monitor_site.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.squareup.picasso.Picasso;
import com.stone.vega.library.VegaLayoutManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import ridersadvisor.com.R;
import ridersadvisor.com.fragments.BaseFragment;
import ridersadvisor.com.manager.SharedPrefManager;
import ridersadvisor.com.models.FeedParams;
import ridersadvisor.com.models.MySiteImageVo;
import ridersadvisor.com.models.ResponseError;
import ridersadvisor.com.monitor_site.adapters.MonitorSiteListAdapter;
import ridersadvisor.com.monitor_site.adapters.MyItem;
import ridersadvisor.com.monitor_site.modal.MonitorSiteDetailVo;
import ridersadvisor.com.monitor_site.modal.MonitorSiteVo;
import ridersadvisor.com.monitor_site.prefs.OfflineMonitorImagePrefs;
import ridersadvisor.com.utils.APIMethods;
import ridersadvisor.com.utils.Constants;
import ridersadvisor.com.utils.Fused;
import ridersadvisor.com.utils.GpsActivation;
import ridersadvisor.com.utils.TrackGPS;
import ridersadvisor.com.utils.Util;

import static ridersadvisor.com.utils.Constants.REQUEST_PERMISSION_GPS_SETTING;

/**
 * Created by Sudhir Singh on 24,April,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class MonitorSiteListFragment extends BaseFragment implements View.OnClickListener
        , OnMapReadyCallback, ClusterManager.OnClusterClickListener<MyItem>,
        ClusterManager.OnClusterInfoWindowClickListener<MyItem>, ClusterManager.OnClusterItemClickListener<MyItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<MyItem> {

    public static final int MONITOR_IMAGE_CAPTURE_STATUS = 3000;

    private List<MonitorSiteDetailVo> mySiteDetailList;
    private MonitorSiteListAdapter mySiteAdapter;

    private RecyclerView rclMySite;
    private Context context;
    private ProgressDialog pDialog;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private ClusterManager<MyItem> mClusterManager;

    private Fused fused;
    private double longitude = 0;
    private double latitude = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.my_site_fragment, container, false);

        mySiteDetailList=new ArrayList<>();
        context = getActivity();
        fused = new Fused(context, 1);
        fused.onStart();

        rclMySite = (RecyclerView) mView.findViewById(R.id.rclMySite);
        rclMySite.setLayoutManager(new VegaLayoutManager());

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapFragment.getView().setVisibility(View.GONE);

        getLocation();

        if (Util.isNetworkOnline(context)) {

            try {
                MonitorSiteVo listDat = (MonitorSiteVo) readSaveSiteData(context, FeedParams.SAVED_MONITOR_SITE_FRAGMENT);
                if (listDat != null && listDat.getData() != null) {
                    mySiteDetailList = listDat.getData();

                    if (latitude == 0 || longitude == 0) {
                        getLocation();
                    }

                    mySiteDetailList = listDat.getData();

                    sortLocations(mySiteDetailList, latitude, longitude);
                    setMonitorSiteAdapter();
//                    mySiteAdapter = new MonitorSiteListAdapter(getActivity(), mySiteDetailList);
//                    rclMySite.setAdapter(mySiteAdapter);
                    addMarkers(mySiteDetailList);
                }
            } catch (Exception e) {

            }

            getMonitorSiteList();

        } else {

            try {
                MonitorSiteVo listDat = (MonitorSiteVo) readSaveSiteData(context, FeedParams.SAVED_MONITOR_SITE_FRAGMENT);
                if (listDat != null && listDat.getData() != null) {
                    mySiteDetailList = listDat.getData();

                    if (latitude == 0 || longitude == 0) {
                        getLocation();
                    }

                    mySiteDetailList = listDat.getData();

                    sortLocations(mySiteDetailList, latitude, longitude);

                    setMonitorSiteAdapter();

//                    if (mySiteAdapter == null) {
//                        mySiteAdapter = new MonitorSiteListAdapter(getActivity(), mySiteDetailList);
//                        rclMySite.setAdapter(mySiteAdapter);
                    addMarkers(mySiteDetailList);
//                    } else {
//                        mySiteAdapter.notifyDataSetChanged();
//                    }

                } else {
                    showToast(getResources().getString(R.string.no_internet));
                }
            } catch (Exception e) {
                showToast(getResources().getString(R.string.no_internet));
            }
        }
        return mView;

    }

    public void showMapView() {
        mapFragment.getView().setVisibility(View.VISIBLE);
        rclMySite.setVisibility(View.GONE);
    }

    public void showListView() {
        mapFragment.getView().setVisibility(View.GONE);
        rclMySite.setVisibility(View.VISIBLE);
    }

    public void refreshData(int positionClick) {
        mySiteDetailList.get(positionClick).setImageCaptured(true);
        setMonitorSiteAdapter();
//        mySiteAdapter = new MonitorSiteListAdapter(getActivity(), mySiteDetailList);
//        rclMySite.setAdapter(mySiteAdapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        mClusterManager = new ClusterManager<MyItem>(context, googleMap);
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            }
        }else{
            googleMap.setMyLocationEnabled(true);
        }
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);

        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

    }

    private void addMarkers(List<MonitorSiteDetailVo> mySiteDetailList) {

        for (int counter = 0; counter < mySiteDetailList.size(); counter++) {

            MonitorSiteDetailVo mySiteDetailVo = mySiteDetailList.get(counter);
            try {
                double latitude = Double.parseDouble(mySiteDetailVo.getLattitude());
                double longitude = Double.parseDouble(mySiteDetailVo.getLongitude());

                MyItem infoWindowItem = new MyItem(latitude, longitude, mySiteDetailVo.getSiteCode(), mySiteDetailVo.getLandmarks(),
                        mySiteDetailVo.getMediaOwner(), mySiteDetailVo.getImage(), mySiteDetailVo.getLocality());
                mClusterManager.addItem(infoWindowItem);

                if (counter == 0) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mClusterManager.cluster();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


        }
    }

    private void getMonitorSiteList() {

        if (Util.isNetworkOnline(context)) {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage(getString(R.string.fetching_data));
            pDialog.setCancelable(false);
            pDialog.show();
//userid, usertoken , media_owner_id, landmarks, locality, site_facing_id, site_type_id, site_size,
// site_angel, road_direction, illumination_status, lighting_type, license, site_position_id, lattitude, longitude
            HashMap<String, String> params = new HashMap<>();
            params.put(FeedParams.USERID, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USERID));
            params.put(FeedParams.USERTOKEN, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USERTOKEN));

            placeRequest(APIMethods.GET_MONITOR_SITE_LISTING, MonitorSiteVo.class, params, true, null);

        } else {
            showToast(R.string.no_internet);
        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.GET_MONITOR_SITE_LISTING)) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            try {

                MonitorSiteVo resultData = (MonitorSiteVo) response;

                if (latitude == 0 || longitude == 0) {
                    getLocation();
                }

                mySiteDetailList = resultData.getData();

                sortLocations(mySiteDetailList, latitude, longitude);

                setMonitorSiteAdapter();

                saveSiteData(context, resultData, FeedParams.SAVED_MONITOR_SITE_FRAGMENT);
                addMarkers(mySiteDetailList);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setMonitorSiteAdapter() {
//        if (mySiteAdapter == null) {
        if(mySiteDetailList!=null) {
            for (int counter = 0; counter < mySiteDetailList.size(); counter++) {
                ArrayList<MySiteImageVo> offlineImageList = OfflineMonitorImagePrefs.getOfflineImage(context, mySiteDetailList.get(counter).getSiteId());

                for (int imageCounter = 0; imageCounter < offlineImageList.size(); imageCounter++) {
                    if (offlineImageList.get(imageCounter).isTaskComplete()) {
                        mySiteDetailList.get(counter).setImageCaptured(true);
                        break;
                    }
                }
            }
            mySiteAdapter = new MonitorSiteListAdapter(getActivity(), mySiteDetailList,latitude,longitude);
            rclMySite.setAdapter(mySiteAdapter);
        }
//        } else {
//            mySiteAdapter.notifyDataSetChanged();
//        }
    }


    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }

        if (apiMethod.equalsIgnoreCase(APIMethods.GET_MONITOR_SITE_LISTING)) {
            setMonitorSiteAdapter();
            ResponseError responseError = (ResponseError) error;
            showToast(responseError.getErrorMessage());
            deleteSaveData(context, FeedParams.SAVED_CAPTURE_SITE_FRAGMENT);

            if (mySiteDetailList != null) {
                mySiteDetailList.clear();
            }

            mySiteAdapter = new MonitorSiteListAdapter(getActivity(), mySiteDetailList,latitude,longitude);
            rclMySite.setAdapter(mySiteAdapter);

        }
    }


    private synchronized void deleteSaveData(Context context, String binFileName) {
        try {
            String tempPath = context.getFilesDir() + "/" + binFileName + ".bin";
            File file = new File(tempPath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showToast("Fragment");
    }

    private synchronized void saveSiteData(Context context, Object object, String binFileName) {
        try {
            String tempPath = context.getFilesDir() + "/" + binFileName + ".bin";
            File file = new File(tempPath);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(object);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized Object readSaveSiteData(Context context, String binFileName) {
        Object obj = new Object();
        try {
            String tempPath = context.getFilesDir() + "/" + binFileName + ".bin";
            File file = new File(tempPath);
            if (file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                obj = ois.readObject();
                ois.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public boolean onClusterClick(Cluster<MyItem> cluster) {
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MyItem> cluster) {
        String firstName = cluster.getItems().iterator().next().getSnippet();
        showToast(firstName);
    }

    @Override
    public boolean onClusterItemClick(MyItem myItem) {
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(MyItem myItem) {
        showToast(myItem.getTitle());
        showInfoDialog(myItem);
    }

    private void showInfoDialog(MyItem myItem) {

        final Dialog infoDialog = new Dialog(context);
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setCancelable(false);

        infoDialog.setContentView(R.layout.my_site_info_dialog);

        TextView txtDlgTitle = (TextView) infoDialog.findViewById(R.id.txtDlgTitle);
        TextView txtDlgTLocation = (TextView) infoDialog.findViewById(R.id.txtDlgLocation);
        TextView txtDlgDesc = (TextView) infoDialog.findViewById(R.id.txtDlgDesc);
        ImageView imgCloseDialog = (ImageView) infoDialog.findViewById(R.id.imgCloseDialog);
        ImageView imgSiteMap = (ImageView) infoDialog.findViewById(R.id.imgSiteMap);

        Picasso.get().load(myItem.getImages()).fit().placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imgSiteMap);

        txtDlgTitle.setText(myItem.getTitle());
        txtDlgTLocation.setText(myItem.getLandmarks());
        txtDlgDesc.setText(myItem.getMediaOwner());

        imgCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDialog.dismiss();
            }
        });

        infoDialog.show();
    }

    private List<MonitorSiteDetailVo> sortLocations(List<MonitorSiteDetailVo> mySiteDetailList, final double myLatitude, final double myLongitude) {
        Comparator comp = new Comparator<MonitorSiteDetailVo>() {
            @Override
            public int compare(MonitorSiteDetailVo o, MonitorSiteDetailVo o2) {
                float[] result1 = new float[3];

                android.location.Location.distanceBetween(myLatitude, myLongitude, Double.valueOf(o.getLattitude()), Double.valueOf(o.getLongitude()), result1);
                Float distance1 = result1[0];

                float[] result2 = new float[3];
                android.location.Location.distanceBetween(myLatitude, myLongitude, Double.valueOf(o2.getLattitude()), Double.valueOf(o2.getLongitude()), result2);
                Float distance2 = result2[0];

                return distance1.compareTo(distance2);
            }
        };

        Collections.sort(mySiteDetailList, comp);
        return mySiteDetailList;
    }

    private void getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Util.checkPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
//                callLocationManager();
                getUpdatedLocation();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.ACCESS_FINE_LOCATION_PERMISSION_CONSTANT);
                } else {
                    openSettingForGPS();
                }
            }
        } else {
            getUpdatedLocation();
        }
    }

    private void openSettingForGPS() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialog.setTitle("GPS is disabled");
        alertDialog.setMessage("Show location_pointer settings?");

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, REQUEST_PERMISSION_GPS_SETTING);

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//                Utils.stopProgress(progressDialog);
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void getUpdatedLocation() {
        TrackGPS gps = new TrackGPS(context);

        if (gps.canGetLocation()) {
            if (fused.lon != null && fused.lat != null && fused.lon != "0.0" && fused.lat != "0.0") {
                longitude = Double.parseDouble(fused.lon);
                latitude = Double.parseDouble(fused.lat);
            }
        } else {
            new GpsActivation(getActivity()).enableGPS();
        }
    }

}
