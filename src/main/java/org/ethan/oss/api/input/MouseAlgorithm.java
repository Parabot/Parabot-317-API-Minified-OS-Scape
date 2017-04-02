package org.ethan.oss.api.input;

import java.awt.*;

public interface MouseAlgorithm {

    public Point[] makeMousePath(final int x1, final int y1, final int x2, final int y2);

    public String getName();
}