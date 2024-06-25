package tech.woodandsafety.data;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tech.woodandsafety.dto.LogEntryCreateDTO;
import tech.woodandsafety.dto.MessageCreateDTO;
import tech.woodandsafety.dto.MessageDisplayDTO;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@QuarkusTest
class CreateMapperTest {

    MessageCreateMapper messageCreateMapper;
    LogEntryCreateMapper logEntryCreateMapper;

    @BeforeEach
    void setUp() {
        messageCreateMapper = new MessageCreateMapper();
        logEntryCreateMapper = new LogEntryCreateMapper(messageCreateMapper);
    }

    @Test
    @RunOnVertxContext
    public void should_create_message_entity(UniAsserter asserter) {

        asserter.execute(() -> PanacheMock.mock(CustomUser.class));
        asserter.execute(() -> Mockito.when(CustomUser.findByName("alice")).thenReturn(Uni.createFrom()
                .item(new CustomUser("alice", "", "admin"))));

        asserter.execute(() -> {
            LocalDate dueDate = LocalDate.of(2024, 12, 12);

            Message item = messageCreateMapper.toEntity(new MessageCreateDTO("message", "alice", dueDate))
                    .subscribe().withSubscriber(UniAssertSubscriber.create()).awaitItem().assertCompleted().getItem();

            assertThat(item).extracting(Message::getMessage).isEqualTo("message");
            assertThat(item).extracting(Message::getAuthor).extracting(CustomUser::getName).isEqualTo("alice");
            assertThat(item).extracting(Message::getDueDate).isEqualTo(dueDate);
        });
    }

    @Test
    public void should_convert_message_entity() {
        LocalDate dueDate = LocalDate.of(2024, 12, 12);
        Message message = new Message("hello",
                new CustomUser("alice", "", "admin"),
                dueDate);
        message.id = 12L;

        var assertion = assertThat(messageCreateMapper.toDisplayDTO(message));
        assertion.extracting(MessageDisplayDTO::id).isEqualTo(12L);
        assertion.extracting(MessageDisplayDTO::author).isEqualTo("alice");
        assertion.extracting(MessageDisplayDTO::message).isEqualTo("hello");
        assertion.extracting(MessageDisplayDTO::dueDate).isEqualTo(dueDate);

    }

    @Test
    @RunOnVertxContext
    public void should_create_logEntry_entity(UniAsserter asserter) {
        LocalDate dueDate = LocalDate.of(2024, 12, 12);

        asserter.execute(() -> PanacheMock.mock(CustomUser.class));
        asserter.execute(() -> PanacheMock.mock(Message.class));

        CustomUser user = new CustomUser("alice", "", "admin");

        asserter.execute(() -> Mockito.when(CustomUser.findByName("alice")).thenReturn(Uni.createFrom()
                .item(user)));
        asserter.execute(() -> Mockito.when(Message.findById(1L)).thenReturn(Uni.createFrom()
                .item(new Message("hello", user, dueDate)).map(message -> {
                    message.id = 1L;
                    return message;
                })));


        asserter.execute(() -> {


            LogEntry item = logEntryCreateMapper.toEntity(
                            new LogEntryCreateDTO(LogEntryType.delete, new MessageDisplayDTO(1L, "message", "alice", dueDate), "alice"))
                    .subscribe().withSubscriber(UniAssertSubscriber.create()).awaitItem().assertCompleted().getItem();

            assertThat(item).extracting(LogEntry::getType).isEqualTo(LogEntryType.delete);
            assertThat(item).extracting(LogEntry::getMessage).extracting(Message::getMessage).isEqualTo("hello");
            assertThat(item).extracting(LogEntry::getMessage).extracting(message -> message.id).isEqualTo(1L);
            assertThat(item).extracting(LogEntry::getInitiator).extracting(CustomUser::getName).isEqualTo("alice");
        });
    }

    @Test
    public void should_convert_logEntry_entity() {
        LocalDate dueDate = LocalDate.of(2024, 12, 12);
        CustomUser user = new CustomUser("alice", "", "admin");
        Message message = new Message("hello",
                user,
                dueDate);
        message.id = 12L;

        LogEntry logEntry = new LogEntry(LogEntryType.add, message, user);

        var assertion = assertThat(logEntryCreateMapper.toDisplayDTO(logEntry));
        assertion.extracting(LogEntryDisplayDTO::logEntryType).isEqualTo(LogEntryType.add);
        assertion.extracting(LogEntryDisplayDTO::message).extracting(MessageDisplayDTO::message).isEqualTo("hello");
        assertion.extracting(LogEntryDisplayDTO::initiator).isEqualTo("alice");

    }

}
