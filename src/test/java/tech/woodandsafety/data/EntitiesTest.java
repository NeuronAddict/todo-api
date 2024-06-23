package tech.woodandsafety.data;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.TestReactiveTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.UniAsserter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

@QuarkusTest
public class EntitiesTest {

    @Test
    @TestReactiveTransaction
    public void testMessage(UniAsserter uniAsserter) {

        CustomUser user = new CustomUser("alice", "secret", "user");

        Message message = new Message("hello", user, LocalDate.of(2024,12,12));

        uniAsserter.execute(() -> Panache.withTransaction(user::persist));
        uniAsserter.execute(() -> Panache.withTransaction(message::persist));

        uniAsserter.execute(() -> Message.<Message>findAll().list().onItem().invoke(messages1 -> System.out.println(messages1)));
        uniAsserter.execute(() -> CustomUser.<CustomUser>findAll().list().onItem().invoke(users -> System.out.println(users)));

        uniAsserter.assertEquals(() -> Message.<Message>listAll().map(List::size), 1);
        uniAsserter.assertEquals(() -> Message.findAll().firstResult(), message);

    }

    @Test
    @TestReactiveTransaction
    public void testCustomUser(UniAsserter uniAsserter) {

        CustomUser user = new CustomUser("alice", "secret", "user");

        uniAsserter.execute(() -> Panache.withTransaction(user::persist));

        uniAsserter.execute(() -> Message.<Message>findAll().list().onItem().invoke(messages1 -> System.out.println(messages1)));
        uniAsserter.execute(() -> CustomUser.<CustomUser>findAll().list().onItem().invoke(users -> System.out.println(users)));

        uniAsserter.assertEquals(() -> CustomUser.<CustomUser>listAll().map(List::size), 1);
        uniAsserter.assertEquals(() -> CustomUser.findByName("alice"), user);

    }

    @Test
    @TestReactiveTransaction
    public void testUpdateAuthor(UniAsserter uniAsserter) {

        CustomUser user1 = new CustomUser("alice", "secret", "admin");
        CustomUser user2 = new CustomUser("bob", "secret", "user");

        Message message = new Message("hello", user1, LocalDate.of(2024,12,12));

        uniAsserter.execute(() -> Panache.withTransaction(user1::persist));
        uniAsserter.execute(() -> Panache.withTransaction(user2::persist));

        uniAsserter.execute(() -> Panache.withSession(message::persist));

        uniAsserter.assertEquals(() -> message.updateAuthor("bob").map(Message::getAuthor), user2);

        uniAsserter.assertEquals(() -> Message.<Message>findById(message.id).map(message1 -> message1.author), user2);

    }

}
