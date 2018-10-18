/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

/**
 *
 * @author Sade-Tuuli
 */
public class Vastaus {
    private String teksti;
    private Integer id;
    private boolean oikein;
    private Integer kys;

    public Vastaus(Integer kys, String teksti, Boolean oikein) {
        this.kys = kys;
        this.teksti = teksti;
        this.oikein = oikein;
        
    }

    public Integer getKys() {
        return kys;
    }
    public void setKys(int kys) {
        this.kys = kys;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeksti() {
        return teksti;
    }

    public void setTeksti(String nimi) {
        this.teksti = nimi;
    }
    public boolean getOikein() {
        return oikein;
    }

    public void setOikein(boolean oikein) {
        this.oikein = oikein;
    }
}
