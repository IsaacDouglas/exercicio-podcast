package br.ufpe.cin.if710.podcast.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import java.io.IOException;

import br.ufpe.cin.if710.podcast.domain.ItemFeed;

/**
 * Created by isaacdouglas1 on 08/10/17.
 */

public class PlayPauseEpisodeService extends Service {

    private final String TAG = "MusicPlayerNoBindingService";
    public static final String EPISODE_PAUSE = "br.ufpe.cin.if710.podcast.action.EPISODE_PAUSE";
    public static final String EPISODE_OVER = "br.ufpe.cin.if710.podcast.action.EPISODE_OVER";

    private MediaPlayer mPlayer;
    private int mStartID;
    ItemFeed itemFeed;

    @Override
    public void onCreate() {
        super.onCreate();

        // configurar media player
        //Nine Inch Nails Ghosts I-IV is licensed under a Creative Commons Attribution Non-Commercial Share Alike license.
        mPlayer = new MediaPlayer();

        if (null != mPlayer) {
            //nao deixa entrar em loop
            mPlayer.setLooping(false);

            // encerrar o service quando terminar a musica
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {

                    //criando um intent para enviar quando a musica acabar
                    Intent musicOver = new Intent(EPISODE_OVER);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Item", itemFeed);
                    musicOver.putExtras(bundle);
                    getApplicationContext().sendBroadcast(musicOver);

                    // encerra se foi iniciado com o mesmo ID
                    stopSelf(mStartID);
                }
            });
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != mPlayer) {
            // ID para o comando de start especifico
            mStartID = startId;

            Bundle params = intent.getExtras();
            itemFeed = (ItemFeed)params.get("Item"); //recupera o item do feed

            /**/
            //se ja esta tocando...
            if (mPlayer.isPlaying()) {
                int timePaused = mPlayer.getCurrentPosition();
                itemFeed.setTimePaused(timePaused); //guardar no item o tempo para salvar no banco posteriormente

                mPlayer.reset();

                //criando um intent para enviar quando clicar pausa
                Intent musicPause = new Intent(EPISODE_PAUSE);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Item", itemFeed);
                musicPause.putExtras(bundle);
                getApplicationContext().sendBroadcast(musicPause);
            }
            else {
                // inicia musica
                try {
                    mPlayer.setDataSource(this, Uri.parse(itemFeed.getUri()));
                    mPlayer.prepare();

//                    mPlayer.seekTo(mPlayer.getDuration() - 2000);// para fins de teste
                    mPlayer.seekTo(itemFeed.getTimePaused()); // ir para o tempo que foi armazenado no item
                    mPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // nao reinicia service automaticamente se for eliminado
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mPlayer) {
            mPlayer.stop();
            mPlayer.release();
        }
    }

    //nao eh possivel fazer binding com este service
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
        //return null;
    }
}
