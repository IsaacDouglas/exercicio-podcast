package br.ufpe.cin.if710.podcast.db;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Created by leopoldomt on 9/19/17.
 */

public class PodcastProviderContract {

    public static final String _ID = PodcastDBHelper._ID;
    public static final String TITLE = PodcastDBHelper.EPISODE_TITLE;
    public static final String DATE = PodcastDBHelper.EPISODE_DATE;
    public static final String LINK = PodcastDBHelper.EPISODE_LINK;
    public static final String DESCRIPTION = PodcastDBHelper.EPISODE_DESC;
    public static final String DOWNLOAD_LINK = PodcastDBHelper.EPISODE_DOWNLOAD_LINK;
    public static final String URI = PodcastDBHelper.EPISODE_FILE_URI;
    public final static String TIME_PAUSED = PodcastDBHelper.EPISODE_TIME_PAUSED;
    public static final String EPISODE_TABLE = PodcastDBHelper.DATABASE_TABLE;


    public final static String[] ALL_COLUMNS = {
            _ID, TITLE, DATE, LINK, DESCRIPTION, DOWNLOAD_LINK, URI, TIME_PAUSED};

    private static final Uri BASE_LIST_URI = Uri.parse("content://br.ufpe.cin.if710.podcast.feed/");
    //URI para tabela
    public static final Uri EPISODE_LIST_URI = Uri.withAppendedPath(BASE_LIST_URI, EPISODE_TABLE);

    // Mime type para colecao de itens
    public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/PodcastProvider.data.text";

    // Mime type para um item especifico
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/PodcastProvider.data.text";

}