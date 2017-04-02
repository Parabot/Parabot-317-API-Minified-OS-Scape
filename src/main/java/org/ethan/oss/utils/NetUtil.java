package org.ethan.oss.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

public class NetUtil {

    public static String[] readPage(String url) {
        url = url.replaceAll(" ", "%20");
        ArrayList<String> lines = new ArrayList<String>();
        try {
            final URLConnection  con = createURLConnection(url);
            final BufferedReader in  = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String               line;
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
            in.close();
        } catch (Exception e) {
            System.out.println("Error reading page!");
        }
        return lines.toArray(new String[lines.size()]);
    }

    public static URLConnection createURLConnection(String url) {
        try {
            final URL           address    = new URL(url);
            final URLConnection connection = address.openConnection();
            connection.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Content-Type", "image/png");
            return connection;
        } catch (IOException ex) {
            System.out.println("Error creating connection!");
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean downloadFile(String url, String location) {
        try {

            final URLConnection connection = createURLConnection(url);

            final int  contentLength = connection.getContentLength();
            final File destination   = new File(location);

            if (destination.exists()) {
                final URLConnection savedFileConnection = destination.toURI().toURL().openConnection();
                if (savedFileConnection.getContentLength() == contentLength) {
                    return true;
                }
            } else {
                final File parent = destination.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
            }

            final ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());

            final FileOutputStream fos = new FileOutputStream(destination);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();

        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }

        System.out.println(url + "->" + location);
        return new File(location).exists();
    }

}