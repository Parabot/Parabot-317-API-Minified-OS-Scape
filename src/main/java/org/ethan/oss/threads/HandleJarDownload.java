package org.ethan.oss.threads;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.utils.FileDownloader;
import org.ethan.oss.utils.Utilities;

public class HandleJarDownload implements Runnable {
    ServerEngine engine = ServerEngine.getInstance();

    @Override
    public void run() {
        System.out.println("Attempting to cache OS-SCAPE gamepack.");
        engine.setDownloader(new FileDownloader("https://cdn.os-scape.com/clients/game.jar",
                Utilities.getContentDirectory() + "/gamepack.jar"));

        engine.getDownloader().run();

    }

}