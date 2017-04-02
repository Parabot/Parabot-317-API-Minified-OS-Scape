package org.ethan.oss.ui;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.api.methods.Bank;
import org.ethan.oss.reflection.ReflWrapper;
import org.ethan.oss.utils.Utilities;
import org.parabot.core.Context;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BotMenu extends ReflWrapper implements ActionListener {
    public JMenuItem dMouse;
    WidgetViewer wv = null;

    public BotMenu(JMenuBar bar) {

        JMenu debug = new JMenu("Debug");
        // JMenu inputs = new JMenu("Input");

        JMenuItem widgets = newItem("Widgets");
        JMenuItem players = newItem("Players");
        JMenuItem npcs    = newItem("Npcs");
        JMenuItem inv     = newItem("Inventory");
        JMenuItem bank    = newItem("Bank");
        JMenuItem info    = newItem("Info");
        JMenuItem mouse   = newItem("Mouse");
        JMenuItem test    = newItem("Test");
        JMenuItem objects = newItem("Objects");
        JMenuItem gitems  = newItem("GroundItems");
        JMenuItem sett    = newItem("Settings");
        dMouse = newItem("Disable Mouse");

        debug.add(widgets);
        debug.add(players);
        debug.add(npcs);
        debug.add(objects);
        debug.add(gitems);
        debug.add(inv);
        debug.add(bank);
        debug.add(info);
        debug.add(mouse);
        debug.add(sett);
        debug.add(test);
        // inputs.add(dMouse);

        bar.add(debug);
        // bar.add(inputs);
    }

    private JMenuItem newItem(String name) {
        JMenuItem item = new JCheckBoxMenuItem(name);
        item.addActionListener(this);
        return item;
    }

    public void performCommand(String command) {
        switch (command) {
            case "Test":
                System.setProperty("socksProxyHost", "127.0.0.1");
                System.setProperty("socksProxyPort", "9150");
                break;
            case "Disable Mouse":
                ServerEngine.getInstance().setDisableMouse(true);
                dMouse.setText("Enable Mouse");
                dMouse.setName("Enable Mouse");

                break;
            case "Enable Mouse":
                ServerEngine.getInstance().setDisableMouse(false);
                dMouse.setText("Disable Mouse");
                dMouse.setName("Disable Mouse");

                break;
            case "Players":
                if (!ServerEngine.getInstance().isDebugPlayers()) {
                    ServerEngine.getInstance().setDebugPlayers(true);
                } else {
                    Utilities.saveAccount(ServerEngine.getInstance().getNames());
                    ServerEngine.getInstance().setDebugPlayers(false);
                }
                break;
            case "Settings":
                if (!ServerEngine.getInstance().isDebugSettings()) {
                    ServerEngine.getInstance().setDebugSettings(true);
                } else {
                    ServerEngine.getInstance().setDebugSettings(false);
                }
                break;
            case "Bank":
                if (!ServerEngine.getInstance().isDebugBank()) {
                    System.out.println("Open: " + Bank.isOpen());
                    ServerEngine.getInstance().setDebugBank(true);
                } else {
                    ServerEngine.getInstance().setDebugBank(false);
                }
                break;
            case "Objects":
                if (!ServerEngine.getInstance().isDebugGameObjects()) {
                    ServerEngine.getInstance().setDebugGameObjects(true);
                } else {
                    ServerEngine.getInstance().setDebugGameObjects(false);
                }
                break;
            case "GroundItems":
                if (!ServerEngine.getInstance().isDebugGroundItems()) {
                    ServerEngine.getInstance().setDebugGroundItems(true);
                } else {
                    ServerEngine.getInstance().setDebugGroundItems(false);
                }
                break;
            case "Npcs":
                if (!ServerEngine.getInstance().isDebugNpcs()) {
                    ServerEngine.getInstance().setDebugNpcs(true);
                } else {
                    ServerEngine.getInstance().setDebugNpcs(false);
                }
                break;
            case "Inventory":

                if (!ServerEngine.getInstance().isDebugInventory()) {
                    ServerEngine.getInstance().setDebugInventory(true);
                } else {
                    ServerEngine.getInstance().setDebugInventory(false);
                }
                break;
            case "Info":
                if (!ServerEngine.getInstance().isDebugText()) {
                    ServerEngine.getInstance().setDebugText(true);
                } else {
                    ServerEngine.getInstance().setDebugText(false);
                }
                break;
            case "Widgets":
                if (wv != null && wv.isVisible()) {
                    wv.dispose();
                    wv = null;
                } else {
                    wv = new WidgetViewer();
                }
                break;
            case "Mouse":
                if (!ServerEngine.getInstance().isDebugMouse()) {
                    ServerEngine.getInstance().setDebugMouse(true);
                } else {
                    ServerEngine.getInstance().setDebugMouse(false);
                    Context.getInstance().getApplet().setCursor(Cursor.getDefaultCursor());
                }
                break;
            default:
                System.out.println("Invalid command: " + command);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.performCommand(e.getActionCommand());
    }

}
