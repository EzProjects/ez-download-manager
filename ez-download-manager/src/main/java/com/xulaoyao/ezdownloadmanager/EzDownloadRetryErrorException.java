package com.xulaoyao.ezdownloadmanager;

/**
 * 下载异常队象
 * EzDownloadRetryErrorException
 * Created by renwoxing on 2018/2/7.
 */
public class EzDownloadRetryErrorException extends Exception {

    public EzDownloadRetryErrorException() {
        super("Maximum retry exceeded");
    }

    public EzDownloadRetryErrorException(Throwable cause) {
        super(cause);
    }
}
