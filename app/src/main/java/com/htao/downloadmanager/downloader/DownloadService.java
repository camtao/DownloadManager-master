package com.htao.downloadmanager.downloader;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by 10607 on 2017/4/30.
 */

public class DownloadService extends Service {

    public static final int PAUSEDORCANCELED = 1;
    public static final int COMPTELED = 2;
    public HashMap<String, DownloadTask> downloadTasks = new HashMap<>();
    private ExecutorService mExecutorService;
    private  LinkedBlockingDeque<DownloadEntry> mWaitingQeque=new LinkedBlockingDeque<>();

    public  Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PAUSEDORCANCELED:
                case COMPTELED:
                    DownloadEntry entry = mWaitingQeque.poll();
                    if (entry!=null){
                        startDownload(entry);
                    }
                    break;
            }
            DownloadEntry downloadEntry= (DownloadEntry) msg.obj;
            DownloadChanger.getInstance().postChange(downloadEntry);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mExecutorService = Executors.newCachedThreadPool();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        DownloadEntry downloadEntry = (DownloadEntry) intent.getSerializableExtra(Constants.KEY_DOWNLOAD_ENTRY);
        int action = intent.getIntExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_ADD);
        doAction(action, downloadEntry);
    }

    private void doAction(int action, DownloadEntry entry) {
        switch (action) {
            case Constants.KEY_DOWNLOAD_ACTION_ADD:
                startDownload(entry);
                break;
            case Constants.KEY_DOWNLOAD_ACTION_PAUSE:
                pauseDownload(entry);
                break;
            case Constants.KEY_DOWNLOAD_ACTION_RESUME:
                resumeDownload(entry);
                break;
            case Constants.KEY_DOWNLOAD_ACTION_CANCEL:
                cancelDownload(entry);
                break;
        }
    }

    private void resumeDownload(DownloadEntry entry) {
        startDownload(entry);
    }

    private void cancelDownload(DownloadEntry entry) {
        DownloadTask downloadTask = downloadTasks.remove(entry.id);
        if (downloadTask!=null){
            downloadTask.cancel();
        }else{
            mWaitingQeque.remove(entry);
            entry.mDownloadStatus= DownloadEntry.DownloadStatus.cancelled;
            DownloadChanger.getInstance().postChange(entry);
        }
    }

    private void pauseDownload(DownloadEntry entry) {
        DownloadTask downloadTask = downloadTasks.remove(entry.id);
        if (downloadTask!=null){
            downloadTask.pause();
        }else{
            mWaitingQeque.remove(entry);
            entry.mDownloadStatus= DownloadEntry.DownloadStatus.paused;
            DownloadChanger.getInstance().postChange(entry);
        }

    }

    private void startDownload(DownloadEntry entry) {
        if (downloadTasks.size()>=Constants.MAX_DOWNLOADTASK_NUM){
            mWaitingQeque.offer(entry);
            entry.mDownloadStatus= DownloadEntry.DownloadStatus.waiting;
            DownloadChanger.getInstance().postChange(entry);
        }else{
            DownloadTask downloadTask = new DownloadTask(entry,mHandler);
            downloadTasks.put(entry.id, downloadTask);
            mExecutorService.execute(downloadTask);
        }
    }

}
