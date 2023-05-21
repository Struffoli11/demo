package groupquattro.demo.utils;

import groupquattro.demo.model.Expence;
import groupquattro.demo.model.RomanaExpence;
import groupquattro.demo.services.RomanaExpenceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class Dictionary {
    private static Map<String, RomanaExpenceService> dict = new TreeMap<String, RomanaExpenceService>();
    @Autowired
    private RomanaExpenceService res;

    public Dictionary(){
        dict.put("Affitto", res);
        dict.put("Supermercato", res);
        dict.put("Pranzo", res);
        dict.put("Regalo", res);
        dict.put("Noleggio", res);
        dict.put("Gita", res);
        dict.put("Viaggio", res);
        dict.put("Vacanza", res);
    }
}
