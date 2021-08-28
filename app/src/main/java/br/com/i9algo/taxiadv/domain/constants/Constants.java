package br.com.i9algo.taxiadv.domain.constants;

import java.util.concurrent.TimeUnit;

public class Constants {

	// LOCATION API Constants
	public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
	public static final int REQUEST_CHECK_SETTINGS = 0x1;
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000 * 60; // 1 minuto.
	public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2; // Every 30 seconds
	public static final long MAX_WAIT_TIME = UPDATE_INTERVAL_IN_MILLISECONDS * 6; // Every 5 minutes.


	public static final String MIXPANEL_PROJECT_TOKEN = "cad90548d64a43cb58b44e20393f5f3b";// Mixpanel project Token
	public static final String MIXPANEL_PROJECT_API_TOKEN = "0dff7e27bf4b7dc2fe36dc9493c0d858"; // Mixpanel project API Token

	//public static final String GEOSPARK_PUBLICH_KEY = "1fd006e6692e523520116af569c6da142d9cd0071602b58b716c33598b63ed91";

	// segment.com
	public static final String SEGMENT_KEY = "IBk4fS6wnAPZvCNGve34Hdk3xZFSISfp";


	// SERVER
	public static final String SERVER_HOST		= "https://taxiadv.com.br/";
	public static final String SERVER_ENDPOINT_API_V3 = SERVER_HOST + "api/v3/";

	// TODO - metodo de grupo utilizado temporariamente
	public static final String GROUP = "PR";
	public static final String GROUP_ID = "1";


	// TIMERS
	public static final long INTERVAL_RESTORE_MAIN_VIEW = TimeUnit.SECONDS.toMillis(30); // Tempo para restaurar tela principal na posicao inicial BAIXADO PARA 30SEGUNDOS DIA 14/06
	public static final long INTERVAL_START_APP = TimeUnit.SECONDS.toMillis(15); // Tempo para startar app
	//public static final long INTERVAL_HIDE_MAIN_NAVIGATION = TimeUnit.MINUTES.toMillis(1); // Tempo para esconder o menu de navegacao principal
	public static final long INTERVAL_HIDE_DIALOG = TimeUnit.MINUTES.toMillis(1); // Tempo para fechar Caixas de dialogo
	public static final long INTERVAL_MIN_SEND_INFO = TimeUnit.MINUTES.toMillis(5); // Intervalo minimo para atualizar as informacoes no banco de dados
	public static final long INTERVAL_RESTART_DEVICE = TimeUnit.HOURS.toMillis(4); // Tempo para reiniciar device

	//Download status variables
	public static final int DOWNLOAD_STARTING = 0;
	public static final int DOWNLOAD_STARTED = 1;
	public static final int DOWNLOAD_COMPLETE = 2;
	public static final int DOWNLOAD_ERROR = 3;

	// ACCOUNT MANAGER
	/*
	public static final String ACCOUNT_TYPE = "br.com.i9algo.taxiadv";
	public static final String ACCOUNT_TOKEN_TYPE = "provider";

	public static final String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
	public static final String ARG_AUTH_TYPE = "AUTH_TYPE";
	public static final String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
	*/

	public static final int SLIDESHOW_TYPE_ADVERT = 0;
	public static final int SLIDESHOW_TYPE_FILLER = 1;
}
