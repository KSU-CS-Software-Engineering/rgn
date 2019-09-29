package com.CIS642.Rural.GroceryImplementation.arcgis;


import com.CIS642.Rural.GroceryImplementation.utils.Future;
import com.darkyen.dave.Request;
import com.darkyen.dave.Response;
import com.darkyen.dave.ResponseTranslator;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public abstract class GISJob {

    protected Future future = null;
    protected GISBridge bridge = null;

    public abstract void run();

    protected JSONObject request(String path, JSONObject payload, HashMap<String, String> urlArgs) throws JSONException {
        String argString = getArgString(urlArgs);
        Request req = bridge.api.post(path + "?f=pjson" + argString);
        if (payload != null) {
            req.body(payload.toString(), "application/json");
        }

        final Response<String> r = req.execute(ResponseTranslator.STRING_TRANSLATOR);
        final String responseString = r.getBody();
        return new JSONObject(responseString);
    }

    public String getArgString(HashMap<String, String> argsMap) {
        if (argsMap == null) return "";

        StringBuilder argString = new StringBuilder();
        for (Map.Entry<String, String> entry : argsMap.entrySet()) {
            argString.append("&").append(entry.getKey());
            argString.append("=").append(entry.getValue());
        }
        return argString.toString();
    }

    @Override
    public String toString() {
        return "GISJob." + getClass().getSimpleName();
    }
}
