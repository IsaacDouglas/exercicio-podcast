package br.ufpe.cin.if710.podcast.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import android.content.Intent;
import android.widget.Toast;

import br.ufpe.cin.if710.podcast.R;

public class EpisodeDetailActivity extends Activity {

    public static final String DOWNLOAD_COMPLETE = "br.ufpe.cin.if710.podcast.action.DOWNLOAD_COMPLETE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);

        //TODO preencher com informações do episódio clicado na lista...



        //Recupera o Item do intent
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        ItemFeed itemFeed = (ItemFeed)params.get("Item");

        //Coloca os textViews na activity
        TextView title = (TextView) findViewById(R.id.title_label);
        TextView itemtitle = (TextView) findViewById(R.id.item_title_label);

        TextView date = (TextView) findViewById(R.id.pubDate_label);
        TextView itemdate = (TextView) findViewById(R.id.item_pubDate_label);

        TextView link = (TextView) findViewById(R.id.link_label);
        TextView itemlink = (TextView) findViewById(R.id.item_link_label);

        TextView downloadLink = (TextView) findViewById(R.id.downloadLink_label);
        TextView itemDownloadLink = (TextView) findViewById(R.id.item_downloadLink_label);

        TextView description = (TextView) findViewById(R.id.description_label);
        TextView itemDescription = (TextView) findViewById(R.id.item_description_label);

        TextView uri = (TextView) findViewById(R.id.uri_label);
        TextView itemUri = (TextView) findViewById(R.id.item_uri_label);

        //preenche os textViews com as informacoes do item recuperado
        itemtitle.setText(itemFeed.getTitle());
        itemdate.setText(itemFeed.getPubDate());
        itemlink.setText(itemFeed.getLink());
        itemDescription.setText(itemFeed.getDescription());
        itemDownloadLink.setText(itemFeed.getDownloadLink());
        itemUri.setText(itemFeed.getUri());
    }


}
