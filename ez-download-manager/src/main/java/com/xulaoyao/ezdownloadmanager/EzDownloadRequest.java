package com.xulaoyao.ezdownloadmanager;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * 下载请求队象实体
 * EzDownloadRequest
 * Created by renwoxing on 2018/2/7.
 */
public class EzDownloadRequest implements Comparable<EzDownloadRequest> {

    /**
     * Priority values.  Requests will be processed from higher priorities to
     * lower priorities, in FIFO order.
     */
    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }

    /**
     * Tells the current download state of this request
     */
    private int mDownloadState;

    /**
     * Download Id assigned to this request
     */
    private int mDownloadId;

    /**
     * The URI resource that this request is to download
     */
    private Uri mUri;

    /**
     * The destination path on the device where the downloaded files needs to be put
     * It can be either External Directory ( SDcard ) or
     * internal app cache or files directory.
     * For using external SDCard access, application should have
     * this permission android.permission.WRITE_EXTERNAL_STORAGE declared.
     */
    private Uri mDestinationURI;

    private IEzDownloadRetryPolicy mRetryPolicy;

    /**
     * Whether or not this request has been canceled.
     */
    private boolean mCancelled = false;

    private boolean mDeleteDestinationFileOnFailure = true;

    private EzDownloadRequestQueue mRequestQueue;

    //private DownloadStatusListener mDownloadListener;

    private IEzDownloadResponseListener mDownloadStatusListener;

    private Object mDownloadContext;

    private HashMap<String, String> mCustomHeader;
    private Priority mPriority = Priority.NORMAL;

    private boolean isDownloadResumable = false;

    public EzDownloadRequest(Uri uri) {
        if (uri == null) {
            throw new NullPointerException();
        }

        String scheme = uri.getScheme();
        if (scheme == null || (!scheme.equals("http") && !scheme.equals("https"))) {
            throw new IllegalArgumentException("Can only download HTTP/HTTPS URIs: " + uri);
        }
        mCustomHeader = new HashMap<>();
        mDownloadState = EzDownloadStatus.STATUS_PENDING;
        mUri = uri;
    }

    /**
     * Returns the {@link Priority} of this request; {@link Priority#NORMAL} by default.
     */
    public Priority getPriority() {
        return mPriority;
    }

    /**
     * Set the {@link Priority}  of this request;
     *
     * @param priority
     * @return request
     */
    public EzDownloadRequest setPriority(Priority priority) {
        mPriority = priority;
        return this;
    }

    /**
     * Adds custom header to request
     *
     * @param key
     * @param value
     */
    public EzDownloadRequest addCustomHeader(String key, String value) {
        mCustomHeader.put(key, value);
        return this;
    }

    /**
     * Associates this request with the given queue. The request queue will be notified when this
     * request has finished.
     */
    void setDownloadRequestQueue(EzDownloadRequestQueue downloadQueue) {
        mRequestQueue = downloadQueue;
    }

    public IEzDownloadRetryPolicy getRetryPolicy() {
        return mRetryPolicy == null ? new EzDownloadDefaultRetryPolicy() : mRetryPolicy;
    }

    public EzDownloadRequest setRetryPolicy(IEzDownloadRetryPolicy mRetryPolicy) {
        this.mRetryPolicy = mRetryPolicy;
        return this;
    }

    /**
     * Gets the download id.
     *
     * @return the download id
     */
    public final int getDownloadId() {
        return mDownloadId;
    }

    /**
     * Sets the download Id of this request.  Used by {@link EzDownloadRequestQueue}.
     */
    final void setDownloadId(int downloadId) {
        mDownloadId = downloadId;
    }

    int getDownloadState() {
        return mDownloadState;
    }

    void setDownloadState(int mDownloadState) {
        this.mDownloadState = mDownloadState;
    }


    /**
     * Gets the status listener. For internal use.
     *
     * @return the status listener
     */
    IEzDownloadResponseListener getStatusListener() {
        return mDownloadStatusListener;
    }

    /**
     * Sets the status listener for this download request. Download manager sends progress,
     * failure and completion updates to this listener for this download request.
     *
     * @param downloadStatusListener the status listener for this download
     */
    public EzDownloadRequest setStatusListener(IEzDownloadResponseListener downloadStatusListener) {
        mDownloadStatusListener = downloadStatusListener;
        return this;
    }

    public Object getDownloadContext() {
        return mDownloadContext;
    }

    public EzDownloadRequest setDownloadContext(Object downloadContext) {
        mDownloadContext = downloadContext;
        return this;
    }

    public Uri getUri() {
        return mUri;
    }

    public EzDownloadRequest setUri(Uri mUri) {
        this.mUri = mUri;
        return this;
    }

    public Uri getDestinationURI() {
        return mDestinationURI;
    }

    public EzDownloadRequest setDestinationURI(Uri destinationURI) {
        this.mDestinationURI = destinationURI;
        return this;
    }

    public boolean getDeleteDestinationFileOnFailure() {
        return mDeleteDestinationFileOnFailure;
    }

    /**
     * It marks the request with resumable feature and It is an optional feature
     *
     * @param isDownloadResumable - It enables resumable feature for this request
     * @return - current {@link EzDownloadRequest}
     */
    public EzDownloadRequest setDownloadResumable(boolean isDownloadResumable) {
        this.isDownloadResumable = isDownloadResumable;
        setDeleteDestinationFileOnFailure(false); // If resumable feature enabled, downloaded file should not be deleted.
        return this;
    }

    public boolean isResumable() {
        return isDownloadResumable;
    }

    /**
     * Set if destination file should be deleted on download failure.
     * Use is optional: default is to delete.
     */
    public EzDownloadRequest setDeleteDestinationFileOnFailure(boolean deleteOnFailure) {
        this.mDeleteDestinationFileOnFailure = deleteOnFailure;
        return this;
    }

    /**
     * Mark this request as canceled.  No callback will be delivered.
     */
    public void cancel() {
        mCancelled = true;
    }

    //Package-private methods.

    /**
     * Returns true if this request has been canceled.
     */
    public boolean isCancelled() {
        return mCancelled;
    }


    /**
     * Marked the request as canceled is aborted.
     */
    public void abortCancel() {
        mCancelled = false;
    }

    /**
     * Returns all custom headers set by user
     *
     * @return
     */
    HashMap<String, String> getCustomHeaders() {
        return mCustomHeader;
    }

    void finish() {
        mRequestQueue.finish(this);
    }


    @Override
    public int compareTo(@NonNull EzDownloadRequest otherEzDownloadRequest) {
        Priority left = this.getPriority();
        Priority right = otherEzDownloadRequest.getPriority();

        // High-priority requests are "lesser" so they are sorted to the front.
        // Equal priorities are sorted by sequence number to provide FIFO ordering.
        return left == right ?
                this.mDownloadId - otherEzDownloadRequest.mDownloadId :
                right.ordinal() - left.ordinal();
    }
}
