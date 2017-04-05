package org.ethan.oss.component.debug;

import org.ethan.oss.utils.BasicTimer;
import org.parabot.osscape.api.interfaces.PaintListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Debugger<E> implements PaintListener {

    protected List<E>    list        = new ArrayList<E>();
    private   BasicTimer refreshRate = new BasicTimer(1000);

    public abstract E[] elements();

    public abstract boolean activate();

    public List<E> refresh() {
        if (!refreshRate.isRunning()) {
            list = Arrays.asList(elements());
        }
        return list;
    }

}