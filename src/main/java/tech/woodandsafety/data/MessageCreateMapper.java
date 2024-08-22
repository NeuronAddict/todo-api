package tech.woodandsafety.data;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Singleton;
import tech.woodandsafety.dto.MessageCreateDTO;
import tech.woodandsafety.dto.MessageDisplayDTO;
import tech.woodandsafety.mapper.CreateMapper;

@Singleton
public class MessageCreateMapper implements CreateMapper<Message, MessageDisplayDTO, MessageCreateDTO> {

    @Override
    public Uni<Message> toEntity(MessageCreateDTO messageCreateDTO) {
        return CustomUser.findByName(messageCreateDTO.author())
                .onItem()
                .ifNull()
                .failWith(() -> new IllegalArgumentException(String.format("User %s not found", messageCreateDTO.author())))
                .map(customUser -> new Message(messageCreateDTO.message(), customUser, messageCreateDTO.dueDate()));
    }

    @Override
    public Uni<Message> updateWithDTO(Message message, MessageCreateDTO messageCreateDTO) {
        message.message = messageCreateDTO.message();
        message.dueDate = messageCreateDTO.dueDate();
        return Uni.createFrom().item(message);
    }

    @Override
    public MessageDisplayDTO toDisplayDTO(Message message) {
        return new MessageDisplayDTO(message.id, message.getMessage(), message.getAuthor().getName(), message.getDueDate());
    }

}
