package ksu.rgn.arcgis.jobs;

import com.darkyen.dave.WebbException;
import ksu.rgn.arcgis.GISJob;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class TestConnectionJ extends GISJob {

    private static final Logger LOG = LoggerFactory.getLogger(TestConnectionJ.class);

    @Override
    public void run() {
        try {

            final JSONObject r = request("/ArcGIS/rest/info/healthCheck", null);
            if ((boolean) r.get("success")) {
                future.invokeSuccess("Server is running");
            } else {
                future.invokeFail("Could not contact server");
            }

        } catch (WebbException we) {
            LOG.warn("GIS Server could not be reached");
            future.invokeFail("The server could not be reached");
        } catch (JSONException e) {
            LOG.warn("GIS Server replied with unexpected response");
            LOG.debug("Cause: {}", e);
            future.invokeFail("Unexpected response");
        }
    }

}
