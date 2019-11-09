package br.com.mobile.Resource;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Description: FIXME: Document this type > *Project: virtual-mobile
 *
 * @author breni > _@date: 20/10/2019 > _@version $$ >
 */
public class SessionResource {
    private final SharedPreferences prefs;

    public SessionResource(Context context) {
        prefs = context.getSharedPreferences("SESSION", context.MODE_PRIVATE);
    }

    public void setPreferenceUserName(String userName) {
        prefs.edit().putString("username", userName).commit();
    }

    public void setPreferencePassword(String password) {
        prefs.edit().putString("password", password).commit();
    }

    public void setPreferencesApiURL(String apiURL) {
        prefs.edit().putString("apiURL", apiURL).apply();
    }

    public void setPreferencesAccessURL(String apiURL) {
        prefs.edit().putString("accessURL", apiURL).apply();
    }

    public void setPreferencesIP(String ip) {
        prefs.edit().putString("IP", ip).apply();
    }

    public void setPreferencesPortApi(String port) {
        prefs.edit().putString("apiPort", port).apply();
    }

    public void setPreferencesPortAccess(String port) {
        prefs.edit().putString("accessPort", port).apply();
    }

    public boolean setPreferencesToken(String tokenAcess) {
        return prefs.edit().putString("token", tokenAcess).commit();
    }

    public void setRemoveUserSession() {
        prefs.edit().remove("username").apply();
        prefs.edit().remove("password").apply();
        prefs.edit().remove("token").apply();
    }

    public String getPreferencesUserName() {
        return prefs.getString("username", "");
    }

    public String getPreferencesPassword() {
        return prefs.getString("password", "");
    }

    public String getPreferencesApiURL() {
        return prefs.getString("apiURL", "");
    }

    public String getPreferencesAccessURL() {
        return prefs.getString("accessURL", "");
    }

    public String getPreferencesToken() {
        return prefs.getString("token", "");
    }

    public String getPreferencesIP() {
        return prefs.getString("IP", "");
    }

    public String getPreferencesPortApi() {
        return prefs.getString("apiPort", "");
    }

    public String getPreferencesPortAccess() {
        return prefs.getString("accessPort", "");
    }

}
