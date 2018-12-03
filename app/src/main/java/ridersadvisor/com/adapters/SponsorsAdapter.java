package ridersadvisor.com.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ridersadvisor.com.R;
import ridersadvisor.com.models.MySiteImageVo;
import ridersadvisor.com.utils.ImageUtility;

/**
 * Created by Sudhir Singh on 07,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class SponsorsAdapter extends BaseAdapter {

    private ArrayList<MySiteImageVo> sponsorsList;
    private Context context;
    private boolean isOffline;

    public SponsorsAdapter(Context context, ArrayList<MySiteImageVo> sponsorsList, boolean isOffline) {
        this.context = context;
        this.sponsorsList = sponsorsList;
        this.isOffline = isOffline;
    }

    // 2
    @Override
    public int getCount() {
        return sponsorsList.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // view holder pattern
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.site_detail_image_adapter, null);

            ImageView imgHomeIcon = (ImageView) convertView.findViewById(R.id.imgPropertyDtlAdap);

            viewHolder = new ViewHolder(imgHomeIcon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imgHomeIcon.setTag(position);

        try {

            if (isOffline) {

                Bitmap bitmap = ImageUtility.loadImageFromStorage(sponsorsList.get(position).getImage(), context);
                if (bitmap != null) {
                    viewHolder.imgHomeIcon.setImageBitmap(bitmap);
                } else {
                    Picasso.get().load(R.drawable.placeholder).into(viewHolder.imgHomeIcon);
                }
            } else {
                Picasso.get().load(sponsorsList.get(position).getImage()).fit().placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(viewHolder.imgHomeIcon);
            }

        } catch (Exception e) {

        }
//        Picasso.get().load("http://api.learn2crack.com/android/images/donut.png").placeholder(R.drawable.ic_photo_size_select_actual_white_48dp)
//                .error(R.drawable.ic_photo_size_select_actual_white_48dp)
//                .into(viewHolder.imgHomeIcon);

        return convertView;
    }

    private class ViewHolder {
        private final ImageView imgHomeIcon;

        private ViewHolder(ImageView imgHomeIcon) {
            this.imgHomeIcon = imgHomeIcon;
        }
    }
}
