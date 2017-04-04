package org.parabot.osscape.accessors;

import java.util.Properties;

/**
 * @author JKetelaar
 */
public interface LoaderClass {
    String getIpAddress();

    String getParams();

    Properties getProperties();

    Object getLoader();
}
