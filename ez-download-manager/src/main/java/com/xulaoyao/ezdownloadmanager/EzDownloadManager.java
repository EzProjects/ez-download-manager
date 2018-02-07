package com.xulaoyao.ezdownloadmanager;

import android.os.Handler;

import java.security.InvalidParameterException;

/**
 * 下载管理器
 * This class used to handles long-running HTTP downloads, User can raise a {@link EzDownloadRequest} request with multiple features.
 * The download manager will conduct the download in the background, taking care of HTTP interactions, failures  and retrying downloads
 * across connectivity changes.
 * EzDownloadManager
 * Created by renwoxing on 2018/2/7.
 */
public class EzDownloadManager implements IEzDownloadManagerListener {


    /**
     * Download request queue takes care of handling the request based on priority.
     */
    private EzDownloadRequestQueue mRequestQueue;

    /**
     * Default constructor
     */
    public EzDownloadManager() {
        mRequestQueue = new EzDownloadRequestQueue();
        mRequestQueue.start();
    }

    /**
     * Construct with provided callback handler
     *
     * @param callbackHandler - callback handler
     */
    public EzDownloadManager(Handler callbackHandler) throws InvalidParameterException {
        mRequestQueue = new EzDownloadRequestQueue(callbackHandler);
        mRequestQueue.start();
    }

    /**
     * Constructor taking MAX THREAD POOL SIZE  Allows maximum of 4 threads.
     * Any number higher than four or less than one wont be respected.
     * <p>
     * Deprecated use Default Constructor. As the thread pool size will not respected anymore through this constructor.
     * Thread pool size is determined with the number of available processors on the device.
     **/
    public EzDownloadManager(int threadPoolSize) {
        mRequestQueue = new EzDownloadRequestQueue(threadPoolSize);
        mRequestQueue.start();
    }

    /**
     * Add a new download.  The download will start automatically once the download manager is
     * ready to execute it and connectivity is available.
     *
     * @param request the parameters specifying this download
     * @return an ID for the download, unique across the application.  This ID is used to make future
     * calls related to this download.
     * @throws IllegalArgumentException
     */
    @Override
    public int add(EzDownloadRequest request) throws IllegalArgumentException {
        checkReleased("add(...) called on a released ThinDownloadManager.");
        if (request == null) {
            throw new IllegalArgumentException("DownloadRequest cannot be null");
        }
        return mRequestQueue.add(request);
    }

    @Override
    public int cancel(int downloadId) {
        checkReleased("cancel(...) called on a released ThinDownloadManager.");
        return mRequestQueue.cancel(downloadId);
    }

    @Override
    public void cancelAll() {
        checkReleased("cancelAll() called on a released ThinDownloadManager.");
        mRequestQueue.cancelAll();
    }

    @Override
    public int pause(int downloadId) {
        checkReleased("pause(...) called on a released ThinDownloadManager.");
        return mRequestQueue.pause(downloadId);
    }

    @Override
    public void pauseAll() {
        checkReleased("pauseAll() called on a released ThinDownloadManager.");
        mRequestQueue.pauseAll();
    }

    @Override
    public int query(int downloadId) {
        checkReleased("query(...) called on a released ThinDownloadManager.");
        return mRequestQueue.query(downloadId);
    }

    @Override
    public void release() {
        if (!isReleased()) {
            mRequestQueue.release();
            mRequestQueue = null;
        }
    }

    @Override
    public boolean isReleased() {
        return mRequestQueue == null;
    }

    /**
     * This is called by methods that want to throw an exception if the EzDownloadManager
     * has already been released.
     */
    private void checkReleased(String errorMessage) {
        if (isReleased()) {
            throw new IllegalStateException(errorMessage);
        }
    }
}
