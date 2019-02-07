package ksu.rgn.arcgis.jobs;

import ksu.rgn.arcgis.GISJob;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        String parameters = "&client_id=" + this.clientId;
        parameters += "&client_secret=" + this.clientSecret;
        parameters += "&expiration=" + (24 * 60);
        parameters += "&grant_type=" + "client_credentials";
        final JSONObject tokenResponse = request("/sharing/rest/oauth2/token", parameters);
        if(tokenResponse.has("access_token")) {
            future.invokeSuccess(tokenResponse.get("access_token"));
            LOG.info("Successfully retrieved token from the ArcGIS API");
        }
    }
}
