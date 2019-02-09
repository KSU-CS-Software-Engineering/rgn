package ksu.rgn.arcgis.jobs;

import com.darkyen.dave.WebbException;
import ksu.rgn.arcgis.GISJob;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 *
 */
public class RequestTmpTokenJ extends GISJob {

    public static final int EXPIRATION = 24 * 60;

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
        parameters.put("expiration", Integer.toString(EXPIRATION));
        parameters.put("grant_type", "client_credentials");

        try {
            final JSONObject tokenResponse = request("/sharing/rest/oauth2/token", null, parameters);
            if (tokenResponse.has("access_token")) {
                future.invokeSuccess(tokenResponse.get("access_token"));
                LOG.info("Retrieved token");
            } else if (tokenResponse.has("error")) {
                future.invokeFail(tokenResponse.getJSONObject("error").getString("message"));
                LOG.warn("Request failed: {}", tokenResponse.getJSONObject("error").getString("message"));
            }
        } catch (JSONException jse) {
            future.invokeFail("Invalid response");
            LOG.warn("Invalid response format");
        } catch (WebbException w) {
            future.invokeFail("Server is unreachable");
            LOG.warn("Server is unreachable");
        }
    }
}
