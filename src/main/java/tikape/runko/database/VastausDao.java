/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Vastaus;

/**
 *
 * @author Sade-Tuuli
 */
public class VastausDao  implements Dao<Vastaus, Integer> {
    private Database database;

    public VastausDao(Database database) {
        this.database = database;
    }
    @Override
    public Vastaus findOne(Integer key) throws SQLException {
        // ei toteutettu
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Vastaus WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String teksti = rs.getString("teksti");
        boolean oikein = rs.getBoolean("oikein");
        
        Vastaus o = new Vastaus(id,teksti,oikein);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    @Override
    public List<Vastaus> findAll() throws SQLException {

        // ei toteutettu
        //entä oikeat vastaukset? Tai tietyn kysymyksen vastaukset?
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Vastaus");

        ResultSet rs = stmt.executeQuery();
        List<Vastaus> vastaukset = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String teksti = rs.getString("teksti");
            boolean oikein = rs.getBoolean("oikein");
            vastaukset.add(new Vastaus(id, teksti, oikein));
        }

        rs.close();
        stmt.close();
        connection.close();

        return vastaukset;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Vastaus WHERE id = ?");
        stmt.setObject(1, key);
        stmt.executeUpdate();
        
        stmt.close();
        
        connection.close();
    }
    @Override
    public Vastaus saveOrUpdate(Vastaus a) {
        //ei tehty
        return null;
    }
    
}
