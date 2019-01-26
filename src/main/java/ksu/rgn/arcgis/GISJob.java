package ksu.rgn.arcgis;


import com.darkyen.dave.Request;
import com.darkyen.dave.Response;
import com.darkyen.dave.ResponseTranslator;
import ksu.rgn.utils.Future;
import org.json.JSONObject;

/**
 *
 */
public abstract class GISJob {

    protected Future future = null;
    GISBridge bridge = null;

    public abstract void run();

    protected JSONObject request(String path, JSONObject data) {
        Request req = bridge.api.post(path + "?f=pjson");
        if (data != null) {
            req.body(data.toString(), "application/json");
        }
        // TODO (nils): Inject clientID & token if applies
        final Response<String> r = req.execute(ResponseTranslator.STRING_TRANSLATOR);

        final String responseString = r.getBody();
        return new JSONObject(responseString);
    }

    @Override
    public String toString() {
        return "GISJob." + getClass().getSimpleName();
    }

}
