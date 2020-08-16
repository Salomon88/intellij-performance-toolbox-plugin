package org.performancetoolbox.intellij.plugin.common;

import java.util.List;

public interface OpenFileHistoryAdapter {

    /**
     * Retrieves history records
     *
     * @return history records
     */
    List<String> retrieve();

    /**
     * Returns configured history size
     *
     * @return history size
     */
    int getHistorySize();

    /**
     * Adds history record to LRU cache with respect to history size
     *
     * @param record current history record
     */
    void addAndStore(String record);
}