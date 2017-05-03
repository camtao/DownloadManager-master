package com.htao.downloadmanager.downloader;

import android.content.Context;
import android.content.Intent;

/**
 * Created by 10607 on 2017/4/30.
 */

public class DownloadManager {

    private static DownloadManager instance ;
    private final Context mContext;

    private DownloadManager(Context context){
        mContext =context;
    }

    public static DownloadManager getInstance(Context context){
        if (instance == null) {
            synchronized (DownloadManager.class){
                if (instance == null) {
                    instance = new DownloadManager(context) ;
                }
            }
        }
        return instance ;
    }

    public void add(DownloadEntry downloadEntry){
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY,downloadEntry);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION,Constants.KEY_DOWNLOAD_ACTION_ADD);
        mContext.startService(intent);

    }

    public void pause(DownloadEntry downloadEntry){
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY,downloadEntry);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION,Constants.KEY_DOWNLOAD_ACTION_PAUSE);
        mContext.startService(intent);
    }

    public void cancel(DownloadEntry downloadEntry){
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY,downloadEntry);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION,Constants.KEY_DOWNLOAD_ACTION_CANCEL);
        mContext.startService(intent);

    }

    public void resume(DownloadEntry downloadEntry){
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY,downloadEntry);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION,Constants.KEY_DOWNLOAD_ACTION_RESUME);
        mContext.startService(intent);

    }

    public void addWatcher(DataWatcher dataWatcher){
        DownloadChanger.getInstance().addObserver(dataWatcher);
    }

    public void deteleWatcher(DataWatcher dataWatcher){
        DownloadChanger.getInstance().deleteObserver(dataWatcher);
    }
}
