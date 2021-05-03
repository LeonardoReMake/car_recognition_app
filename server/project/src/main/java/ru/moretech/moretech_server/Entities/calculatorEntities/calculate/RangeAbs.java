package ru.moretech.moretech_server.Entities.calculatorEntities.calculate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RangeAbs {
    private static final Logger LOG = LoggerFactory.getLogger(RangeAbs.class);

    protected boolean filled;
    protected int max;
    protected int min;

    public RangeAbs() {
    }

    public RangeAbs(boolean filled, int max, int min) {
        this.filled = filled;
        this.max = max;
        this.min = min;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
