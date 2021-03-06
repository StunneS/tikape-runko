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
        Integer ky = rs.getInt("kysymys_id");
        Vastaus o = new Vastaus(ky,teksti,oikein);
        o.setId(id);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    @Override
    public List<Vastaus> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Vastaus");

        ResultSet rs = stmt.executeQuery();
        List<Vastaus> vastaukset = new ArrayList<>();
        while (rs.next()) {
            Integer ky= rs.getInt("kysymys_id");
            Integer id = rs.getInt("id");
            String teksti = rs.getString("teksti");
            boolean oikein = rs.getBoolean("oikein");
            Vastaus o = new Vastaus(ky, teksti, oikein);
            o.setId(id);
            vastaukset.add(o);
        }

        rs.close();
        stmt.close();
        connection.close();

        return vastaukset;
    }

    public List<Vastaus> findAllWanted(int kys) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Vastaus WHERE kysymys_id = ?");
        stmt.setObject(1, kys);

        ResultSet rs = stmt.executeQuery();
        List<Vastaus> vastaukset = new ArrayList<>();
        while (rs.next()) {
            Integer apu = rs.getInt("id");
            String teksti = rs.getString("teksti");
            boolean oikein = rs.getBoolean("oikein");
            Vastaus vas = new Vastaus(kys, teksti, oikein);
            vas.setId(apu);
            vastaukset.add(vas);
        }

        rs.close();
        stmt.close();
        connection.close();

        return vastaukset;
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Vastaus WHERE id = ?");
        stmt.setObject(1, key);
        stmt.executeUpdate();
        
        stmt.close();
        
        connection.close();
    }
    @Override
    public Vastaus saveOrUpdate(Vastaus a) throws SQLException{
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * From Vastaus WHERE teksti LIKE ?");
        stmt.setObject(1, a.getTeksti());
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
        
        PreparedStatement smt = connection.prepareStatement("INSERT INTO Vastaus (teksti, oikein, kysymys_id) VALUES (?,?,?)");
        smt.setObject(1, a.getTeksti());
        if(a.getOikein()) {
            smt.setObject(2,1);
        } else {
            smt.setObject(2,0);
        }
        smt.setObject(3, a.getKys());
        smt.executeUpdate();
        
        smt.close();
        
        PreparedStatement sm = connection.prepareStatement("SELECT id FROM Vastaus WHERE teksti LIKE ?");
        sm.setObject(1, a.getTeksti());
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
