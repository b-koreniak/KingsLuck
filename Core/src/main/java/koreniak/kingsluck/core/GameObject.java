package koreniak.kingsluck.core;

import java.util.Objects;

public abstract class GameObject {
    private int uniqueId;

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != getClass()) return false;
        GameObject gameObject = (GameObject) o;
        return uniqueId == gameObject.uniqueId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueId);
    }
}
