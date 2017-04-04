//package org.ethan.oss;
//
//import org.ethan.oss.arch.Archive;
//import org.ethan.oss.arch.ArchiveClassLoader;
//import org.ethan.oss.arch.JarArchive;
//import org.ethan.oss.hook.Hook;
//import org.ethan.oss.parameters.OSScapeParameters;
//import org.ethan.oss.script.ScriptEngine;
//import org.ethan.oss.stub.Stub;
//import org.ethan.oss.threads.HandleInputs;
//import org.ethan.oss.threads.HandleJarDownload;
//import org.ethan.oss.ui.BotMenu;
//import org.ethan.oss.utils.Condition;
//import org.ethan.oss.utils.Utilities;
//import org.objectweb.asm.tree.ClassNode;
//import org.parabot.core.reflect.RefClass;
//import org.parabot.environment.scripts.Script;
//import org.parabot.environment.servers.ServerManifest;
//import org.parabot.environment.servers.ServerProvider;
//import org.parabot.environment.servers.Type;
//
//import javax.swing.*;
//import java.applet.Applet;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.jar.JarFile;
//
///**
// * @author Ethan, Everel, JKetelaar
// */
////@ServerManifest(author = "Ethan & Parabot", name = "OS-Scape", type = Type.REFLECTION, version = 0.1)
//public class GameLoader extends ServerProvider {
//    private ServerEngine      engine         = ServerEngine.getInstance();
//    private OSScapeParameters parser         = null;
//    private Thread            inputThreads   = null;
//    private Thread            downloadThread = null;
//
//    @Override
//    public Applet fetchApplet() {
//        try {
//            while (engine.getDownloader() != null && !engine.getDownloader().isFinished()) {
//                Condition.sleep(5);
//            }
//            final JarFile      jar     = new JarFile(Utilities.getContentDirectory() + Utilities.getContentName());
//            Archive<ClassNode> archive = new JarArchive(jar);
//            engine.setClassLoader(new ArchiveClassLoader(archive));
//            Utilities.handleOSScape(parser, engine.getClassLoader());
//            final Class<?> clientClass = engine.loadClass("oss.iIIiIIiIII"); //MAIN CLASS
//            RefClass       client      = new RefClass(clientClass);
//            engine.setApplet((Applet) client.newInstance().getInstance());
//            engine.getApplet().setStub(new Stub(parser));
//            return engine.getApplet();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public URL getJar() {
//        Hook.getInstance().init();
//
//        inputThreads = new Thread(new HandleInputs());
//        inputThreads.start();
//        parser = new OSScapeParameters();
//        downloadThread = new Thread(new HandleJarDownload());
//        downloadThread.start();
//        try {
//            return new URL("https://cdn.os-scape.com/clients/game.jar");
//        } catch (MalformedURLException e) {
//
//            e.printStackTrace();
//        }
//        return null;
//
//    }
//
//    @Override
//    public void addMenuItems(JMenuBar bar) {
//        ServerEngine.getInstance().setBotMenu(new BotMenu(bar));
//    }
//
//    @Override
//    public void initScript(Script script) {
//        ScriptEngine.getInstance().setScript(script);
//        ScriptEngine.getInstance().init();
//    }
//
//    @Override
//    public void unloadScript(Script script) {
//
//        ScriptEngine.getInstance().unload();
//    }
//
//    @Override
//    public void init() {
//    }
//
//}
