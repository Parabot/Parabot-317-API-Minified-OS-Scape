package org.ethan.oss.stub;

import org.ethan.oss.parameters.OSScapeParameters;

import java.applet.AppletContext;
import java.applet.AppletStub;
import java.net.MalformedURLException;
import java.net.URL;

public class Stub implements AppletStub {
    OSScapeParameters params;

    public Stub(OSScapeParameters params) {
        this.params = params;
    }

    @Override
    public void appletResize(int w, int h) {

    }

    @Override
    public AppletContext getAppletContext() {
        return null;
    }

    @Override
    public URL getCodeBase() {
        try {
            return new URL("http://93.158.237.2");
        } catch (MalformedURLException e) {

        }
        return null;
    }

    @Override
    public URL getDocumentBase() {
        return getCodeBase();
    }

    @Override
    public String getParameter(String key) {
        return params.prop.getProperty(key);
    }

    @Override
    public boolean isActive() {
        return false;
    }

}
