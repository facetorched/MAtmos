package eu.ha3.matmos.core.sheet;

public interface VirtualSheet extends Sheet {
    /**
     * Commits this virtual sheet.
     */
    void apply();
}
