package br.ufpe.cin.if710.podcast.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.db.PodcastProvider;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.domain.XmlFeedParser;
import br.ufpe.cin.if710.podcast.service.DownloadXMLService;
import br.ufpe.cin.if710.podcast.service.PlayPauseMusicService;
import br.ufpe.cin.if710.podcast.ui.adapter.XmlFeedAdapter;

public class MainActivity extends Activity {

    //ao fazer envio da resolucao, use este link no seu codigo!
    private final String RSS_FEED = "http://leopoldomt.com/if710/fronteirasdaciencia.xml";
    //TODO teste com outros links de podcast

    // Notification ID permite associar e agrupar notificacoes no futuro
    private static final int MY_NOTIFICATION_ID = 3;

    public static final String DOWNLOAD_COMPLETE = "br.ufpe.cin.if710.podcast.action.DOWNLOAD_COMPLETE";
    public static final String DOWNLOAD_XML_COMPLETE = "br.ufpe.cin.if710.podcast.action.DOWNLOAD_XML_COMPLETE";
    public static final String MUSIC_PAUSE = "br.ufpe.cin.if710.podcast.action.MUSIC_PAUSE";

    private ListView items;

    boolean primeiroPlano = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = (ListView) findViewById(R.id.items);
        items.setTextFilterEnabled(true);
        items.setClickable(true);

        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                XmlFeedAdapter adapter = (XmlFeedAdapter) parent.getAdapter();
                ItemFeed item = adapter.getItem(position);
                final String msg = "Abrindo: " + item.getTitle();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                //abre a tela de detalhes do item
                Intent i = new Intent(getApplicationContext(), EpisodeDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Item", item);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    private void tela(){

        //Responsavel por atualizar a tela se mudar algo no banco

        Cursor c = getContentResolver().query(PodcastProviderContract.EPISODE_LIST_URI, null, null, null, null);
        ArrayList<ItemFeed> itemFeed = new ArrayList<>();
        while (c.moveToNext()) {
            String title = c.getString(c.getColumnIndex(PodcastProviderContract.TITLE));
            String link = c.getString(c.getColumnIndex(PodcastProviderContract.EPISODE_LINK));
            String pubDate = c.getString(c.getColumnIndex(PodcastProviderContract.DATE));
            String description = c.getString(c.getColumnIndex(PodcastProviderContract.DESCRIPTION));
            String downloadLink = c.getString(c.getColumnIndex(PodcastProviderContract.DOWNLOAD_LINK));

            String uriString = c.getString(c.getColumnIndex(PodcastProviderContract.EPISODE_URI));
            uriString = (uriString.length()==0) ? null : uriString;

            Integer timePaused = c.getInt(c.getColumnIndex(PodcastProviderContract.TIME_PAUSED));

            ItemFeed item = new ItemFeed(title, link, pubDate, description, downloadLink);
            item.setTimePaused(timePaused);
            item.setUri(uriString);
            itemFeed.add(item);
        }

        //Adapter Personalizado
        XmlFeedAdapter adapter = new XmlFeedAdapter(getApplicationContext(), R.layout.itemlista, itemFeed);

        //atualizar o list view
        items.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        primeiroPlano = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        primeiroPlano = false;
        super.onPause();
    }

    @Override
    protected void onStart() {
        primeiroPlano = true;
        super.onStart();

        //Atualizar a view
        tela();

        //registrando
        registerReceiver(downloadCompleto, new IntentFilter(DOWNLOAD_COMPLETE));
        registerReceiver(musicPause, new IntentFilter(MUSIC_PAUSE));
        registerReceiver(downloadXMLComplete, new IntentFilter(DOWNLOAD_XML_COMPLETE));

        //Iniciando o download do xml
        Intent downloadXMLService = new Intent(getApplicationContext(), DownloadXMLService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("rss", RSS_FEED);
        downloadXMLService.putExtras(bundle);
        getApplicationContext().startService(downloadXMLService);
    }

    @Override
    protected void onStop() {
        primeiroPlano = false;
        super.onStop();
        XmlFeedAdapter adapter = (XmlFeedAdapter) items.getAdapter();
        adapter.clear();
    }

    @Override
    protected void onDestroy() {
        primeiroPlano = false;
        super.onDestroy();

        //desregistrando
        unregisterReceiver(downloadCompleto);
        unregisterReceiver(musicPause);
        unregisterReceiver(downloadXMLComplete);
    }

    BroadcastReceiver downloadCompleto = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Download Completo", Toast.LENGTH_SHORT).show();

            //Recupera o Item do intent
            Bundle params = intent.getExtras();
            ItemFeed itemFeed = (ItemFeed)params.get("Item");

            ContentResolver cr = getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(PodcastDBHelper.EPISODE_FILE_URI, params.get("uri").toString());
            String selection = PodcastProviderContract.TITLE + " = ?";
            String[] selectionArgs = new String[]{itemFeed.getTitle()};
            cr.update(PodcastProviderContract.EPISODE_LIST_URI, cv, selection, selectionArgs);

                //Atualizar a view
                tela();
        }
    };

    BroadcastReceiver musicPause = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Pausou...", Toast.LENGTH_SHORT).show();

            //Recupera o Item do intent
            Bundle params = intent.getExtras();
            ItemFeed itemFeed = (ItemFeed)params.get("Item");

            ContentResolver cr = getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(PodcastDBHelper.EPISODE_TIME_PAUSED, itemFeed.getTimePaused());
            String selection = PodcastProviderContract.TITLE + " = ?";
            String[] selectionArgs = new String[]{itemFeed.getTitle()};
            cr.update(PodcastProviderContract.EPISODE_LIST_URI, cv, selection, selectionArgs);

            //Atualizar a view
            tela();
        }
    };

    BroadcastReceiver downloadXMLComplete = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Download XML Completo", Toast.LENGTH_SHORT).show();

            Bundle params = intent.getExtras();
            boolean atualizou = (Boolean) params.get("atualizou");

            View rootView = getWindow().getDecorView().getRootView();

            //Verifica se esta em primeiro plano
            if (rootView.isShown()){
                //se tem item novo no banco atualiza a tela
                if (atualizou){
                    //Atualizar a view
                    tela();
                    Toast.makeText(getApplicationContext(), "Atualizou", Toast.LENGTH_SHORT).show();
                }
            }else {
                final Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
                final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

                final Notification notification = new Notification.Builder(
                        getApplicationContext())
                        .setSmallIcon(android.R.drawable.btn_star)
                        .setAutoCancel(true)
                        .setOngoing(true).setContentTitle("Podcasts Atualizados")
                        .setContentText("Acesse para ver as novidades!")
                        .setContentIntent(pendingIntent)
                        .build();

                NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(MY_NOTIFICATION_ID, notification);
            }


        }
    };
}
