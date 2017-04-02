package org.ethan.oss.api.randoms;

public abstract class RandomEvent {

    private boolean enabled = true;
    private String  status  = "";

    public abstract String getName();

    public abstract String getAuthor();

    public abstract boolean active();

    public abstract void solve();

    public abstract void reset();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}