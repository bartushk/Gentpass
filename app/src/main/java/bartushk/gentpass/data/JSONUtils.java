package bartushk.gentpass.data;

import org.json.simple.JSONObject;

/**
 * Created by kbartush on 7/7/2014.
 */

public class JSONUtils {
    public static JSONObject userToJSON(User user) {
        JSONObject job = new JSONObject();
        job.put("user", user.getUsername());
        job.put("challenge", user.getChallenge());
        job.put("crypto_version", user.getCryptoVersion());
        return job;
    }

}
