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

    public Vastaus(Integer id, String teksti, Boolean oikein) {
        this.id = id;
        this.teksti = teksti;
        this.oikein = oikein;
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeksti() {
        return teksti;
    }

    public void setTeksti(String nimi) {
        this.teksti = teksti;
    }
    public boolean getOikein() {
        return oikein;
    }

    public void setOikein(boolean oikein) {
        this.oikein = oikein;
    }
}