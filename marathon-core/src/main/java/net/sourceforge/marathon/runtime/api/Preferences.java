/*******************************************************************************
 * Copyright 2016 Jalian Systems Pvt. Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.sourceforge.marathon.runtime.api;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.io.Files;

public class Preferences {

    private static Preferences _instance = new Preferences();
    private JSONObject prefs;

    private Map<String, Set<IPreferenceChangeListener>> listeners = new HashMap<>();

    private Preferences() {
        prefs = getProjectPreferences();
    }

    public static Preferences instance() {
        return _instance;
    }

    public JSONObject getSection(String section) {
        if (!prefs.has(section)) {
            prefs.put(section, new JSONObject());
        }
        return prefs.getJSONObject(section);
    }

    private JSONObject getProjectPreferences() {
        File preferenceFile = new File(Constants.getMarathonProjectDirectory(), "project.json");
        if (preferenceFile.exists()) {
            try {
                return new JSONObject(Files.toString(preferenceFile, Charset.forName("utf-8")));
            } catch (JSONException e) {
            } catch (IOException e) {
            }
        }
        return new JSONObject();
    }

    public void save(String section) {
        File preferenceFile = new File(Constants.getMarathonProjectDirectory(), "project.json");
        try {
            Files.write(prefs.toString(2).getBytes(), preferenceFile);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        firePreferenceChange(section);
    }

    private void firePreferenceChange(String section) {
        Set<IPreferenceChangeListener> sectionListeners = listeners.get(section);
        if (sectionListeners == null) {
            return;
        }
        for (IPreferenceChangeListener l : sectionListeners) {
            l.preferencesChanged(section, getSection(section));
        }
    }

    public void addPreferenceChangeListener(String section, IPreferenceChangeListener l) {
        Set<IPreferenceChangeListener> list = listeners.get(section);
        if (list == null) {
            list = new HashSet<>();
            listeners.put(section, list);
        }
        list.add(l);
    }

    @SuppressWarnings("unchecked") public <T> T getValue(String section, String property, T defaultValue) {
        JSONObject oSection = getSection(section);
        Object value = oSection.opt(property);
        if (value == null) {
            return defaultValue;
        }
        return (T) value;
    }

    public <T> T getValue(String section, String property) {
        return getValue(section, property, null);
    }

    public void setValue(String section, String property, Object defaultValue) {
        JSONObject oSection = getSection(section);
        oSection.put(property, defaultValue);
        save(section);
    }

    public int getRememberedRunCount() {
        if (!prefs.has("remembered-count")) {
            setRememberedRunCount(10);
        }
        return prefs.getInt("remembered-count");
    }

    public void setRememberedRunCount(int count) {
        prefs.put("remembered-count", count);
        save("remembered-count");
    }
}
