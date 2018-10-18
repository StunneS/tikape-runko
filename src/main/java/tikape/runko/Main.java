package tikape.runko;

import java.io.File;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.KysymysDao;
import tikape.runko.database.OpiskelijaDao;
import tikape.runko.database.VastausDao;
import tikape.runko.domain.Kysymys;
import tikape.runko.domain.Vastaus;

public class Main {

    public static void main(String[] args) throws Exception {
        // asetetaan portti jos heroku antaa PORT-ympäristömuuttujan
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        File tied = new File("db","tietokanta.db");
        Database database = new Database("jdbc:sqlite:" + tied.getAbsolutePath());
        database.init();
        
        //OMAA NYT
        KysymysDao kysymysDao = new KysymysDao(database);
        VastausDao vastausDao = new VastausDao(database);
        
        //ALKUSIVU
        
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        //KYSYMYSTEN LISTAUS
        get("/kysymykset", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kysymykset", kysymysDao.findAll());

            return new ModelAndView(map, "opiskelijat");
        }, new ThymeleafTemplateEngine());
        //KYSYMYS SIVU
        get("/kysymykset/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kys", kysymysDao.findOne(Integer.parseInt(req.params(":id"))));

            return new ModelAndView(map, "kysymys");
        }, new ThymeleafTemplateEngine());
        //POISTO KYS
        Spark.post("/poista/:id", (req, res) -> {
            kysymysDao.delete(Integer.parseInt(req.params(":id")));
            
            res.redirect("/kysymykset");
            return "";
        });
        //KYSYMYSTEN LUONTI SIVULLE
        get("/uusi", (req, res) -> {
            HashMap map = new HashMap<>();

            return new ModelAndView(map, "opiskelija");
        }, new ThymeleafTemplateEngine());
        //KUN KYSYMYS SAATU
        Spark.post("/uusi", (req, res) -> {
            String kurssi = req.queryParams("kurssi");
            String aihe = req.queryParams("aihe");
            String kysymys = req.queryParams("kysymysteksti");
            if(kurssi.equals("") || aihe.equals("") || kysymys.equals("")) {
                //res.redirect("/uusi");
                //return "";
                HashMap map = new HashMap<>();
                map.put("error", "Varmista ettei mikään tekstikentistä ole tyhjä.");

            return new ModelAndView(map, "opiskelija");
                
            }
            Kysymys kys = new Kysymys(kurssi,aihe,kysymys);
            kysymysDao.saveOrUpdate(kys);
 
            //res.redirect("/kysymykset/" + kys.getId());
            
            HashMap map = new HashMap<>();
            map.put("kys", kysymysDao.findOne(kys.getId()));

            return new ModelAndView(map, "kysymys");
            //return "";
        }, new ThymeleafTemplateEngine());
        
        //LISÄÄ VASTAUS
        Spark.post("/lisaa/:id", (req, res) -> {
            String vas = req.queryParams("vastaus");
            boolean oik = false;
            if(req.queryParams("tosi") != null) {
                oik = true;
            }
            vastausDao.saveOrUpdate(new Vastaus(Integer.parseInt(req.params(":id")),vas,oik));
            
            res.redirect("/kysymykset/"+Integer.parseInt(req.params(":id")));
            return "";
        });
        //POISTA VASTAUS
        Spark.post("/poistav/:id", (req, res) -> {
            Vastaus u = vastausDao.findOne(Integer.parseInt(req.params(":id")));
            vastausDao.delete(Integer.parseInt(req.params(":id")));
            
            res.redirect("/kysymykset/" + u.getKys());
            return "";
        });
    }
}
