package groupquattro.demo.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequestMapping("/coinfly")
public class CoinFlyAPI {

    @GetMapping("/home")
    @ResponseStatus(HttpStatus.OK)
    public String index(){
        return "/index";
    }
    @ResponseStatus(HttpStatus.OK)
    public String formLogin(){
        return "/login";
    }


    @ResponseStatus(HttpStatus.OK)
    public String formRegistration(){
        return "/registration";
    }

    @RequestMapping("/logout-success")
    public String logoutPage() {
        return "index";
    }

}
