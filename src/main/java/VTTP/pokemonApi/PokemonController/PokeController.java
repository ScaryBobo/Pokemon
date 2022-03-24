package VTTP.pokemonApi.PokemonController;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Controller
@RequestMapping(path="/")
public class PokeController {

    @GetMapping(path="/search")
    public String getPokemon(@RequestParam(name="pokemonName") String pokemonName, Model model){
        String url = "https://pokeapi.co/api/v2/pokemon/" + pokemonName;

        RequestEntity<Void> req = RequestEntity
        .get(url)
        .accept(MediaType.APPLICATION_JSON)
        .build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);
        // System.out.println(resp.getBody());

        JsonObject data= null;

        try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())){
            JsonReader reader = Json.createReader(is);
            data = reader.readObject();
            

        } catch (Exception e) {
            
        }
            JsonObject sprites = data.getJsonObject("sprites");
            JsonObject versions = sprites.getJsonObject("versions");
            JsonObject generation = versions.getJsonObject("generation-iii");
            JsonObject emerald = generation.getJsonObject("emerald");

            Set<String> keySet = emerald.keySet();
            List<String> pics = new ArrayList<>();

            System.out.println(keySet); // returns [front_default, front_shiny]
            for (String k : keySet){
                pics.add(emerald.getString(k));
            }    
            System.out.println(pics.toString()); //returns [img url 1, img url 2]
        model.addAttribute("pictures", pics);   

        return "searchResult";
    }
}
