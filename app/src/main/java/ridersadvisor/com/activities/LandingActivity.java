package ridersadvisor.com.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import ridersadvisor.com.R;
import ridersadvisor.com.fragments.AddSiteFragment;
import ridersadvisor.com.fragments.CaptureSiteListFragment;
import ridersadvisor.com.fragments.CaptureSyncFragment;
import ridersadvisor.com.manager.SharedPrefManager;
import ridersadvisor.com.models.FeedParams;
import ridersadvisor.com.monitor_site.fragment.MonitorSiteListFragment;
import ridersadvisor.com.monitor_site.fragment.MonitorSyncFragment;
import ridersadvisor.com.utils.AddSiteDetailPrefs;
import ridersadvisor.com.utils.OfflineCaptureImagesPrefs;

import static ridersadvisor.com.monitor_site.fragment.MonitorSiteListFragment.MONITOR_IMAGE_CAPTURE_STATUS;

/**
 * Created by Sudhir Singh on 20,March,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class LandingActivity extends AppBaseActivity implements View.OnClickListener {

    Context context;

    private DrawerLayout mDrawerLayout;
    private NavigationView left_drawer;
    private Toolbar tb;
    private TextView txtPageHeader;

    private ActionBarDrawerToggle mDrawerToggle;
    private ImageView iv_sync_data;
    private ImageView img_profile_type;
    private ImageView imgListView;
    private ImageView imgMapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        context = LandingActivity.this;

        tb = (Toolbar) findViewById(R.id.tool_bar);
        tb.setTitleTextColor(getResources().getColor(R.color.white));
        tb.setTitle(getResources().getString(R.string.add_site_master));
        setSupportActionBar(tb);

        txtPageHeader = (TextView) findViewById(R.id.txtPageHeader);
        iv_sync_data = (ImageView) findViewById(R.id.iv_sync_data);
        img_profile_type = (ImageView) findViewById(R.id.img_profile_type);
        imgMapView = (ImageView) findViewById(R.id.imgMapView);
        imgListView = (ImageView) findViewById(R.id.imgListView);

        left_drawer = (NavigationView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        img_profile_type.setVisibility(View.GONE);
        String access = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ACCESS);
        if (access.equalsIgnoreCase(FeedParams.BOTH_ACCESS)) {
            img_profile_type.setVisibility(View.VISIBLE);
            SharedPrefManager.getInstance().setSharedData(FeedParams.SITE_CHOOSEN, FeedParams.MONITOR_SITE);
            left_drawer.getMenu().clear(); //clear old inflated items.
            left_drawer.inflateMenu(R.menu.monitor_drawer_menu);
        } else if (access.equalsIgnoreCase(FeedParams.MONITOR_SITE)) {
            SharedPrefManager.getInstance().setSharedData(FeedParams.SITE_CHOOSEN, FeedParams.MONITOR_SITE);
            left_drawer.getMenu().clear(); //clear old inflated items.
            left_drawer.inflateMenu(R.menu.monitor_drawer_menu);
        } else if (access.equalsIgnoreCase(FeedParams.CAPTURE_SITE)) {
            SharedPrefManager.getInstance().setSharedData(FeedParams.SITE_CHOOSEN, FeedParams.CAPTURE_SITE);
            left_drawer.getMenu().clear(); //clear old inflated items.
            left_drawer.inflateMenu(R.menu.capture_drawer_menu);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, tb, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        left_drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);

                switch (menuItem.getItemId()) {

                    case R.id.add_site_master:
                        selectItem(0);
                        break;

                    case R.id.my_site_view:
                        selectItem(1);
                        break;

                    case R.id.sync_site_data:
                        selectItem(2);
                        break;

                    case R.id.monitor_site_view:
                        selectItem(3);
                        break;

                    case R.id.monitor_sync_site_data:
                        selectItem(4);
                        break;


                    case R.id.logout:
                        selectItem(5);
                        break;
                }

                // close drawer when item is tapped
                mDrawerLayout.closeDrawers();
                return false;
            }
        });


        iv_sync_data.setOnClickListener(this);
        imgMapView.setOnClickListener(this);
        imgListView.setOnClickListener(this);
        img_profile_type.setOnClickListener(this);

        if (savedInstanceState == null) {
            if (SharedPrefManager.getInstance().getSharedDataString(FeedParams.SITE_CHOOSEN).equalsIgnoreCase(FeedParams.MONITOR_SITE)) {
                selectItem(3);
            } else {
                selectItem(0);
            }
        }
    }

    public void selectItem(int position) {
        Fragment fragment = null;
//        iv_delete_product.setVisibility(View.GONE);
//        iv_add_product.setVisibility(View.GONE);
//        imgNotification.setVisibility(View.GONE);

        switch (position) {
            case 0:
                txtPageHeader.setText(getResources().getString(R.string.add_site_master));
                iv_sync_data.setVisibility(View.GONE);
                imgMapView.setVisibility(View.GONE);
                imgListView.setVisibility(View.GONE);
                fragment = new AddSiteFragment();
                break;


            case 1:
                txtPageHeader.setText(getResources().getString(R.string.my_site_view));
                iv_sync_data.setVisibility(View.GONE);
                imgMapView.setVisibility(View.GONE);
                imgListView.setVisibility(View.GONE);
                fragment = new CaptureSiteListFragment();
                break;


            case 2:
                txtPageHeader.setText(getResources().getString(R.string.sync_site_data));
                iv_sync_data.setVisibility(View.VISIBLE);
                imgMapView.setVisibility(View.GONE);
                imgListView.setVisibility(View.GONE);
                fragment = new CaptureSyncFragment();
                break;

            case 3:
                //monitor site list
                txtPageHeader.setText(getResources().getString(R.string.my_site_view_monitor));
                iv_sync_data.setVisibility(View.GONE);
                imgMapView.setVisibility(View.VISIBLE);
                imgListView.setVisibility(View.GONE);
                fragment = new MonitorSiteListFragment();
                break;

            case 4://monitor unsync data
                txtPageHeader.setText(getResources().getString(R.string.sync_site_data));
                iv_sync_data.setVisibility(View.VISIBLE);
                imgMapView.setVisibility(View.GONE);
                imgListView.setVisibility(View.GONE);
                fragment = new MonitorSyncFragment();
                break;

            case 5:
                iv_sync_data.setVisibility(View.GONE);
                showLogoutPopUp();
                break;

        }

        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            transaction.commit();

//            FragmentManager fragmentManager = getFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            mDrawerLayout.closeDrawer(left_drawer);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MONITOR_IMAGE_CAPTURE_STATUS) {
            if(resultCode == RESULT_OK) {
                int positionClick = data.getIntExtra(FeedParams.POSITION_CLICKED,0);
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (fragment != null && fragment instanceof MonitorSiteListFragment) {
                    MonitorSiteListFragment monitorSiteListFragment = (MonitorSiteListFragment) fragment;
                    monitorSiteListFragment.refreshData(positionClick);
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_profile_type:

                displayUserOption();

                break;

            case R.id.iv_sync_data:

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (fragment != null && fragment instanceof CaptureSyncFragment) {
                    CaptureSyncFragment captureFragment = (CaptureSyncFragment) fragment;
                    captureFragment.sendData();
                } else if (fragment != null && fragment instanceof MonitorSyncFragment) {
                    MonitorSyncFragment monitorFragment = (MonitorSyncFragment) fragment;
                    monitorFragment.sendData();
                }

                break;

            case R.id.imgMapView:

                MonitorSiteListFragment monitorSiteListFragment = (MonitorSiteListFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (monitorSiteListFragment != null && monitorSiteListFragment instanceof MonitorSiteListFragment) {
                    monitorSiteListFragment.showMapView();
                    imgListView.setVisibility(View.VISIBLE);
                    imgMapView.setVisibility(View.GONE);
                }

                break;

            case R.id.imgListView:

                MonitorSiteListFragment ListFragment = (MonitorSiteListFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (ListFragment != null && ListFragment instanceof MonitorSiteListFragment) {
                    ListFragment.showListView();
                    imgListView.setVisibility(View.GONE);
                    imgMapView.setVisibility(View.VISIBLE);
                }

                break;

        }
    }

    private void displayUserOption() {

        final Dialog userAccessPopUp = new Dialog(LandingActivity.this, R.style.DialogUserAccessAnim);
        userAccessPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        userAccessPopUp.setContentView(R.layout.popup_user_access);
        userAccessPopUp.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        userAccessPopUp.getWindow().setGravity(Gravity.TOP);
        userAccessPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnSiteCapture = (Button) userAccessPopUp.findViewById(R.id.btnSiteCapture);
        Button btnSiteMonitoring = (Button) userAccessPopUp.findViewById(R.id.btnSiteMonitoring);

        final ImageView imgCapturingSelected = (ImageView) userAccessPopUp.findViewById(R.id.imgCapturingSelected);
        final ImageView imgMonitoringSelected = (ImageView) userAccessPopUp.findViewById(R.id.imgMonitoringSelected);

        if (SharedPrefManager.getInstance().getSharedDataString(FeedParams.SITE_CHOOSEN).equalsIgnoreCase(FeedParams.MONITOR_SITE)) {
            imgCapturingSelected.setVisibility(View.GONE);
            imgMonitoringSelected.setVisibility(View.VISIBLE);
        } else {
            imgCapturingSelected.setVisibility(View.VISIBLE);
            imgMonitoringSelected.setVisibility(View.GONE);
        }

        btnSiteMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance().setSharedData(FeedParams.SITE_CHOOSEN, FeedParams.MONITOR_SITE);
                left_drawer.getMenu().clear(); //clear old inflated items.
                left_drawer.inflateMenu(R.menu.monitor_drawer_menu);
                userAccessPopUp.dismiss();
                selectItem(3);
                imgCapturingSelected.setVisibility(View.GONE);
                imgMonitoringSelected.setVisibility(View.VISIBLE);
            }
        });
        btnSiteCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance().setSharedData(FeedParams.SITE_CHOOSEN, FeedParams.CAPTURE_SITE);
                left_drawer.getMenu().clear(); //clear old inflated items.
                left_drawer.inflateMenu(R.menu.capture_drawer_menu);
                userAccessPopUp.dismiss();
                imgCapturingSelected.setVisibility(View.VISIBLE);
                imgMonitoringSelected.setVisibility(View.GONE);
                selectItem(0);
            }
        });
        userAccessPopUp.show();

    }


    private void showLogoutPopUp() {
        final Dialog logoutPopUp = new Dialog(LandingActivity.this, R.style.DialogSlideAnim);
        logoutPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logoutPopUp.setContentView(R.layout.popup_logout);
        logoutPopUp.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        logoutPopUp.getWindow().setGravity(Gravity.CENTER);
        logoutPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button logoutNo = (Button) logoutPopUp.findViewById(R.id.logoutNoBtn);
        Button logoutYes = (Button) logoutPopUp.findViewById(R.id.logoutYesBtn);

        logoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logoutPopUp.isShowing()) {
                    logoutPopUp.dismiss();
                    logoutPopUp.cancel();
                }
            }
        });
        logoutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPrefManager.getInstance().clearPreference();
                deleteSaveData(context, FeedParams.SAVED_CAPTURE_SITE_FRAGMENT);
                deleteSaveData(context, FeedParams.MASTER_DATA);
                OfflineCaptureImagesPrefs.deleteAllOfflineImage(context);
                AddSiteDetailPrefs.deleteAllOfflineImage(context);

                Intent intent = new Intent(getApplicationContext(),
                        SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (logoutPopUp.isShowing()) {
                    logoutPopUp.dismiss();
                    logoutPopUp.cancel();
                }
                startActivity(intent);
                finish();
            }
        });
        logoutPopUp.show();
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
