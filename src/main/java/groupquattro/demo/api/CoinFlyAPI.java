package groupquattro.demo.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequestMapping("/api/v1/coinfly")
public class CoinFlyAPI {

    @GetMapping("/home")
    public ResponseEntity<?> index(){
        return ResponseEntity.ok("/homepage.html");
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
