package br.ufpe.cin.if710.podcast.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

import br.ufpe.cin.if710.podcast.ui.MainActivity;

/**
 * Created by isaacdouglas1 on 14/10/17.
 */

public class JobSchedulerTime extends JobService {


    @Override
    public boolean onStartJob(JobParameters params) {

        Intent downloadService = new Intent(getApplicationContext(), DownloadXMLService.class);
        downloadService.putExtra("rss", MainActivity.RSS_FEED_PUBLIC);
        getApplicationContext().startService(downloadService);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
