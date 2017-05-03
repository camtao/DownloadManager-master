package com.htao.downloadmanager.downloader;

import android.os.Handler;
import android.os.Message;

/**
 * Created by 10607 on 2017/4/30.
 */

public class DownloadTask implements Runnable{

    private final Handler mHandler;
    private  DownloadEntry entry;
    private boolean isPaused;
    private boolean isCanceled;

    public DownloadTask(DownloadEntry entry, Handler mHandler) {
        this.entry=entry;
        this.mHandler=mHandler;
    }

    public void start() {
        entry.mDownloadStatus= DownloadEntry.DownloadStatus.downloading;
        int totalLength=1024*50;
        entry.totalLength=totalLength;

        Message message = mHandler.obtainMessage();
        message.obj=entry;
        mHandler.sendMessage(message);
        for (int i= entry.curLength; i <entry.totalLength; ) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isPaused||isCanceled){
                entry.mDownloadStatus=isPaused? DownloadEntry.DownloadStatus.paused: DownloadEntry.DownloadStatus.cancelled;
                message = mHandler.obtainMessage();
                message.what=DownloadService.PAUSEDORCANCELED;
                message.obj=entry;
                mHandler.sendMessage(message);
                return;
            }
            entry.curLength+=1024;
            i+=1024;
            message = mHandler.obtainMessage();
            message.obj=entry;
            mHandler.sendMessage(message);
//            DownloadChanger.getInstance().postChange(entry);
        }
        entry.mDownloadStatus= DownloadEntry.DownloadStatus.completed;
        message = mHandler.obtainMessage();
        message.obj=entry;
        message.what=DownloadService.COMPTELED;
        mHandler.sendMessage(message);
//        DownloadChanger.getInstance().postChange(entry);
    }

    public void pause() {
        isPaused=true;
    }

    public void cancel() {
        isCanceled= true;
    }

    @Override
    public void run() {
        start();
    }
}
