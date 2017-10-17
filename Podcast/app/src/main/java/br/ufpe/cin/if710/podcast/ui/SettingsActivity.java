package br.ufpe.cin.if710.podcast.ui;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.service.JobSchedulerTime;

public class SettingsActivity extends Activity {
    public static final String FEED_LINK = "feedlink";
    public static final String CANCEL = "cancelarJob";
    public static final String AGENDAR = "agendarJob";
    public static final int JOB_ID = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class FeedPreferenceFragment extends PreferenceFragment {

        protected static final String TAG = "FeedPreferenceFragment";
        private SharedPreferences.OnSharedPreferenceChangeListener mListener;
        private Preference feedLinkPref;
        private JobScheduler jobScheduler;
        private ListPreference listPreference;

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

            //Inicializa o job
            jobScheduler = (JobScheduler) getContext().getSystemService(JOB_SCHEDULER_SERVICE);

            //Coloca o botao de cancelar
            Preference cancelar = getPreferenceManager().findPreference(CANCEL);
            cancelar.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    cancelarjobs();
                    Toast.makeText(getContext(), "Agendamento desmarcado", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            listPreference = (ListPreference)getPreferenceManager().findPreference(AGENDAR);//inicializa
            listPreference.setEntries(R.array.periods_string);
            listPreference.setEntryValues(R.array.periods_long);

            //Fazer agendamento
            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String s = newValue.toString();
                    long tempo = Long.parseLong(s);
                    agendarJob(tempo);
                    return true;
                }
            });
        }

        private void agendarJob(long tempo){
            JobInfo.Builder b = new JobInfo.Builder(JOB_ID, new ComponentName(getContext(), JobSchedulerTime.class));

            //criterio de rede
            b.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            //b.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);

            //define intervalo de periodicidade
            b.setPeriodic(tempo * 60000);

            //exige (ou nao) que esteja conectado ao carregador
            b.setRequiresCharging(false);

            //persiste (ou nao) job entre reboots
            //se colocar true, tem que solicitar permissao action_boot_completed
            b.setPersisted(false);

            //exige (ou nao) que dispositivo esteja idle
            b.setRequiresDeviceIdle(false);

            //backoff criteria (linear ou exponencial)
            //b.setBackoffCriteria(1500, JobInfo.BACKOFF_POLICY_EXPONENTIAL);

            //periodo de tempo minimo pra rodar
            //so pode ser chamado se nao definir setPeriodic...
            //b.setMinimumLatency(3000);

            //mesmo que criterios nao sejam atingidos, define um limite de tempo
            //so pode ser chamado se nao definir setPeriodic...
            //b.setOverrideDeadline(6000);

            jobScheduler.schedule(b.build());

            Toast.makeText(getContext(), "Agendado", Toast.LENGTH_SHORT).show();
        }

        private void cancelarjobs(){
            if(jobScheduler != null){
                jobScheduler.cancel(JOB_ID);
            }
        }
    }
}