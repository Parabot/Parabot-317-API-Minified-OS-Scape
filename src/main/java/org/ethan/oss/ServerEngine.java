package org.ethan.oss;

import org.ethan.oss.api.input.InternalKeyboard;
import org.ethan.oss.api.input.InternalMouse;
import org.ethan.oss.api.listeners.InventoryMonitor;
import org.ethan.oss.arch.ArchiveClassLoader;
import org.ethan.oss.component.RSCanvas;
import org.ethan.oss.hook.FieldHook;
import org.ethan.oss.hook.MethodHook;
import org.ethan.oss.ui.BotMenu;
import org.ethan.oss.utils.FileDownloader;
import org.parabot.core.Context;

import java.applet.Applet;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerEngine {
    private static ServerEngine instance = new ServerEngine();
    private ClassLoader classLoader;
    private boolean debugPlayers     = false;
    private boolean debugNpcs        = false;
    private boolean debugInventory   = false;
    private boolean debugText        = false;
    private boolean debugMouse       = false;
    private boolean debugGameObjects = false;
    private boolean debugGroundItems = false;
    private boolean debugSettings    = false;
    private boolean debugBank        = false;
    private boolean disableMouse     = false;
    private boolean debugWidgets     = false;
    private BotMenu          botMenu;
    private RSCanvas         canvas;
    private InternalKeyboard keyboard;
    private InternalMouse    mouse;
    private Applet           applet;
    private Map<String, FieldHook>  fieldMap  = null;
    private Map<String, MethodHook> methodMap = null;
    private InventoryMonitor inventoryMonitor;
    private FileDownloader   downloader;
    private String       user                     = null;
    private String       pass                     = null;
    private boolean      useRandomMouse           = true;
    private boolean      gameObjectCenterInteract = false;
    private int          staffRandomsCompleted    = 0;
    private List<String> names                    = new ArrayList<String>();
    private List<String> preCheckedNames          = new ArrayList<String>();
    private BufferedReader  br;
    private FileInputStream fstream;

    public static ServerEngine getInstance() {
        return instance;
    }

    public BufferedReader getBr(String fileName) {
        if (br == null) {
            br = new BufferedReader(new InputStreamReader(getFstream(fileName)));
        }
        return br;
    }

    public void setBr(BufferedReader br) {

        this.br = br;
    }

    public FileInputStream getFstream(String fileName) {
        if (fstream == null) {
            try {
                fstream = new FileInputStream(System.getProperty("user.home") + "/Desktop/" + fileName + ".txt");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fstream;
    }

    public void setFstream(FileInputStream fstream) {
        this.fstream = fstream;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public boolean isGameObjectCenterInteract() {
        return gameObjectCenterInteract;
    }

    public void setGameObjectCenterInteract(boolean gameObjectCenterInteract) {
        this.gameObjectCenterInteract = gameObjectCenterInteract;
    }

    public int getStaffRandomsCompleted() {
        return staffRandomsCompleted;
    }

    public void setStaffRandomsCompleted(int staffRandomsCompleted) {
        this.staffRandomsCompleted = staffRandomsCompleted;
    }

    public boolean isUseRandomMouse() {
        return useRandomMouse;
    }

    public void setUseRandomMouse(boolean useRandomMouse) {
        this.useRandomMouse = useRandomMouse;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public List<String> getPreCheckedNames() {
        return preCheckedNames;
    }

    public void setPreCheckedNames(List<String> preCheckedNames) {
        this.preCheckedNames = preCheckedNames;
    }

    public Map<String, FieldHook> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, FieldHook> map) {
        this.fieldMap = map;
    }

    public boolean isDebugBank() {
        return debugBank;
    }

    public void setDebugBank(boolean debugBank) {
        this.debugBank = debugBank;
    }

    public Map<String, MethodHook> getMethodMap() {
        return methodMap;
    }

    public void setMethodMap(Map<String, MethodHook> map) {
        this.methodMap = map;
    }

    public boolean isDebugWidgets() {
        return debugWidgets;
    }

    public void setDebugWidgets(boolean debugWidgets) {
        this.debugWidgets = debugWidgets;
    }

    public boolean isDebugPlayers() {
        return debugPlayers;
    }

    public void setDebugPlayers(boolean debugPlayers) {
        this.debugPlayers = debugPlayers;
    }

    public boolean isDebugSettings() {
        return debugSettings;
    }

    public void setDebugSettings(boolean debugSettings) {
        this.debugSettings = debugSettings;
    }

    public boolean isDebugMouse() {
        return debugMouse;
    }

    public void setDebugMouse(boolean debugMouse) {
        this.debugMouse = debugMouse;
    }

    public InventoryMonitor getInventoryMonitor() {
        return inventoryMonitor;
    }

    public void setInventoryMonitor(InventoryMonitor inventoryMonitor) {
        this.inventoryMonitor = inventoryMonitor;
    }

    public boolean isDebugNpcs() {
        return debugNpcs;
    }

    public void setDebugNpcs(boolean debugNpcs) {
        this.debugNpcs = debugNpcs;
    }

    public boolean isDisableMouse() {
        return disableMouse;
    }

    public void setDisableMouse(boolean disableMouse) {
        this.disableMouse = disableMouse;
    }

    public boolean isDebugInventory() {
        return debugInventory;
    }

    public void setDebugInventory(boolean debugInventory) {
        this.debugInventory = debugInventory;
    }

    public boolean isDebugText() {
        return debugText;
    }

    public void setDebugText(boolean debugText) {
        this.debugText = debugText;
    }

    public boolean isDebugGroundItems() {
        return debugGroundItems;
    }

    public void setDebugGroundItems(boolean debugGroundItems) {
        this.debugGroundItems = debugGroundItems;
    }

    public boolean isDebugGameObjects() {
        return debugGameObjects;
    }

    public void setDebugGameObjects(boolean debugGameObjects) {
        this.debugGameObjects = debugGameObjects;
    }

    public InternalKeyboard getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(InternalKeyboard keyboard) {
        this.keyboard = keyboard;
    }

    public InternalMouse getMouse() {
        return mouse;
    }

    public void setMouse(InternalMouse mouse) {
        this.mouse = mouse;
    }

    public FileDownloader getDownloader() {
        return downloader;
    }

    public void setDownloader(FileDownloader downloader) {
        this.downloader = downloader;
    }

    public Class<?> loadClass(final String className) {
        if (classLoader == null) {
            System.out.println("Error Null Class Loader");
            return null;
        }
        if (!((ArchiveClassLoader) classLoader).classes().containsKey(className)) {
            try {
                return classLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return ((ArchiveClassLoader) classLoader).classes().get(className);
    }

    public synchronized RSCanvas getCanvas() {
        if (canvas != null) {
            return canvas;
        }
        if (Context.getInstance().getApplet() == null || Context.getInstance().getApplet().getComponentCount() == 0
                || !(Context.getInstance().getApplet().getComponent(0) instanceof RSCanvas)) {
            return null;
        }
        return (RSCanvas) Context.getInstance().getApplet().getComponent(0);
    }

    public RSCanvas getEngineCanvas() {
        return canvas;
    }

    public Applet getApplet() {
        return applet;
    }

    public void setApplet(Applet applet) {
        this.applet = applet;
    }

    public BotMenu getBotMenu() {
        return botMenu;
    }

    public void setBotMenu(BotMenu botMenu) {
        this.botMenu = botMenu;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
