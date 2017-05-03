package com.htao.downloadmanager.downloader;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by 10607 on 2017/4/30.
 */

public abstract class DataWatcher implements Observer{
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof DownloadEntry){
            notifyUpdate((DownloadEntry) arg);
        }
    }

    public abstract void notifyUpdate(DownloadEntry downloadEntry) ;
}
