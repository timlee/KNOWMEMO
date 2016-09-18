package knowmemoAPI;

import org.json.JSONObject;

/**
 * Created by ShiangChiLee on 2016/3/13.
 */
public abstract class knowmemoCallBackListener {

    public abstract void onSuccess(JSONObject result);
    public abstract void onFail(JSONObject result);
}
