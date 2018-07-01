package com.alcwithgoogle.journalapp.utils;

import android.os.Looper;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Global executor pools for the whole application.
 * <p>
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */

public class DiaryExecutors {

    // singleton instanciation
    private static final Object LOCK = new Object();
    private static DiaryExecutors mInstance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;

    // private constructor to avoid class instanciation
    private DiaryExecutors(Executor diskIO, Executor mainThread, Executor networkIO) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;
    }

    // static getInstance method
    public static DiaryExecutors getInstance() {
        if (mInstance == null) {
            synchronized (LOCK) {
                mInstance = new DiaryExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3), new MainThreadExecutor());
            }
        }
        return mInstance;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {

        private Handler newMainThreadHandler = new Handler(Looper.getMainLooper());


        @Override
        public void execute(@NonNull Runnable runnable) {
            newMainThreadHandler.post(runnable);
        }
    }
}
