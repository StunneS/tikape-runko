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
import tikape.runko.domain.Kysymys;
import tikape.runko.domain.Vastaus;

/**
 *
 * @author Sade-Tuuli
 */
public class KysymysDao implements Dao<Kysymys, Integer> {
    private Database database;

    public KysymysDao(Database database) {
        this.database = database;
    }
    @Override
    public Kysymys findOne(Integer key) throws SQLException {
        // ei toteutettu
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kysymys WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String kurssi = rs.getString("kurssi");
        String kysymys = rs.getString("kysymysteksti");
        String aihe = rs.getString("aihe");
        
        Kysymys o = new Kysymys(id,kurssi,aihe,kysymys);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    @Override
    public List<Kysymys> findAll() throws SQLException {

        // ei toteutettu
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kysymys");

        ResultSet rs = stmt.executeQuery();
        List<Kysymys> kysymykset = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String kurssi = rs.getString("kurssi");
            String kysymys = rs.getString("kysymysteksti");
            String aihe = rs.getString("aihe");

            Kysymys o = new Kysymys(id, kurssi, aihe, kysymys);
            //haetaan liittyvät vastaukset:
            List<Vastaus> vastaukset = new ArrayList<>();
            PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM Vastaus WHERE Vastaus.kysymys_id = " + id);
            ResultSet rs2 = ps2.executeQuery();
            while(rs2.next()){
                vastaukset.add(new Vastaus(rs2.getInt("id"),rs2.getString("teksti"),rs2.getBoolean("oikein")));
            }
            o.setLista(vastaukset);
            kysymykset.add(o);
            rs2.close();
            ps2.close();
        }

        rs.close();
        stmt.close();
        connection.close();

        return kysymykset;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Kysymys WHERE id = ?");
        stmt.setObject(1, key);
        stmt.executeUpdate();
        
        stmt.close();
        
        connection.close();
    }
    @Override
    public Kysymys saveOrUpdate(Kysymys a) {
        //ei tehty
        return null;
    }
}
