package br.ufpe.cin.if710.podcast.ui;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.service.JobSchedulerTime;

public class SettingsActivity extends Activity {
    public static final String FEED_LINK = "feedlink";
    public static final String PERIODO = "periodo";
    public static final String CANCEL = "cancel";
    public static final int _IDJOB = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class FeedPreferenceFragment extends PreferenceFragment {

        protected static final String TAG = "FeedPreferenceFragment";
        private SharedPreferences.OnSharedPreferenceChangeListener mListener;
        private Preference feedLinkPref;

        private SharedPreferences.OnSharedPreferenceChangeListener pListener;
        private Preference periodoPref;
        private JobScheduler jobScheduler;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // carrega preferences de um recurso XML em /res/xml
            addPreferencesFromResource(R.xml.preferences);

            // pega o valor atual de FeedLink
            feedLinkPref = (Preference) getPreferenceManager().findPreference(FEED_LINK);

            // cria listener para atualizar summary ao modificar link do feed
            mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    feedLinkPref.setSummary(sharedPreferences.getString(FEED_LINK, getActivity().getResources().getString(R.string.feed_link)));
                }
            };

            // pega objeto SharedPreferences gerenciado pelo PreferenceManager deste fragmento
            SharedPreferences prefs = getPreferenceManager().getSharedPreferences();

            // registra o listener no objeto SharedPreferences
            prefs.registerOnSharedPreferenceChangeListener(mListener);

            // força chamada ao metodo de callback para exibir link atual
            mListener.onSharedPreferenceChanged(prefs, FEED_LINK);


            //__________________________________________________________________________________________

            periodoPref = (Preference) getPreferenceManager().findPreference(PERIODO);

            pListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    long tempo = 10;
                    String s = sharedPreferences.getString(PERIODO, "10");

                    try {
                        tempo = Long.parseLong(s);

                        if(tempo > 0){
                            periodoPref.setSummary(sharedPreferences.getString(PERIODO, "10"));
                            job(tempo);
                        }else{
                            tempo = 10;
                            periodoPref.setSummary("10");
                            Toast.makeText(getContext(), "Digite um número positivo", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        periodoPref.setSummary("10");
                        Toast.makeText(getContext(), "Digite um número", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            // registra o listener no objeto SharedPreferences
            prefs.registerOnSharedPreferenceChangeListener(pListener);

            // força chamada ao metodo de callback para exibir link atual
            pListener.onSharedPreferenceChanged(prefs, PERIODO);

            //Inicializa o job
            jobScheduler = (JobScheduler) getContext().getSystemService(JOB_SCHEDULER_SERVICE);

            //Coloca o botao de cancelar
            Preference button = getPreferenceManager().findPreference(CANCEL);
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if(jobScheduler != null){
                        jobScheduler.cancel(_IDJOB);
                    }
                    Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

        private void job(long tempo){
            JobInfo.Builder jobInfo = new JobInfo.Builder(_IDJOB, new ComponentName(getContext(), JobSchedulerTime.class));
            PersistableBundle bundle = new PersistableBundle();

            //setando a internet
            jobInfo.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

            //Setando os atributos do job
            long time = tempo*1000;
            jobInfo.setPeriodic(time);
            jobInfo.setRequiresCharging(false);
            jobInfo.setPersisted(false);
            jobInfo.setRequiresDeviceIdle(false);
            jobScheduler.schedule(jobInfo.build());

            Toast.makeText(getContext(), "Agendado", Toast.LENGTH_SHORT).show();
        }

    }
}