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

        CustomUser user = new CustomUser("alice2", "secret", "user");

        Message message = new Message("hello", user, LocalDate.of(2024,12,12));

        uniAsserter.execute(() -> Panache.withTransaction(user::persist));
        uniAsserter.execute(() -> Panache.withTransaction(message::persist));

        uniAsserter.execute(() -> Message.<Message>findAll().list().onItem().invoke(messages1 -> System.out.println(messages1)));
        uniAsserter.execute(() -> CustomUser.<CustomUser>findAll().list().onItem().invoke(users -> System.out.println(users)));

        uniAsserter.assertEquals(() -> Message.findByAuthor("alice2").map(messages -> messages.get(0)), message);

        uniAsserter.assertEquals(() -> Message.findByAuthor("bob2").map(List::size), 0);
        uniAsserter.assertEquals(() -> Message.findByAuthor("alice2").map(List::size), 1);
        uniAsserter.assertEquals(() -> Message.findByAuthor("alice2").map(messages -> messages.get(0)).map(message1 -> message1.author), user);
    }

    @Test
    @TestReactiveTransaction
    public void testCustomUser(UniAsserter uniAsserter) {


        CustomUser user = new CustomUser("alice2", "secret", "user");

        uniAsserter.execute(() -> Panache.withTransaction(user::persist));

        uniAsserter.execute(() -> Message.<Message>findAll().list().onItem().invoke(messages1 -> System.out.println(messages1)));
        uniAsserter.execute(() -> CustomUser.<CustomUser>findAll().list().onItem().invoke(users -> System.out.println(users)));

        uniAsserter.assertEquals(() -> CustomUser.findByName("alice2"), user);

    }

    @Test
    @TestReactiveTransaction
    public void testUpdateAuthor(UniAsserter uniAsserter) {

        CustomUser user1 = new CustomUser("alice2", "secret", "admin");
        CustomUser user2 = new CustomUser("bob2", "secret", "user");

        Message message = new Message("hello", user1, LocalDate.of(2024,12,12));

        uniAsserter.execute(() -> Panache.withTransaction(user1::persist));
        uniAsserter.execute(() -> Panache.withTransaction(user2::persist));

        uniAsserter.execute(() -> Panache.withSession(message::persist));

        uniAsserter.assertEquals(() -> message.updateAuthor("bob2").map(Message::getAuthor), user2);

        uniAsserter.assertEquals(() -> Message.<Message>findById(message.id).map(message1 -> message1.author), user2);

    }

    @Test
    @TestReactiveTransaction
    public void testLogEntry(UniAsserter uniAsserter) {

        CustomUser user1 = new CustomUser("alice2", "secret", "admin");
        CustomUser user2 = new CustomUser("bob2", "secret", "user");

        Message message = new Message("hello", user1, LocalDate.of(2024, 12, 12));

        LogEntry logEntry = new LogEntry(LogEntryType.delete, message, user2);

        uniAsserter.execute(() -> Panache.withTransaction(user1::persist));
        uniAsserter.execute(() -> Panache.withTransaction(user2::persist));

        uniAsserter.execute(() -> Panache.withTransaction(message::persist));
        uniAsserter.execute(() -> Panache.withTransaction(logEntry::persist));

        uniAsserter.assertEquals(() -> LogEntry.findByInitiator("alice2").map(List::size), 0);
        uniAsserter.assertEquals(() -> LogEntry.findByInitiator("bob2").map(List::size), 1);

        uniAsserter.assertEquals(() -> LogEntry.findByInitiator("bob2").map(logEntries -> logEntries.get(0)).map(logEntry1 -> logEntry1.message), message);
    }

}
