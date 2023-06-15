package groupquattro.demo.token;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "blacklist")
@NoArgsConstructor
@AllArgsConstructor
public class Token {
	@Id
	public String id;
	
  public String jwt;

  public Date timeToLive;

}

