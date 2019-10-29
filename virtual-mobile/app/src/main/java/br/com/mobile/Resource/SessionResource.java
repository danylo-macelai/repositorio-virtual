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

    public void setPreferencesApiConnection(String apiURL) {
        prefs.edit().putString("apiURL", apiURL).apply();
    }

    public boolean setPreferencesToken(String tokenAcess) {
        return prefs.edit().putString("token", tokenAcess).commit();
    }

    public String getPreferencesUserName() {
        return prefs.getString("username", "");
    }

    public String getPreferencesPassword() {
        return prefs.getString("password", "");
    }

    public String getPreferencesApiConnection() {
        return prefs.getString("apiURL", "");
    }

    public String getPreferencesToken() {
        return prefs.getString("token", "");
    }

}
