package groupquattro.demo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static groupquattro.demo.model.Role.USER;

@Document(collection = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class User implements UserDetails {
    @Id
    private String id;
    @NonNull
    private String username;
    @NonNull
    private String password;
    @Field(name = "emailAddress")
    private String email;
    @DocumentReference(collection = "groups")
    private List<Group> groups = new ArrayList<>();

    @DocumentReference(collection = "debts")
    private List<Debt> debts = new ArrayList<>();

    private List<String> keys = new ArrayList<>();

    private Role role=USER;



    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean isUserAMember(String groupName){
        for(Group g : groups){
            if(g.getGroupName().equals(groupName)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
