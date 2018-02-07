package com.xulaoyao.ezdownloadmanager;

/**
 * IEzDownloadRetryPolicy
 * Created by renwoxing on 2018/2/7.
 */
public interface IEzDownloadRetryPolicy {
    /**
     * Returns the current timeout (used for logging).
     */
    public int getCurrentTimeout();

    /**
     * Returns the current retry count (used for logging).
     */
    public int getCurrentRetryCount();

    /**
     * Return back off multiplier
     */
    public float getBackOffMultiplier();


    public void retry() throws EzDownloadRetryErrorException;
}
