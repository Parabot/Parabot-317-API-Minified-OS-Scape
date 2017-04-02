package org.ethan.oss.ui;

import org.ethan.oss.api.interactive.Widgets;
import org.ethan.oss.api.wrappers.Widget;
import org.ethan.oss.api.wrappers.WidgetChild;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class WidgetViewer extends JFrame {
    private static final long      serialVersionUID = 2251112333747448462L;
    public               Rectangle rect             = null;
    private JTable            table;
    private JTextField        txtFilter;
    private JPanel            panel_1;
    private JSplitPane        splitPane;
    private JTree             tree;
    private JComboBox<String> comboBox;

    public WidgetViewer() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                rect = null;
            }
        });
        setTitle("Widget Viewer");
        setSize(500, 497);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

        splitPane = new JSplitPane();
        getContentPane().add(splitPane);

        panel_1 = new JPanel();
        splitPane.setLeftComponent(panel_1);
        panel_1.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane2 = new JScrollPane();
        panel_1.add(scrollPane2);

        tree = new JTree();
        scrollPane2.setViewportView(tree);

        JPanel panel = new JPanel();
        splitPane.setRightComponent(panel);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                refreshWidgets(null);
            }
        });
        SpringLayout sl_panel = new SpringLayout();
        sl_panel.putConstraint(SpringLayout.NORTH, btnRefresh, 6, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.WEST, btnRefresh, 6, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.EAST, btnRefresh, -6, SpringLayout.EAST, panel);
        panel.setLayout(sl_panel);
        panel.add(btnRefresh);

        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        txtFilter = new JTextField();
        sl_panel.putConstraint(SpringLayout.NORTH, txtFilter, 6, SpringLayout.SOUTH, btnRefresh);
        sl_panel.putConstraint(SpringLayout.EAST, txtFilter, -6, SpringLayout.EAST, panel);
        txtFilter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER
                        || !comboBox.getSelectedItem().toString().equalsIgnoreCase("Mouse (x,y)")) {
                    refreshWidgets(new Callback<Boolean, WidgetChild>() {

                        @Override
                        public Boolean call(WidgetChild child) {
                            switch (comboBox.getSelectedItem().toString()) {
                                case "Text":
                                    return child.getText().toLowerCase().contains(txtFilter.getText().toLowerCase());
                                case "Mouse (x,y)":

                                    return false;

                                case "Action":
                                    if (child.getActions() == null) {
                                        return false;
                                    }
                                    for (String s : child.getActions()) {
                                        if (s != null && s.toLowerCase().contains(txtFilter.getText().toLowerCase())) {
                                            return true;
                                        }
                                    }
                                    return false;
                                default:
                                    return true;
                            }
                        }
                    });
                }
            }
        });
        panel.add(txtFilter);
        txtFilter.setColumns(10);

        comboBox = new JComboBox<String>();
        sl_panel.putConstraint(SpringLayout.NORTH, comboBox, 6, SpringLayout.SOUTH, btnRefresh);
        sl_panel.putConstraint(SpringLayout.WEST, comboBox, 6, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.NORTH, scrollPane, 6, SpringLayout.SOUTH, txtFilter);
        sl_panel.putConstraint(SpringLayout.EAST, scrollPane, -6, SpringLayout.EAST, panel);
        sl_panel.putConstraint(SpringLayout.WEST, scrollPane, 6, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane, -6, SpringLayout.SOUTH, panel);
        sl_panel.putConstraint(SpringLayout.WEST, txtFilter, 6, SpringLayout.EAST, comboBox);
        comboBox.setModel(new DefaultComboBoxModel<String>(new String[]{ "Text", "Action", "Mouse (x,y)" }));
        panel.add(comboBox);
        splitPane.setDividerLocation(200);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                Widget      widget = null;
                WidgetChild child  = null;
                if (tree.getSelectionPath() != null) {
                    for (Object s : tree.getSelectionPath().getPath()) {
                        if (s == null) {
                            continue;
                        }
                        String str = s.toString();

                        if (str.equalsIgnoreCase("Widgets")) {
                            continue;
                        }
                        if (widget == null) {
                            widget = Widgets.get(Integer.parseInt(str.substring(7)));
                        } else if (child == null) {
                            child = widget.getChild(Integer.parseInt(str.substring(12)));
                        } else {
                            child = child.getChild(Integer.parseInt(str.substring(12)));
                        }
                    }
                }
                if (child != null) {
                    setWidgetChild(child);
                } else if (widget != null) {
                    setWidget(widget);
                }
            }
        });

        setVisible(true);
    }

    public void setWidget(Widget widget) {
        ArrayList<Object[]> props = new ArrayList<Object[]>();
        props.add(new Object[]{ "Index", widget.getIndex() });
        props.add(new Object[]{ "isValid", "true" });

        setTable(new DefaultTableModel(
                props.toArray(new Object[props.size()][2]),
                new String[]{
                        "Property", "Value"
                }
        ));
    }

    public void setWidgetChild(WidgetChild child) {
        ArrayList<Object[]> props = new ArrayList<Object[]>();
        props.add(new Object[]{ "Text", child.getText() });
        try {
            props.add(new Object[]{ "Parent", child.getParent().getIndex(),
                    null });
        } catch (Exception e) {
        }
        try {
            props.add(new Object[]{ "Parent ID", child.getParent().getId(),
                    null });
        } catch (Exception e) {
        }
        try {
            props.add(new Object[]{ "Child", child.getIndex() });
        } catch (Exception e) {
        }
        try {
            props.add(new Object[]{ "Child ID", child.getId() });
        } catch (Exception e) {
        }
        try {
            props.add(new Object[]{ "Relative X", child.getRelativeX() });
        } catch (Exception e) {
        }
        try {
            props.add(new Object[]{ "Relative Y", child.getRelativeY() });
        } catch (Exception e) {
        }
        try {
            props.add(new Object[]{ "Scroll X", child.getScrollX() });
        } catch (Exception e) {
        }
        try {
            props.add(new Object[]{ "Scroll Y", child.getScrollY() });
        } catch (Exception e) {
        }
        try {
            props.add(new Object[]{ "Absolute X", child.getX() });
        } catch (Exception e) {
        }
        try {
            props.add(new Object[]{ "Absolute Y", child.getY() });
        } catch (Exception e) {
        }
        try {
            props.add(new Object[]{ "isVisible",
                    child.isVisible() ? "true" : "false" });
        } catch (Exception e) {
        }
        try {
            props.add(new Object[]{ "Width", child.getWidth() });
        } catch (Exception e) {
        }

        try {
            props.add(new Object[]{ "Height", child.getHeight() });
        } catch (Exception e) {
        }
        try {
            props.add(new Object[]{ "TextureID", child.getTextureID() });
        } catch (Exception e) {
        }

        try {
            props.add(new Object[]{ "Actions:", "" });
            if (child.getActions() != null) {
                for (String s : child.getActions()) {
                    if (s != null && !s.trim().isEmpty()) {
                        props.add(new Object[]{ "-", s });
                    }
                }
            }
        } catch (Exception e) {
        }

        try {
            props.add(new Object[]{ "Item ID", child.getItemId() });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            props.add(new Object[]{ "Item Stack", child.getItemStack() });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            props.add(new Object[]{ "SlotContent:", "" });
            if (child.getActions() != null) {
                for (int s : child.getSlotContentIds()) {
                    String s1 = Integer.toString(s);
                    if (s1 != null && !s1.trim().isEmpty()) {
                        props.add(new Object[]{ "-", s });
                    }
                }
            }
        } catch (Exception e) {
        }

        try {
            rect = new Rectangle(child.getX(), child.getY(),
                    child.getWidth(), child.getHeight());
        } catch (Exception e) {
        }

        setTable(new DefaultTableModel(props.toArray(
                new Object[props.size()][2]), new String[]{ "Property", "Value" }));
    }

    public void refreshWidgets(final Callback<Boolean, WidgetChild> callback) {
        setTree(new DefaultTreeModel(
                new DefaultMutableTreeNode("Widgets") {
                    private static final long serialVersionUID = 5683094594973880127L;

                    {
                        for (int a = 0; a < 1000; a++) {
                            if (!Widgets.get(a).isValid()) {
                                continue;
                            }
                            DefaultMutableTreeNode node = addChildren(callback, Widgets.get(a), new DefaultMutableTreeNode("Widget-" + a));

                            if (node.getChildCount() > 0) {
                                add(node);
                            }
                        }
                    }

                    private DefaultMutableTreeNode addChildren(Callback<Boolean, WidgetChild> callback,
                                                               WidgetChild widget, DefaultMutableTreeNode parent) {
                        if (widget.getChildren() != null) {
                            for (WidgetChild c : widget.getChildren()) {
                                if (callback == null || callback.call(c)) {
                                    parent.add(addChildren(callback, c, new DefaultMutableTreeNode("WidgetChild-" + c.getIndex())));
                                }
                            }
                        }

                        return parent;
                    }

                    private DefaultMutableTreeNode addChildren(Callback<Boolean, WidgetChild> callback,
                                                               Widget widget, DefaultMutableTreeNode parent) {
                        if (!widget.isValid()) {
                            return null;
                        }

                        if (widget.getChildren() != null) {
                            for (WidgetChild c : widget.getChildren()) {
                                if (callback == null || callback.call(c)) {
                                    parent.add(addChildren(callback, c, new DefaultMutableTreeNode("WidgetChild-" + c.getIndex())));
                                }
                            }
                        }

                        return parent;
                    }
                }
        ));
    }

    public void setTable(DefaultTableModel model) {
        table.setModel(model);
        table.getColumnModel().getColumn(0).setMinWidth(150);
        table.getColumnModel().getColumn(0).setMaxWidth(150);
    }

    public void setTree(DefaultTreeModel model) {
        tree.setModel(model);
    }

    public abstract class Callback<TRet, TArg> {
        public abstract TRet call(TArg val);
    }
}
