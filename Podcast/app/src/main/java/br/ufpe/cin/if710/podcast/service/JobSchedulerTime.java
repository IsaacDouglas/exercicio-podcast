package br.ufpe.cin.if710.podcast.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Bundle;

import br.ufpe.cin.if710.podcast.ui.MainActivity;

/**
 * Created by isaacdouglas1 on 14/10/17.
 */

public class JobSchedulerTime extends JobService {


    @Override
    public boolean onStartJob(JobParameters params) {

        //Iniciando o download do xml
        Intent downloadXMLService = new Intent(getApplicationContext(), DownloadXMLService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("rss", MainActivity.RSS_FEED_PUBLIC);
        downloadXMLService.putExtras(bundle);
        getApplicationContext().startService(downloadXMLService);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Intent downloadXMLService = new Intent (getApplicationContext(),DownloadXMLService.class);
        getApplicationContext().stopService(downloadXMLService);
        return false;
    }
}
