package com.example.engmohamed.technonews;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Ask Monitor why $TechnoNewsPreferencesFragment on layout >> activity_setting
 * SetSummary function more details
 * bindPreferenceSummaryToValue Function more details
 * index check onPreferenceChange more details
 */

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the setting XML which lead to TechnoNewsPreferencesFragment
        setContentView(R.layout.activity_setting);
    }

    public static class TechnoNewsPreferencesFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Inflate the preferences from XML
            addPreferencesFromResource(R.xml.setting_main);

            // Find Number Of News Preference
            Preference newsNumber = findPreference(getString(R.string.news_number_key));

            // Bind Number Of News Value To UI
            bindPreferenceSummaryToValue(newsNumber);

            // Find Preference of News Type
            Preference newsType = findPreference(getString(R.string.news_type_key));

            // Bind News Type value to UI
            bindPreferenceSummaryToValue(newsType);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            // Convert new preference value to string
            String stringValue = newValue.toString();

            // Check the type of Preference
            if (preference instanceof ListPreference) {
                // Convert Preference object to ListPreference
                ListPreference listPreference = (ListPreference) preference;

                // Get the index of selected label
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                // Set Summary to update UI value
                preference.setSummary(stringValue);
            }
            return true;

        }

        /**
         * Bind the value or label of preference the UI so the user can see it
         *
         * @param preference which we want to bind
         */
        private void bindPreferenceSummaryToValue(Preference preference) {
            // Read onChangePreference Method
            preference.setOnPreferenceChangeListener(this);

            // Instantiate news preference constructor
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());

            // Find the Based Preference
            String preferenceString = preferences.getString(preference.getKey(), "");

            // Set the new value of preference
            onPreferenceChange(preference, preferenceString);
        }
    }
}
