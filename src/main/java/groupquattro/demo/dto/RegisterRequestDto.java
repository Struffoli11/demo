package groupquattro.demo.dto;

import groupquattro.demo.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Antonio Miele
 * @author Francesco Intonti
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    private String username;
    private String password;
    private String email;
    private Role role;
}
