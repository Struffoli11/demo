package groupquattro.demo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    //Permessi di prova, ovviamente andranno implementati secondo le esigenze
    GROUP_MEMBER_ADD_NEW_MEMBER("group Memeber:add new memeber in group"),
    GROUP_MEMBER_ACCESS_EXPENCE("group Memeber:access to expence options")
    ;

    @Getter
    private final String permission;
}
