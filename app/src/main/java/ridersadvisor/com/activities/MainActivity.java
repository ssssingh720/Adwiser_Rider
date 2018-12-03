package ridersadvisor.com.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ridersadvisor.com.R;
import ridersadvisor.com.fragments.AddSiteFragment;
import ridersadvisor.com.fragments.CaptureSiteListFragment;
import ridersadvisor.com.utils.Constants;
import ridersadvisor.com.utils.Fused;
import ridersadvisor.com.utils.GpsActivation;
import ridersadvisor.com.utils.TrackGPS;
import ridersadvisor.com.utils.Util;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private static final int REQUEST_PERMISSION_SETTING = 107;
    public static String title;
    private long mBackPressed;
    private int REQUEST_PERMISSION_GPS_SETTING = 5;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    private TrackGPS gps;
    private Fused fused;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        fused = new Fused(getApplicationContext(), 1);
        fused.onStart();
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        mNavigationView = (NavigationView) findViewById(R.id.left_drawer);
        View header = mNavigationView.getHeaderView(0);

        setSupportActionBar(toolbar);
        initReplaceFragment(new AddSiteFragment(), "Home");

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
        initKeyBoardListener();
        getLocation();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer != null && mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
        try {
            popFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void popFragment() {
        try {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (f instanceof AddSiteFragment) {
                if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                    finish();
                } else {
                    Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setTitle("Home");
//                    mNavigationView.setCheckedItem(R.id.nav_home);

                }
            } else {
                getSupportFragmentManager().popBackStack();
                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setTitle("Home");
//                    mNavigationView.setCheckedItem(R.id.nav_home);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBackPressed = System.currentTimeMillis();
    }

    public boolean isFragmentOnTop(Class<?> type) {
        try {
            Fragment fr = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (fr != null) {
                if (fr.getClass().getSimpleName().equals(type.getSimpleName())) {
                    Log.d("Constants.LOG_TAG", "fragment already on top");
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void initAddFragment(Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(title);
//        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left, R.anim.in_from_left,
// R.anim.out_to_right).
        fragmentTransaction.replace(R.id.content_frame, fragment).commit();
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }

    public void initReplaceFragment(Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }

    public void setScreenTitle(String title) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.add_site_master:
//                if (!isFragmentOnTop(AddSiteFragment.class)) {
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    initReplaceFragment(new AddSiteFragment(), "Home");
//                }
                break;
            case R.id.my_site_view:
                if (!isFragmentOnTop(CaptureSiteListFragment.class)) {
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    initAddFragment(new CaptureSiteListFragment(), "Exhibitors");
                }
                break;

//            case R.id.nav_favourites:
//                if (!isFragmentOnTop(FavExhibitorListFragment.class)) {
//                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    FavExhibitorListFragment fragment = new FavExhibitorListFragment();
//                    Bundle bundleFavExhibitor = new Bundle();
//                    bundleFavExhibitor.putString(BundleKeys.BUNDLE_FAV_EXHIBITOR_GET_QUERY, SqliteQuery.GET_ALL_FAV_EXHIBITOR_QUERY);
//                    fragment.setArguments(bundleFavExhibitor);
//                    initAddFragment(fragment, "My Favourite");
//                }
//                break;

//            case R.id.nav_planner:
//                if (!isFragmentOnTop(PlannerFragment.class)) {
//                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    initAddFragment(new PlannerFragment(), "Planner");
//                }
//                break;
//
//            case R.id.nav_register:
//                UserPref pref = new UserPref(MainActivity.this);
//                String reg_id = pref.getStringData(Constants.REGISTRATION_ID);
//                if (reg_id != null && !reg_id.isEmpty()) {
//                    showDialoge();
//                } else {
//                    if (!isFragmentOnTop(RegistrationFormFragment.class)) {
//                        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                        initAddFragment(new RegistrationFormFragment(), "Visitor Registration");
//
//                    }
//                }
//                break;
//            case R.id.nav_product_notes:
//                if (!isFragmentOnTop(ExhibitorsListingWithAlbumFragment.class)) {
//                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    initAddFragment(new ExhibitorsListingWithAlbumFragment(), "Product Note");
//                }
//                break;
//            case R.id.nav_feedback:
//                if (!isFragmentOnTop(FeedbackFragment.class)) {
//                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    initAddFragment(new FeedbackFragment(), "Feedback");
//                }
//                break;
//            case R.id.nav_info:
//                getExternalRedWritePermission();
//                break;
//            case R.id.nav_social_media:
//                if (!isFragmentOnTop(SocialMediaFragment.class)) {
//                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    initAddFragment(new SocialMediaFragment(), "Social Media");
//                }
//                break;
//
//            case R.id.nav_seminar:
//                if (!isFragmentOnTop(SeminarFragment.class)) {
//                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    initAddFragment(new SeminarFragment(), "Seminar");
//                }
//                break;
//            case R.id.nav_venue_reach:
//                getLocation();
//                break;
//            case R.id.nav_update_data:
//            /*    firstLaunch = new UserPref(MainActivity.this);
//                int currentVersionCode = getAppVersionCode();
//                int oldVersion=firstLaunch.getIntData(Constants.APP_VERSION_CODE_FOR_UPATE_DATA);
//                if (currentVersionCode>oldVersion) {
//                    firstLaunch.setIntData(Constants.APP_VERSION_CODE, currentVersionCode);
//                    firstLaunch.clearKey(Constants.FIRST_APP_LAUNCH_COMPLETED);
//                }
//
//                boolean firstLaunchCompeted = firstLaunch.getPrefBooleanValue(Constants.FIRST_APP_LAUNCH_COMPETED_FOR_UPDATE_DATA);
//                if (!firstLaunchCompeted) {*/
//                    final Dialog alert = new Dialog(MainActivity.this);
//                    alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    alert.setContentView(R.layout.dialog_alert);
//                    alert.setCancelable(false);
//
//                    TextView txtAlertTitle = (TextView) alert.findViewById(R.id.txtAlertTitle);
//                    TextView txtAlertDesc = (TextView) alert.findViewById(R.id.txtAlertDesc);
//
//                    txtAlertTitle.setText("Update Data");
//                    txtAlertDesc.setText("The App requires a one-time load, extraction and import of the data of all exhibitors. This may take up to one minute and should not be interrupted. Please update your App data again before your visit to the Fair. The App can also be used offline without an active Internet connection.");
//                    Button btnYes = (Button) alert.findViewById(R.id.btnYes);
//
//                    btnYes.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            alert.dismiss();
////                            firstLaunch.setPrefBooleanValue(Constants.FIRST_APP_LAUNCH_COMPETED_FOR_UPDATE_DATA, true);
//                            if (Utils.isOnline(MainActivity.this)) {
//                                startActivity(new Intent(MainActivity.this, UpdateDataActivity.class));
//                            } else {
//                                Toast.makeText(MainActivity.this, "Network connection not available!", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//                    alert.show();
//             /*   } else {
//
//                    firstLaunch.setPrefBooleanValue(Constants.FIRST_APP_LAUNCH_COMPETED_FOR_UPDATE_DATA, true);
//                    if (Utils.isOnline(MainActivity.this)) {
//                        startActivity(new Intent(MainActivity.this, UpdateDataActivity.class));
//                    } else {
//                        Toast.makeText(MainActivity.this, "Network connection not available!", Toast.LENGTH_SHORT).show();
//                    }
//
//                }*/
//
//                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Util.checkPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                callLocationManager();
                getUpdatedLocation();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.ACCESS_FINE_LOCATION_PERMISSION_CONSTANT);
                } else {
                    openSettingForGPS();
                }
            }
        } else {
            getUpdatedLocation();
        }
    }


    private void getUpdatedLocation() {
        gps = new TrackGPS(MainActivity.this);
        if (gps.canGetLocation()) {
            if (fused.lon != null && fused.lat != null && fused.lon != "0.0" && fused.lat != "0.0") {
                double longitude = Double.parseDouble(fused.lon);
                double latitude = Double.parseDouble(fused.lat);
//                if (!isFragmentOnTop(DirectionFragment.class)) {
//                    LatLng sourceLatLng = new LatLng(Constants.LATITUDE_EPCH, Constants.LONGITUDE_EPCH);
//                    LatLng destinationLatLong = new LatLng(latitude, longitude);
//                    String address = getCompleteAddressString(latitude, longitude);
//                    NearByPlaceModel mModel = new NearByPlaceModel();
//                    mModel.setPlaceName(address);
//                    DirectionFragment fragment = new DirectionFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable(BundleKeys.BUNDLE_SOURCE_LATLONG, sourceLatLng);
//                    bundle.putBoolean(BundleKeys.BUNDLE_FROM_VENUE_MAP, true);
//                    bundle.putParcelable(BundleKeys.BUNDLE_PLACE_INFO, mModel);
//                    bundle.putParcelable(BundleKeys.BUNDLE_DESTINATION_LATLONG, destinationLatLong);
//                    fragment.setArguments(bundle);
//                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    initAddFragment(fragment, "Routes");
//                }


                Toast.makeText(MainActivity.this, "Longitude:" + fused.lon + "\nfused.lat:" + fused.lat, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Not Able to get location_pointer.please turn on your GPS", Toast.LENGTH_SHORT).show();
            }
        } else {
            new GpsActivation(MainActivity.this).enableGPS();
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        Geocoder geocoder;
        String address = "";
        List<Address> addresses = new ArrayList<>();
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            address = addresses.get(0).getAddressLine(0);
        }
        // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
  /*  String city = addresses.get(0).getLocality();
    String state = addresses.get(0).getAdminArea();
    String country = addresses.get(0).getCountryName();
    String postalCode = addresses.get(0).getPostalCode();
    String knownName = addresses.get(0).getFeatureName();*/
        return address;
    }

    private void openSettingForGPS() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
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
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    private void getExternalRedWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Util.checkPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) && Util.checkPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                launchFragment();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.ACCESS_FINE_LOCATION_PERMISSION_CONSTANT);
                } else {
                    openSettingForCamraAndExternal();
                }
            }
        } else {
            launchFragment();
        }
    }

    private void openSettingForCamraAndExternal() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle(" External Read Write Permission Required For This Event");
        alertDialog.setMessage("Show External Permission settings?");

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void launchFragment() {
        if (!isFragmentOnTop(AddSiteFragment.class)) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            initAddFragment(new CaptureSiteListFragment(), "Fair Information");
        }

    }


    private void initKeyBoardListener() {
        // Threshold for minimal keyboard height.
        final int MIN_KEYBOARD_HEIGHT_PX = 150;
        //  Top-level window decor view.
        final View decorView = getWindow().getDecorView();
        // Register global layout listener.
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            // Retrieve visible rectangle inside window.
            private final Rect windowVisibleDisplayFrame = new Rect();
            private int lastVisibleDecorViewHeight;

            @Override
            public void onGlobalLayout() {
                decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
                final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();

                if (lastVisibleDecorViewHeight != 0) {
//                    bottom add block
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                    }
                }
                // Save current decor view height for the next call.
                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
