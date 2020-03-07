package eu.ha3.matmos.data.scanners;

public interface Progress {
    /**
     * Returns the current progress.
     *
     * @return
     */
    int getProgress_Current();

    /**
     * Returns the total value to "complete". Never set the total to zero, to prevent division by zero
     * errors.
     *
     * @return
     */
    int getProgress_Total();
}
