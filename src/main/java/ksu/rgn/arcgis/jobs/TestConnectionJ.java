package ksu.rgn.arcgis.jobs;

import com.darkyen.dave.WebbException;
import ksu.rgn.arcgis.GISJob;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ksu.rgn.utils.Future;

/**
 *
 */
public class TestConnectionJ extends GISJob {

    private final String clientId, clientSecret;

    public TestConnectionJ(String clientId, String clientSecret){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public void run() {
        Future f = bridge.addJob(new RequestTmpTokenJ(clientId, clientSecret));
        f.onSuccess(o -> {
            future.invokeSuccess("Successful connection to the ArcGIS database");
        });
        f.onFail(o -> {
            future.invokeFail("Failed to connect to the ArcGIS database");
        });
    }
}
