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
            rs.close();
            stmt.close();
            connection.close();
            return null;
        }

        Integer id = rs.getInt("id");
        String kurssi = rs.getString("kurssi");
        String kysymys = rs.getString("kysymysteksti");
        String aihe = rs.getString("aihe");
        
        Kysymys o = new Kysymys(kurssi,aihe,kysymys);
        o.setId(id);

        rs.close();
        stmt.close();
        
        List<Vastaus> vastaukset = new ArrayList<>();
            PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM Vastaus WHERE Vastaus.kysymys_id = " + id);
            ResultSet rs2 = ps2.executeQuery();
            while(rs2.next()){
                Vastaus v = new Vastaus(rs2.getInt("kysymys_id"),rs2.getString("teksti"),rs2.getBoolean("oikein"));
                v.setId(rs2.getInt("id"));
                vastaukset.add(v);
            }
            o.setLista(vastaukset);
            
            rs2.close();
            ps2.close();
        
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

            Kysymys o = new Kysymys(kurssi, aihe, kysymys);
            o.setId(id);
            //haetaan liittyvät vastaukset:
            List<Vastaus> vastaukset = new ArrayList<>();
            PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM Vastaus WHERE Vastaus.kysymys_id = " + id);
            ResultSet rs2 = ps2.executeQuery();
            while(rs2.next()){
                Vastaus v = new Vastaus(rs2.getInt("kysymys_id"),rs2.getString("teksti"),rs2.getBoolean("oikein"));
                v.setId(rs2.getInt("id"));
                vastaukset.add(v);
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
        PreparedStatement st = connection.prepareStatement("DELETE FROM Vastaus WHERE kysymys_id = ?");
        st.setObject(1, key);
        st.executeUpdate();
        st.close();
        
        connection.close();
    }
    @Override
    public Kysymys saveOrUpdate(Kysymys a) throws SQLException{
        //ei tehty
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * From Kysymys WHERE kysymysteksti LIKE ?");
        stmt.setObject(1, a.getKysymysteksti());
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (hasOne) {
            rs.close();
            stmt.close();
            connection.close();
            return null;
        }
        rs.close();
        stmt.close();
        
        PreparedStatement smt = connection.prepareStatement("INSERT INTO Kysymys (kurssi, aihe, kysymysteksti) VALUES (?,?,?)");
        smt.setObject(1, a.getKurssi());
        smt.setObject(2, a.getAihe());
        smt.setObject(3, a.getKysymysteksti());
        smt.executeUpdate();
        
        smt.close();
        
        PreparedStatement sm = connection.prepareStatement("SELECT id FROM Kysymys WHERE kysymysteksti LIKE ?");
        sm.setObject(1, a.getKysymysteksti());
        ResultSet rrs = sm.executeQuery();
        if(rrs.next()) {
            a.setId(rrs.getInt(1));
        }
        rrs.close();
        sm.close();
        connection.close();
        return a;
    }
}
