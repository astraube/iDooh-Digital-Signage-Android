package br.com.i9algo.taxiadv.domain.enums;

/**
 * Created by Taxi ADV on 16/03/2016.
 */
public class Analytics {

    public enum CategoryName {
        CONTENT, // metrica para identificar que o usuario interagiu com o conteudo
        UI // metrica para identificar que o usuario interagiu com partes especificas do APP
    }

    public enum ActionsName {
        NEW_SESSION_USER,
        VISUALIZOU_PROPAGANDA,
        VISUALIZOU_CATEGORIA,
        VISUALIZOU_SIDEBAR,
        VISUALIZOU_CREDITOS,
        TOCOU_SLIDESHOW,
        TOCOU_TELA,
        TOCOU_RELOGIO,
        TOCOU_RESERVAR,
        TOCOU_COMPARTILHAR,
        TOCOU_METODO_COMPARTILHAR,
        CONFIRMOU_RESERVAR,
        CONFIRMOU_COMPARTILHAR,
        IDIOMA_BT,
        IDIOMA_TROCOU;
    }

    public enum Keys {

        slideTitle("slideTitle"),
        slideID("slideID"),
        categoryTitle("categoryTitle"),
        categoryID("categoryID"),

        sidebarName("sidebarName"),
        sidebarID("sidebarID"),
        shareMethod("shareMethod");

        private String stringValue;
        Keys(String toString) { stringValue = toString; }
        @Override
        public String toString() { return stringValue; }
    }
}
