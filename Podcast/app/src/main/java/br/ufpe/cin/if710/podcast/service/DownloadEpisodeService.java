package br.ufpe.cin.if710.podcast.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;

/**
 * Created by isaacdouglas1 on 08/10/17.
 */

public class DownloadEpisodeService extends IntentService {

    public static final String DOWNLOAD_COMPLETE = "br.ufpe.cin.if710.podcast.action.DOWNLOAD_COMPLETE";
    public Uri uri;

    public DownloadEpisodeService() {
        super("DownloadEpisodeService");
    }

    @Override
    public void onHandleIntent(Intent i) {
        try {
            Bundle params = i.getExtras();
            final ItemFeed itemFeed = (ItemFeed)params.get("Item"); //recupera o item do feed

            //checar se tem permissao... Android 6.0+
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            root.mkdirs();
            uri = Uri.parse(itemFeed.getDownloadLink()); // pegando a URI
            final File output = new File(root, uri.getLastPathSegment());
            if (output.exists()) {
                output.delete();
            }

            //realiza o download
            URL url = new URL(uri.toString());
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            FileOutputStream fos = new FileOutputStream(output.getPath());
            BufferedOutputStream out = new BufferedOutputStream(fos);
            try {
                InputStream in = c.getInputStream();
                byte[] buffer = new byte[8192];
                int len = 0;
                while ((len = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, len);
                }
                out.flush();
            }
            finally {
                fos.getFD().sync();
                out.close();
                c.disconnect();
            }


            final Uri uriDownload = Uri.parse(output.toString()); //prepara a uri de onde foi salvo o download

            //prepara o intent para passar o broadcast do fim do download
            Intent downloadComplete = new Intent(DOWNLOAD_COMPLETE);
            downloadComplete.putExtra("uri", uriDownload); // passa a uri de download
            downloadComplete.putExtra("Item", itemFeed); //passa o item que foi realizado o download
            getApplicationContext().sendBroadcast(downloadComplete);


        } catch (IOException e2) {
            Log.e(getClass().getName(), "Exception durante download", e2);
        }
    }

}
