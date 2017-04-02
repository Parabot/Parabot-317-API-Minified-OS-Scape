package org.ethan.oss.api.methods;

import org.ethan.oss.api.interactive.Widgets;
import org.ethan.oss.api.wrappers.WidgetChild;

public class NMZ {
    private final static int ABSORPTION_PARENT   = 202;
    private final static int ABSORPTION_CHILDREN = 1;
    private final static int ABSORPTION_CHILD    = 9;

    public static int getAbsPoints() {
        try {
            WidgetChild rsInterfaceChild = Widgets.get(ABSORPTION_PARENT, ABSORPTION_CHILDREN);
            if (rsInterfaceChild == null) {
                return 0;
            }
            WidgetChild rsInterfaceComponent = rsInterfaceChild.getChild(ABSORPTION_CHILD);
            if (rsInterfaceComponent == null || rsInterfaceComponent.getText() == null || rsInterfaceComponent != null && rsInterfaceComponent.getText() != null && rsInterfaceComponent.getText().equals("")) {
                return 0;
            }

            return Integer.parseInt(rsInterfaceComponent.getText().replaceAll(",", ""));
        } catch (Exception e) {
            System.err.println("Problem with interfaces?");
        }
        return 0;
    }

    public static boolean needAbsPot(int min) {
        if (getAbsPoints() <= min) {
            return true;
        }
        return false;
    }

}
