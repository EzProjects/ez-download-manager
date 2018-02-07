# ez-download-manager
thin, thin, super thin


## Usuage
#### **IEzDownloadResponseListener**
  * Provides call back option to know when the download is completed, failed and reason for failure, and to know the progress of the download. EzDownloadRequest is given back in the callback so that you can easily set some Object as context to download request and get the context object back from the request object.
``` java
    //Callback when download is successfully completed
    void onDownloadComplete(EzDownloadRequest downloadRequest);

    //Callback if download is failed. Corresponding error code and
    //error messages are provided
    void onDownloadFailed(EzDownloadRequest downloadRequest, int errorCode, String errorMessage);


    //Callback provides download progress
    void onProgress(EzDownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress);

```

#### **EzDownloadRequest**
  * Takes all the necessary information required for download.
  * Download URI, Destination URI.
  * Set Priority for request as HIGH or MEDIUM or LOW.
  * Takes Callback listener IEzDownloadResponseListener
  * Use custom Http Headers for a download request
  * Resumable a download if network connection drops or download is paused.
  * You can set a Retry Policy

     ``` java
        Uri downloadUri = Uri.parse("https://github.com/Genymobile/gnirehtet/releases/download/v2.1/gnirehtet-rust-linux64-v2.1.zip");
        Uri destinationUri = Uri.parse(this.getExternalCacheDir().toString() + "/test.zip");
        downloadRequest = new EzDownloadRequest(downloadUri);
        downloadRequest.addCustomHeader("Auth-Token", "YourTokenApiKey");
        downloadRequest.setRetryPolicy(new EzDownloadDefaultRetryPolicy());
        downloadRequest.setDestinationURI(destinationUri);
        downloadRequest.setPriority(EzDownloadRequest.Priority.HIGH);
        downloadRequest.setDownloadContext(downObjId);
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
            }
        });


     ```

#### **EzDownloadManager**
  * The number of threads used to perform parallel download is determined by the available processors on the device. Uses `Runtime.getRuntime().availableProcessors()` api.
  
  	``` java
    private EzDownloadManager downloadManager = new EzDownloadManager();
    ```

  * To start a download use *add( EzDownloadRequest request)*
   	```java
   	int downloadId = downloadManager.add(downloadRequest);
   	```

  * To cancel a particular download use *cancel(int downloadId)* by passing download id.
  	- Returns 1 if successfull cancelled.
  	- Returns -1 if supplied download id is not found.

  	```java
  	int status = downloadManager.cancel(downloadId);
  	```

  * To cancel all running requests use *cancelAll()*
  	```java
  	downloadManager.cancelAll();
  	```

  * To query for a particular download use *query(int downloadId)*

    The possible status could be
  	- STATUS_PENDING
  	- STATUS_STARTED
  	- STATUS_RUNNING

  	```java
  	int status = downloadManager.query(downloadId);
  	```
  * To pause a download in progress. The download request has to be marked as `setDownloadResumable` to true
    ``` java
    downloadManager.pause(downloadId)
    ```
  * To release all the resources used by download manager use *release()*.

  	```java
  	downloadManager.release();
  	```


## No Permissions Required
  * Unless if you specify download destination to be in external public SDCard location.You might need *android.permission.WRITE_EXTERNAL_STORAGE* permission.
