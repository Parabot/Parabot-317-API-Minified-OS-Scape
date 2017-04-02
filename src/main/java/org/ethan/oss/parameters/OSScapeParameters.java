package org.ethan.oss.parameters;

import org.ethan.oss.utils.NetUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class OSScapeParameters {
    private static final Map<String, String> PARAMETER_MAP = new HashMap<>();
    public               String              paramsString  = "";
    public               Properties          prop          = new Properties();

    /**
     * Creates a new instance of the parameters class.
     *
     * @param worldId the world you wish to load into
     */
    public OSScapeParameters() {
        parse();

    }

    public void parse() {
        PARAMETER_MAP.clear();
        StringBuilder builder = new StringBuilder();
        try {
            String[] hooks = NetUtil.readPage("https://pastebin.com/raw/Aa9Vw2mr"); //https://pastebin.com/raw/rnSpKiQ2 old
            for (String str : hooks) {

                builder.append(str);
                builder.append("\n");
                if (str.contains("param=")) {
                    str = str.replace("param=", "");
                    String str3 = str.substring(0, str.indexOf("="));
                    String str4 = str.substring(str.indexOf("=") + 1, str.length());
                    prop.put(str3, str4);
                }
                if (str.contains("=")) {
                    str = str.replaceAll("param=", "");

                    final String[] parts = str.split("=");
                    if (parts.length == 1) {
                        add(parts[0], "");
                    } else if (parts.length == 2) {
                        add(parts[0], parts[1]);
                    } else if (parts.length == 3) {
                        add(parts[0], parts[1] + "=" + parts[2]);
                    } else if (parts.length == 4) {
                        add(parts[0], parts[1] + "=" + parts[2] + "=" + parts[3]);
                    }

                }
            }
            paramsString = builder.toString();

        } catch (Exception e) {

        }
    }

    /**
     * Stores the given data in the Parameter map if it does not already exist.
     *
     * @param key The identification key
     * @param val The parameter data
     */
    private void add(String key, String val) {
        PARAMETER_MAP.put(key, val);
    }

    /**
     * Returns the value based on the key. If the key isn't found, it returns a
     * blank string.
     *
     * @param key
     *
     * @return the retrieved parameter
     */
    public String get(String key) {
        return PARAMETER_MAP.containsKey(key) ? PARAMETER_MAP.get(key) : "";
    }

}
