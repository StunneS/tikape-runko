/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sade-Tuuli
 */
public class Kysymys {
    private Integer id;
    private String kurssi;
    private String aihe;
    private String kysymysteksti;
    private List<Vastaus> lista;

    public Kysymys( String kurssi,String aihe, String kysymysteksti) {
        this.kurssi = kurssi;
        this.aihe = aihe;
        this.kysymysteksti = kysymysteksti;
        this.lista = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKurssi() {
        return kurssi;
    }

    public void setKurssi(String nimi) {
        this.kurssi = kurssi;
    }
    public String getAihe() {
        return aihe;
    }

    public void setAihe(String nimi) {
        this.kurssi = kurssi;
    }
    public String getKysymysteksti(){
    return kysymysteksti;
    }
    public void setKysymysteksti(String kysymysteksti) {
        this.kysymysteksti = kysymysteksti;
    }
    public List<Vastaus> getLista(){
    return lista;
    }
    public void setLista(List<Vastaus> vastaukset) {
        this.lista = vastaukset;
    }
}
