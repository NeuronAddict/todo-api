package tech.woodandsafety;

import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


@QuarkusTest
class MessageTest {

    @Test
    @RunOnVertxContext
    public void testEntities(TransactionalUniAsserter asserter) {
        CustomUser customUser = new CustomUser("user1", "secret", "user");
        asserter.execute(() -> customUser.persist());

        Message message = new Message("hello", customUser, LocalDate.of(2024,1,1));
        asserter.execute(() -> message.persist());

        LogEntry logEntry = new LogEntry(LogEntryType.add, message, customUser);
        asserter.execute(() -> logEntry.persist());

        asserter.assertEquals(LogEntry::count, 1L);
//        asserter.assertSame(() -> CustomUser.<CustomUser>findAll().firstResult(), customUser);

//        asserter.assertSame(() -> Message.findAll().firstResult(), message);
//
//        asserter.assertSame(() -> LogEntry.findAll().firstResult(), logEntry);
//
    }

}