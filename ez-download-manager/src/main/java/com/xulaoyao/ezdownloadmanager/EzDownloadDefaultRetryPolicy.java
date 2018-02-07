package com.xulaoyao.ezdownloadmanager;

/**
 * EzDownloadDefaultRetryPolicy
 * Created by renwoxing on 2018/2/7.
 */
public class EzDownloadDefaultRetryPolicy implements IEzDownloadRetryPolicy {

    /**
     * The current timeout in milliseconds.
     */
    private int mCurrentTimeoutMs;

    /**
     * The current retry count.
     */
    private int mCurrentRetryCount;

    /**
     * The maximum number of attempts.
     */
    private final int mMaxNumRetries;

    /**
     * The backoff multiplier for for the policy.
     */
    private final float mBackoffMultiplier;

    /**
     * The default socket timeout in milliseconds
     */
    public static final int DEFAULT_TIMEOUT_MS = 5000;

    /**
     * The default number of retries
     */
    public static final int DEFAULT_MAX_RETRIES = 1;

    /**
     * The default backoff multiplier
     */
    public static final float DEFAULT_BACKOFF_MULT = 1f;

    /**
     * Constructs a new retry policy using the default timeouts.
     */
    public EzDownloadDefaultRetryPolicy() {
        this(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT);
    }

    /**
     * Constructs a new retry policy.
     *
     * @param initialTimeoutMs  The initial timeout for the policy.
     * @param maxNumRetries     The maximum number of retries.
     * @param backoffMultiplier Backoff multiplier for the policy.
     */
    public EzDownloadDefaultRetryPolicy(int initialTimeoutMs, int maxNumRetries, float backoffMultiplier) {
        mCurrentTimeoutMs = initialTimeoutMs;
        mMaxNumRetries = maxNumRetries;
        mBackoffMultiplier = backoffMultiplier;
    }


    @Override
    public float getBackOffMultiplier() {
        return mBackoffMultiplier;
    }

    @Override
    public int getCurrentTimeout() {
        return mCurrentTimeoutMs;
    }

    @Override
    public int getCurrentRetryCount() {
        return mCurrentRetryCount;
    }

    @Override
    public void retry() throws EzDownloadRetryErrorException {
        mCurrentRetryCount++;
        mCurrentTimeoutMs += (mCurrentTimeoutMs * mBackoffMultiplier);
        if (!hasAttemptRemaining()) {
            throw new EzDownloadRetryErrorException();
        }
    }

    /**
     * Returns true if this policy has attempts remaining, false otherwise.
     */
    protected boolean hasAttemptRemaining() {
        return mCurrentRetryCount <= mMaxNumRetries;
    }
}
