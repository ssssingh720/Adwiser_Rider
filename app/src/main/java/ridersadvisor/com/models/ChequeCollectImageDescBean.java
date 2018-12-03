package ridersadvisor.com.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jonu.kumar on 12-01-2018.
 */

public class ChequeCollectImageDescBean implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ChequeCollectImageDescBean> CREATOR = new Parcelable.Creator<ChequeCollectImageDescBean>() {
        @Override
        public ChequeCollectImageDescBean createFromParcel(Parcel in) {
            return new ChequeCollectImageDescBean(in);
        }

        @Override
        public ChequeCollectImageDescBean[] newArray(int size) {
            return new ChequeCollectImageDescBean[size];
        }
    };
    private String imageUri;
    private String chequeRemark;
    private Bitmap imgBitmap;

    public ChequeCollectImageDescBean() {
    }


    protected ChequeCollectImageDescBean(Parcel in) {
        imageUri = in.readString();
        chequeRemark = in.readString();
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getChequeRemark() {
        return chequeRemark;
    }

    public void setChequeRemark(String chequeRemark) {
        this.chequeRemark = chequeRemark;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUri);
        dest.writeString(chequeRemark);
    }
}
