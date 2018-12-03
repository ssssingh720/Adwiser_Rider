package ridersadvisor.com.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ridersadvisor.com.R;
import ridersadvisor.com.customViews.ZoomImageView;
import ridersadvisor.com.fragments.DepthPageTransformer;
import ridersadvisor.com.models.FeedParams;
import ridersadvisor.com.models.MySiteImageVo;
import ridersadvisor.com.utils.ImageUtility;

public class DetailActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private ArrayList<MySiteImageVo> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        data = (ArrayList<MySiteImageVo>) getIntent().getExtras().get("data");
        int pos = getIntent().getIntExtra("pos", 0);
        boolean isOffline = getIntent().getBooleanExtra(FeedParams.IS_OFFLINE_MODE, false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        setTitle(data.get(pos).getId());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), data, isOffline);

        // Set up the ViewPager with the sections adapter.
        LinearLayout lntSiteDetailBack = (LinearLayout) findViewById(R.id.lntSiteDetailBack);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(pos);

        lntSiteDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onBackPressed();


            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //noinspection ConstantConditions
                setTitle(data.get(position).getId());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_IMG_TITLE = "image_title";
        private static final String ARG_IMG_URL = "image_url";
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        String name, url;
        int pos;
        boolean isOffline;

        Context mContext;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            mContext=context;
        }

        public PlaceholderFragment() {
            mContext = getActivity();
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String name, String url, boolean isOffline) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_IMG_TITLE, name);
            args.putBoolean(FeedParams.IS_OFFLINE_MODE, isOffline);
            args.putString(ARG_IMG_URL, url);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
            this.pos = args.getInt(ARG_SECTION_NUMBER);
            this.name = args.getString(ARG_IMG_TITLE);
            this.url = args.getString(ARG_IMG_URL);
            this.isOffline = args.getBoolean(FeedParams.IS_OFFLINE_MODE, false);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.zoom_image, container, false);
            final ZoomImageView imageView = (ZoomImageView) rootView.findViewById(R.id.detail_image);

            try {

                if (isOffline && mContext != null) {

                    Bitmap bitmap = ImageUtility.loadImageFromStorage(url, mContext);
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Picasso.get().load(R.drawable.placeholder).into(imageView);
                    }
                } else {
                    Picasso.get().load(url).placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(imageView);
                }

            } catch (Exception e) {

            }


//            Glide.with(getActivity()).load(url).thumbnail(0.1f).into(imageView);

            return rootView;

        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public ArrayList<MySiteImageVo> data = new ArrayList<>();
        private boolean isOffline;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<MySiteImageVo> data, boolean isOffline) {
            super(fm);
            this.data = data;
            this.isOffline = isOffline;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position, data.get(position).getId(), data.get(position).getImage(), isOffline);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return data.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return data.get(position).getId();
        }
    }
}
