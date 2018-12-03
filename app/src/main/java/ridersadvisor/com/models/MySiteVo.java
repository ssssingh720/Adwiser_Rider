package ridersadvisor.com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sudhir Singh on 28,March,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class MySiteVo extends BaseVO implements Serializable {

    @SerializedName("data")
    @Expose
    private List<MySiteDetailVo> data = null;

    public List<MySiteDetailVo> getData() {
        return data;
    }

    public void setData(List<MySiteDetailVo> data) {
        this.data = data;
    }
}
