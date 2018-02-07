package com.xulaoyao.ezdownloadmanager;

/**
 * 下载管理器 接口事件
 * IEzDownloadManagerListener
 * Created by renwoxing on 2018/2/7.
 */
public interface IEzDownloadManagerListener {

    /**
     * 新增
     * @param request
     * @return
     */
    int add(EzDownloadRequest request);

    /**
     * 放弃下载
     * @param downloadId
     * @return
     */
    int cancel(int downloadId);

    /**
     * 放弃所有下载队列
     */
    void cancelAll();

    /**
     * 暂停下载
     * @param downloadId
     * @return
     */
    int pause(int downloadId);

    /**
     * 暂停所有下载
     */
    void pauseAll();

    /**
     * 请求
     */
    int query(int downloadId);

    /**
     * 释放
     */
    void release();

    /**
     *
     * @return
     */
    boolean isReleased();
}
