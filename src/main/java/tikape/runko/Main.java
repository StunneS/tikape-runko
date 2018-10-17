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

        OpiskelijaDao opiskelijaDao = new OpiskelijaDao(database);
        
        //OMAA NYT
        KysymysDao kysymysDao = new KysymysDao(database);
        VastausDao vastausDao = new VastausDao(database);
        
        //ALKUSIVU
        
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        //KYSYMYSTEN LISTAUS
        get("/opiskelijat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kysymykset", kysymysDao.findAll());

            return new ModelAndView(map, "opiskelijat");
        }, new ThymeleafTemplateEngine());
        //KYSYMYS SIVU
        get("/opiskelijat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kys", kysymysDao.findOne(Integer.parseInt(req.params(":id"))));

            return new ModelAndView(map, "kysymys");
        }, new ThymeleafTemplateEngine());
        //POISTO
        Spark.post("/poista/:id", (req, res) -> {
            //int id = Integer.parseInt(req.queryParams("id"));
            kysymysDao.delete(Integer.parseInt(req.params(":id")));
            HashMap map = new HashMap<>();
            map.put("tehtavat", kysymysDao.findAll());
            res.redirect("/opiskelijat");
            return "";
        });
        //KYSYMYSTEN LUONTI SIVULLE
        get("/opiskelija", (req, res) -> {
            HashMap map = new HashMap<>();

            return new ModelAndView(map, "opiskelija");
        }, new ThymeleafTemplateEngine());
        //KUN KYSYMYS SAATU
        Spark.post("/opiskelija", (req, res) -> {
            String kurssi = req.queryParams("kurssi");
            String aihe = req.queryParams("aihe");
            String kysymys = req.queryParams("kysymysteksti");
            Kysymys kys = new Kysymys(kurssi,aihe,kysymys);
            kysymysDao.saveOrUpdate(kys);
            //HashMap map = new HashMap<>();
            //map.put("tehtavat", kysymysDao.findAll());
            res.redirect("/opiskelijat/" + kys.getId());
            return "";
        });
        //LISÄÄ VASTAUS
        Spark.post("/lisaa/:id", (req, res) -> {
            //int id = Integer.parseInt(req.queryParams("id"));
            Kysymys o = kysymysDao.findOne(Integer.parseInt(req.params(":id")));
            String vas = req.queryParams("vastaus");
            vastausDao.saveOrUpdate(new Vastaus(Integer.parseInt(req.params(":id")),vas));
            HashMap map = new HashMap<>();
            map.put("vastaukset", vastausDao.findAllWanted(Integer.parseInt(req.params(":id"))));
            res.redirect("/opiskelijat/"+Integer.parseInt(req.params(":id")));
            return "";
        });
    }
}
