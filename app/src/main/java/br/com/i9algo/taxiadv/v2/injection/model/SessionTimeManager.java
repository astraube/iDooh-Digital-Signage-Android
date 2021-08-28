package br.com.i9algo.taxiadv.v2.injection.model;

import java.util.concurrent.TimeUnit;

import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.injection.provides.Session;
import br.com.i9algo.taxiadv.v2.storage.firebase.RemoteConfigs;
import br.com.i9algo.taxiadv.v2.utils.TimeUtils;
import br.com.i9algo.taxiadv.v2.utils.time.Stopwatch;

public class SessionTimeManager {

    private final String LOG_TAG = getClass().getSimpleName();

    private Session mSession;

    // timer watch MINIMO para identificar novos passageiros
    // é resetado toda vez que tem uma interacao na tela
    // se passado o tempo de 'TIME_MIN_NEW_SESSION_MILLIS' sem nenhuma interacao, cria uma nova sessao
    // nova sessao por tempo de INATIVIDADE
    private long TIME_INTERACTION_NEW_SESSION_MILLIS;
    private Stopwatch watchInteractionNewSession;

    // timer watch MAXIMO para identificar novos passageiros
    // cada vez que finaliza esse timer, cria uma nova sessao
    // nova sessao por TEMPO MEDIO DE NOVOS PASSAGEIROS
    private long TIME_MAX_NEW_SESSION_MILLIS;
    public Stopwatch maxWatchNewSession;

    public SessionTimeManager(Session session) {
        this.mSession = session;
        this.TIME_INTERACTION_NEW_SESSION_MILLIS = TimeUnit.MINUTES.toMillis(RemoteConfigs.getTimeNewSessionInteraction());
        this.TIME_MAX_NEW_SESSION_MILLIS = TimeUnit.MINUTES.toMillis(RemoteConfigs.getTimeNewSessionNormal());
        initialize();
    }

    private void initialize() {
        this.watchInteractionNewSession = new Stopwatch(false);
        this.maxWatchNewSession = new Stopwatch(false);

        Logger.d(LOG_TAG, "-----> Tempo para nova sessao por INTERACAO: " + TimeUtils.getDurationBreakdown(TIME_INTERACTION_NEW_SESSION_MILLIS));
        Logger.d(LOG_TAG, "-----> Tempo para nova sessao por MEDIA DE CORRIDA: " + TimeUtils.getDurationBreakdown(TIME_MAX_NEW_SESSION_MILLIS));

        this.newSession();
    }

    public void newSession() {
        this.mSession.newSession();
        this.watchInteractionNewSession.reset();
        this.maxWatchNewSession.reset();
    }

    public void onUserInteraction() {
        // NAO MEXER NESSA LOGICA PORAAAA!!!!!!

        long maxSessionWatch = this.maxWatchNewSession.getElapsedTimeLong();
        //Logger.d(LOG_TAG, "-----> maxSessionWatch: " + TimeUnit.MILLISECONDS.toMinutes(maxSessionWatch) + " min");

        long timeWatchInteraction = this.watchInteractionNewSession.getElapsedTimeLong();
        //Logger.d(LOG_TAG, "-----> timeWatchInteraction: " + TimeUnit.MILLISECONDS.toMinutes(timeWatchInteraction) + " min");

        if (maxSessionWatch == 0 || maxSessionWatch >= getMaxWatchNewSession()) {
            // NOVA SESSAO por tempo maximo
            this.newSession();
            Logger.v(LOG_TAG, "-----> NOVA SESSAO por tempo maximo");
            return;
        } else if (timeWatchInteraction >= getTimeWatchInteraction()) {
            // NOVA SESSAO por tempo de inatividade
            this.newSession();
            Logger.v(LOG_TAG, "-----> NOVA SESSAO por tempo de inatividade");
            return;
        }
        // Reinicia o timer de interacao
        this.watchInteractionNewSession.reset();
    }

    public long getTimeWatchInteraction(){ return this.TIME_INTERACTION_NEW_SESSION_MILLIS; }
    public void setTimeWatchInteraction(long millis){ this.TIME_INTERACTION_NEW_SESSION_MILLIS = millis; }

    public long getMaxWatchNewSession(){ return this.TIME_MAX_NEW_SESSION_MILLIS; }
    public void setMaxWatchNewSession(long millis){ this.TIME_MAX_NEW_SESSION_MILLIS = millis; }
}
