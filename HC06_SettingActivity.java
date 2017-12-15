package org.androidtown.sw_pj;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class HC06_SettingActivity extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addPreferencesFromResource(R.xml.settings_screen);

    }

        /*
        Preference set_profile = (Preference)findPreference("profile");

        set_profile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //Intent i = new Intent(SettingActivity.this, ProfileActivity.class);
                //startActivity(i);

                SettingActivity.this.startActivity(new Intent(SettingActivity.this,ProfileActivity.class));
                return false;
            }
        });


    }
    */

}