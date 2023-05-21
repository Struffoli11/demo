package groupquattro.demo.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.HTML;


@RestController()
@RequestMapping("/coinfly")
public class CoinFlyAPI {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getHome(){
        return "welcome";
    }

}
