package tech.woodandsafety;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import org.junit.jupiter.api.Test;
import tech.woodandsafety.data.CustomUser;


@QuarkusTest
class MessageTest {

    @Test
    @RunOnVertxContext
    public void testEntities(UniAsserter asserter) {
        asserter.execute(() -> {
            CustomUser customUser = new CustomUser("user1", "secret", "user");
            return customUser.persist();
        });

        asserter.assertEquals(CustomUser::count, 1L);

//        Message message = new Message("hello", customUser, LocalDate.of(2024,1,1));
//        asserter.execute(() -> message.persist());
//
//        LogEntry logEntry = new LogEntry(LogEntryType.add, message, customUser);
//        asserter.execute(() -> logEntry.persist());
//
//        asserter.assertEquals(LogEntry::count, 1L);
//        asserter.assertSame(() -> CustomUser.<CustomUser>findAll().firstResult(), customUser);

//        asserter.assertSame(() -> Message.findAll().firstResult(), message);
//
//        asserter.assertSame(() -> LogEntry.findAll().firstResult(), logEntry);
//
    }

}