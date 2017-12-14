package br.ufpe.cin.if710.podcast.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.ufpe.cin.if710.podcast.db.BaseDados;
import br.ufpe.cin.if710.podcast.db.ItemFeedDaoRoom;
import br.ufpe.cin.if710.podcast.db.PodcastProvider;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.domain.XmlFeedParser;

/**
 * Created by isaacdouglas1 on 14/10/17.
 */

public class DownloadXMLService extends IntentService {

    public static final String DOWNLOAD_XML_COMPLETE = "br.ufpe.cin.if710.podcast.action.DOWNLOAD_XML_COMPLETE";

    public DownloadXMLService() {
        super("DownloadXMLService");
    }

    //TODO Opcional - pesquise outros meios de obter arquivos da internet
    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed = "";
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }

    @Override
    public void onHandleIntent(Intent i) {

        //Recupera o rss
        Bundle params = i.getExtras();
        String rss = (String) params.get("rss");

        List<ItemFeed> itemList = new ArrayList<>();
        try {
            itemList = XmlFeedParser.parse(getRssFeed(rss));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        //verifica se tem item novo no parse, se tiver salva no banco
        boolean atualizou = false;

        ContentResolver cr;
        ContentValues cv = new ContentValues();

        PodcastProvider pv = new PodcastProvider();

        //inserindo os dados no Room
        ItemFeedDaoRoom db = BaseDados.getBaseDados(getApplicationContext()).itemFeedDaoRoom() ;
        for (ItemFeed item : itemList) {
            db.inserir(item);
        }

        //inserindo os dados no sqllite
        for (ItemFeed item : itemList) {

            ContentResolver crAux = getContentResolver();
            String selection = PodcastProviderContract.TITLE + " = ?";
            String[] selectionArgs = new String[]{item.getTitle()};
            Cursor c = crAux.query(PodcastProviderContract.EPISODE_LIST_URI, null, selection, selectionArgs, null);

            atualizou = atualizou || (c.getCount() == 0);

            if (c.getCount() == 0) {
                cr = getContentResolver();
                cv.put(PodcastProviderContract.TITLE, item.getTitle());
                cv.put(PodcastProviderContract.LINK, item.getLink());
                cv.put(PodcastProviderContract.DATE, item.getPubDate());
                cv.put(PodcastProviderContract.DESCRIPTION, item.getDescription());
                cv.put(PodcastProviderContract.DOWNLOAD_LINK, item.getDownloadLink());
                cv.put(PodcastProviderContract.URI, "");
                cv.put(PodcastProviderContract.TIME_PAUSED, "0");
                cr.insert(PodcastProviderContract.EPISODE_LIST_URI, cv);
            }
        }

        //prepara o intent para passar o broadcast do fim do download
        Intent downloadXmlComplete = new Intent(DOWNLOAD_XML_COMPLETE);
        downloadXmlComplete.putExtra("atualizou", atualizou); // se atualizou
        getApplicationContext().sendBroadcast(downloadXmlComplete);
    }
}
