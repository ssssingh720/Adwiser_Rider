package ridersadvisor.com.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ridersadvisor.com.R;
import ridersadvisor.com.models.MySiteImageVo;

/**
 * Created by Sudhir Singh on 28,March,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class PropertyDetailAdapter extends RecyclerView.Adapter<PropertyDetailAdapter.ViewHolder> {
    private List<MySiteImageVo> images;
    private Context context;

    public PropertyDetailAdapter(Context context, List<MySiteImageVo> images) {
        this.images = images;
        this.context = context;
    }

    @Override
    public PropertyDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.site_detail_image_adapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PropertyDetailAdapter.ViewHolder viewHolder, int i) {

//        Picasso.get().load(images.get(i).getImage()).placeholder(R.drawable.ic_photo_size_select_actual_white_48dp)
//                .error(R.drawable.ic_photo_size_select_actual_white_48dp)
//                .into(viewHolder.imgPropertyDtlAdap);

        Picasso.get().load("http://api.learn2crack.com/android/images/donut.png").placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(viewHolder.imgPropertyDtlAdap);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPropertyDtlAdap;

        public ViewHolder(View view) {
            super(view);

            imgPropertyDtlAdap = (ImageView) view.findViewById(R.id.imgPropertyDtlAdap);
        }
    }
}
