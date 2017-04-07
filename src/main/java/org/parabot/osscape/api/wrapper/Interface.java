package org.parabot.osscape.api.wrapper;

import org.parabot.osscape.accessors.Widget;

/**
 * @author JKetelaar
 */
public class Interface {

    private Widget[] children;
    private int      index;

    public Interface(Widget[] children, int index) {
        this.children = children;
        this.index = index;
    }

    public InterfaceChild getChild(int index) {
        return new InterfaceChild(this.children[index], index);
    }

    public int getIndex() {
        return index;
    }

    public InterfaceChild[] getChildren() {
        InterfaceChild[] interfaceChildren = new InterfaceChild[children.length];
        for (int i = 0; i < this.children.length; i++) {
            interfaceChildren[i] = getChild(i);
        }

        return interfaceChildren;
    }
}
