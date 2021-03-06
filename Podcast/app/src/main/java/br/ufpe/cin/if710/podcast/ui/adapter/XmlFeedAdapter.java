package br.ufpe.cin.if710.podcast.ui.adapter;

import java.util.List;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.service.DownloadEpisodeService;
import br.ufpe.cin.if710.podcast.service.PlayPauseEpisodeService;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;

import android.widget.Button;
import android.widget.Toast;

public class XmlFeedAdapter extends ArrayAdapter<ItemFeed> {

    int linkResource;
    public static final String PERMISSIONS_STORAGE = "br.ufpe.cin.if710.podcast.action.PERMISSIONS_STORAGE";

    public XmlFeedAdapter(Context context, int resource, List<ItemFeed> objects) {
        super(context, resource, objects);
        linkResource = resource;
    }

    /**
     * public abstract View getView (int position, View convertView, ViewGroup parent)
     * <p>
     * Added in API level 1
     * Get a View that displays the data at the specified position in the data set. You can either create a View manually or inflate it from an XML layout file. When the View is inflated, the parent View (GridView, ListView...) will apply default layout parameters unless you use inflate(int, android.view.ViewGroup, boolean) to specify a root view and to prevent attachment to the root.
     * <p>
     * Parameters
     * position	The position of the item within the adapter's data set of the item whose view we want.
     * convertView	The old view to reuse, if possible. Note: You should check that this view is non-null and of an appropriate type before using. If it is not possible to convert this view to display the correct data, this method can create a new view. Heterogeneous lists can specify their number of view types, so that this View is always of the right type (see getViewTypeCount() and getItemViewType(int)).
     * parent	The parent that this view will eventually be attached to
     * Returns
     * A View corresponding to the data at the specified position.
     */


	/*
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.itemlista, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.item_title);
		textView.setText(items.get(position).getTitle());
	    return rowView;
	}
	/**/

    //http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
    static class ViewHolder {
        TextView item_title;
        TextView item_date;
        Button button;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), linkResource, null);
            holder = new ViewHolder();
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.item_date = (TextView) convertView.findViewById(R.id.item_date);
            holder.button = (Button) convertView.findViewById(R.id.item_action);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_title.setText(getItem(position).getTitle());
        holder.item_date.setText(getItem(position).getPubDate());


        final String uri = getItem(position).getUri();
        //verifica se tem url para setar o nome do botao corretamente
        if(uri.length()!=0){
            holder.button.setEnabled(true);
            holder.button.setText("Play");
        }else{
            holder.button.setText("Download");
        }

        holder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(uri.length()==0){
                    if (!podeEscrever()) {
                        //prepara o intent para passar o broadcast de salvar na memoria
                        Intent storage = new Intent(PERMISSIONS_STORAGE);
                        getContext().sendBroadcast(storage);
                    }else {
                        holder.button.setEnabled(false); //desabilitar o botao de download

                        Toast.makeText(getContext(), "Iniciando o download", Toast.LENGTH_SHORT).show();
                        holder.button.setText("Baixando");
                        //cria um intent para iniciar o service e passa o item clicado
                        Intent downloadService = new Intent(getContext(), DownloadEpisodeService.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Item", getItem(position));
                        downloadService.putExtras(bundle);
                        getContext().startService(downloadService);
                    }
                }else{
                    //troca de play para pause e vice versa
                    if(holder.button.getText().equals("Play")){
                        holder.button.setText("Pause");
                    }else{
                        holder.button.setText("Play");
                    }

                    //inicia o service para tocar a musica ou pausar passando o item clicado
                    Intent playMusicService = new Intent(getContext(), PlayPauseEpisodeService.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Item", getItem(position));
                    playMusicService.putExtras(bundle);
                    getContext().startService(playMusicService);
                }
            }
        });

        return convertView;
    }

    public boolean podeEscrever() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}