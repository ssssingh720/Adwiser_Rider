package ridersadvisor.com.models;

import com.android.volley.VolleyError;

/**
 * Created by Sudhir Singh on 2/5/15.
 */
public class ResponseError extends VolleyError {

    private String errorMessage = "";
    private boolean isErrorOccured=false;

    public boolean isErrorOccured() {
        return isErrorOccured;
    }

    public void setErrorOccured(boolean errorOccured) {
        isErrorOccured = errorOccured;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
