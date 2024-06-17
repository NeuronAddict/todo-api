package tech.woodandsafety;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Collection;

@Entity
@UserDefinition
public class CustomUser extends PanacheEntity {

    @Username
    public String name;

    @Password
    public String password;

    @Roles
    public String roles;

//    @OneToMany
//    public Collection<Message> messages;

}
