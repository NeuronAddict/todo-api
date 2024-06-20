package tech.woodandsafety.data;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
@UserDefinition
public class CustomUser extends PanacheEntity {

    @Column(unique = true)
    @Username
    String name;

    @Password
    String hashedPassword;

    @Roles
    String roles;

    public CustomUser(String name, String hashedPassword, String roles) {
        this.name = name;
        this.hashedPassword = BcryptUtil.bcryptHash(hashedPassword);
        this.roles = roles;
    }

    protected CustomUser() {}

    public String getName() {
        return name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getRoles() {
        return roles;
    }

    //    @OneToMany
//    public Collection<Message> messages;

    public static Uni<CustomUser> findByName(String name) {
        return CustomUser.<CustomUser>find("name", name).firstResult();
    }
}
