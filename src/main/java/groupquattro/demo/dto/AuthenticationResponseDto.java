package groupquattro.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AuthenticationResponseDto {

    @JsonProperty("username")
    private String username;
    @JsonProperty("access_token")
    private String accessToken;
//    @JsonProperty("refresh_token")
//    private String refreshToken;
}
