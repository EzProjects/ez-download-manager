package com.xulaoyao.ezdownloadmanager.demo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xulaoyao.ezdownloadmanager.EzDownloadDefaultRetryPolicy;
import com.xulaoyao.ezdownloadmanager.EzDownloadManager;
import com.xulaoyao.ezdownloadmanager.EzDownloadRequest;
import com.xulaoyao.ezdownloadmanager.IEzDownloadResponseListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private EzDownloadManager downloadManager;
    private EzDownloadRequest downloadRequest;
    private static final int DOWNLOAD_THREAD_POOL_SIZE = 4;
    private int downloadId;

    Button btnDownload;
    TextView tvProgress;
    ProgressBar pbpProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDownload = findViewById(R.id.button_download_headers);
        tvProgress = findViewById(R.id.progressTxt);
        pbpProgress = findViewById(R.id.progress);

        initDownload();

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadManager = new EzDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
                downloadId = downloadManager.add(downloadRequest);
            }
        });


    }

    private void initDownload() {

        Uri downloadUri = Uri.parse("https://github.com/Genymobile/gnirehtet/releases/download/v2.1/gnirehtet-rust-linux64-v2.1.zip");
        Uri destinationUri = Uri.parse(this.getExternalCacheDir().toString() + "/test.zip");
        downloadRequest = new EzDownloadRequest(downloadUri);
        downloadRequest.addCustomHeader("Auth-Token", "YourTokenApiKey");
        downloadRequest.setRetryPolicy(new EzDownloadDefaultRetryPolicy());
        downloadRequest.setDestinationURI(destinationUri);
        downloadRequest.setPriority(EzDownloadRequest.Priority.HIGH);
        downloadRequest.setDownloadResponseListener(new IEzDownloadResponseListener() {
            @Override
            public void onDownloadComplete(EzDownloadRequest downloadRequest) {
                Log.d(TAG, "onDownloadComplete: ---");
            }

            @Override
            public void onDownloadFailed(EzDownloadRequest downloadRequest, int errorCode, String errorMessage) {
                Log.d(TAG, "onDownloadFailed: id:" + downloadRequest.getDownloadId());
            }

            @Override
            public void onProgress(EzDownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                Log.d(TAG, "onProgress: id:" + downloadRequest.getDownloadId() + " progress:" + progress);
                tvProgress.setText("Download id: " + downloadRequest.getDownloadId() + ", " + progress + "%" + "  " + getBytesDownloaded(progress, totalBytes));
                pbpProgress.setProgress(progress);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        downloadManager.release();
    }


    private String getBytesDownloaded(int progress, long totalBytes) {
        //Greater than 1 MB
        long bytesCompleted = (progress * totalBytes) / 100;
        if (totalBytes >= 1000000) {
            return ("" + (String.format("%.1f", (float) bytesCompleted / 1000000)) + "/" + (String.format("%.1f", (float) totalBytes / 1000000)) + "MB");
        }
        if (totalBytes >= 1000) {
            return ("" + (String.format("%.1f", (float) bytesCompleted / 1000)) + "/" + (String.format("%.1f", (float) totalBytes / 1000)) + "Kb");

        } else {
            return ("" + bytesCompleted + "/" + totalBytes);
        }
    }
}
