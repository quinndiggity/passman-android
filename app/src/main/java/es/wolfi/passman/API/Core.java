/**
 *  Passman Android App
 *
 * @copyright Copyright (c) 2016, Sander Brand (brantje@gmail.com)
 * @copyright Copyright (c) 2016, Marcos Zuriaga Miguel (wolfi@wolfi.es)
 * @license GNU AGPL version 3 or any later version
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package es.wolfi.passman.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import es.wolfi.app.passman.R;
import es.wolfi.app.passman.SettingValues;
import es.wolfi.app.passman.SingleTon;
import es.wolfi.utils.JSONUtils;

public abstract class Core {
    protected static final String LOG_TAG    = "API_LIB";

    protected static String host;
    protected static String username;
    protected static String password;


    public static void setUpAPI(String host, String username, String password) {
        Core.setAPIHost(host);
        Core.username = username;
        Core.password = password;
    }

    public static String getAPIHost() {
        return host;
    }

    public static void setAPIHost(String host) {
        Core.host = host.concat("/index.php/apps/passman/api/v2/");
    }

    public static String getAPIUsername() {
        return username;
    }

    public static void setAPIUsername(String username) {
        Core.username = username;
    }

    public static String getAPIPassword() {
        return password;
    }

    public static void setAPIPassword(String password) {
        Core.password = password;
    }

    public static void requestAPIGET(Context c, String endpoint, final FutureCallback<String> callback) {
        String auth = "Basic ".concat(Base64.encodeToString(username.concat(":").concat(password).getBytes(), Base64.NO_WRAP));

        Ion.with(c)
        .load(host.concat(endpoint))
        .setHeader("Authorization", auth)                // set the header
        .asString()
        .setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if (e == null && JSONUtils.isJSONObject(result)) {
                    try {
                        JSONObject o = new JSONObject(result);
                        if (o.getString("message").equals("Current user is not logged in")) {
                            callback.onCompleted(new Exception("401"), null);
                            return;
                        }
                    } catch (JSONException e1) {

                    }
                }

                callback.onCompleted(e, result);
            }
        });
    }

    /**
     * Check if the user has logged in, also sets up the API
     * @param c     The context where this should be run
     * @param toast Whether we want or not a toast! Yum!
     * @param cb    The callback
     */
    public static void checkLogin(final Context c, final boolean toast, final FutureCallback<Boolean> cb) {
        SingleTon ton = SingleTon.getTon();

        if (ton.getString(SettingValues.HOST.toString()) == null) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
            String url = settings.getString(SettingValues.HOST.toString(), null);

            // If the url is null app has not yet been configured!
            if (url == null) {
                cb.onCompleted(null, false);
                return;
            }

            // Load the server settings
            ton.addString(SettingValues.HOST.toString(), url);
            ton.addString(SettingValues.USER.toString(), settings.getString(SettingValues.USER.toString(), ""));
            ton.addString(SettingValues.PASSWORD.toString(), settings.getString(SettingValues.PASSWORD.toString(), ""));
        }

        String host = ton.getString(SettingValues.HOST.toString());
        String user = ton.getString(SettingValues.USER.toString());
        String pass = ton.getString(SettingValues.PASSWORD.toString());

        Vault.setUpAPI(host, user, pass);
        Vault.getVaults(c, new FutureCallback<HashMap<String, Vault>>() {
            @Override
            public void onCompleted(Exception e, HashMap<String, Vault> result) {
                boolean ret = true;

                if (e != null) {
                    if (e.getMessage().equals("401")) {
                        if (toast) Toast.makeText(c, c.getString(R.string.wrongNCSettings), Toast.LENGTH_LONG).show();
                        ret = false;
                    }
                    else if (e.getMessage().contains("Unable to resolve host") || e.getMessage().contains("Invalid URI")) {
                        if (toast) Toast.makeText(c, c.getString(R.string.wrongNCUrl), Toast.LENGTH_LONG).show();
                        ret = false;
                    }
                    else {
                        if (toast) Toast.makeText(c, c.getString(R.string.net_error), Toast.LENGTH_LONG).show();
                        ret = false;
                    }
                }

                cb.onCompleted(e, ret);
            }
        });
    }
}
