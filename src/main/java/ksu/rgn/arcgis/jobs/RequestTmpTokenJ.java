package ksu.rgn.arcgis.jobs;

import ksu.rgn.arcgis.GISJob;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 *
 */
public class RequestTmpTokenJ extends GISJob {

    private static final Logger LOG = LoggerFactory.getLogger(RequestTmpTokenJ.class);
    private String clientId;
    private String clientSecret;

    public RequestTmpTokenJ(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public void run() {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("client_id", clientId);
        parameters.put("client_secret", clientSecret);
        parameters.put("expiration", Integer.toString(24 * 60));
        parameters.put("grant_type", "client_credentials");

        final JSONObject tokenResponse = request("/sharing/rest/oauth2/token", null, parameters);
        if(tokenResponse.has("access_token")) {
            future.invokeSuccess(tokenResponse.get("access_token"));
            LOG.info("Successfully retrieved token from the ArcGIS API");
        } else {
            future.invokeFail("Invalid response format");
            LOG.warn("Unable to retrieve token from ArcGIS API");
        }
    }
}
