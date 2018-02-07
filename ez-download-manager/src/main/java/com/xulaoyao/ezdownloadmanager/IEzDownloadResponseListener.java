package com.xulaoyao.ezdownloadmanager;

/**
 * 下载回调接口
 * IEzDownloadResponseListener
 * Created by renwoxing on 2018/2/7.
 */
public interface IEzDownloadResponseListener {

    /**
     * This method is invoked when download is complete.
     *
     * @param downloadRequest the download request provided by the client
     */
    void onDownloadComplete(EzDownloadRequest downloadRequest);


    /**
     * This method is invoked when download has failed.
     *
     * @param downloadRequest the download request provided by the client
     * @param errorCode       the download error code
     * @param errorMessage    the error message
     */
    void onDownloadFailed(EzDownloadRequest downloadRequest, int errorCode, String errorMessage);

    /**
     * This method is invoked on a progress update.
     *
     * @param downloadRequest the download request provided by the client
     * @param totalBytes      the total bytes
     * @param downloadedBytes bytes downloaded till now
     * @param progress        the progress of download
     */
    void onProgress(EzDownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress);
}
