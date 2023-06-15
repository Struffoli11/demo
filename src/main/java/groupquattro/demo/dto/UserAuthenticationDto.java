package groupquattro.demo.dto;

import groupquattro.demo.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthenticationDto {

    private String username;
    private String password;
    private Role role;
}
