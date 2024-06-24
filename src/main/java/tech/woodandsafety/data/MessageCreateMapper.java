package tech.woodandsafety.data;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Singleton;
import tech.woodandsafety.dto.MessageDTO;
import tech.woodandsafety.mapper.CreateMapper;

@Singleton
public class MessageCreateMapper implements CreateMapper<Message, MessageDTO, MessageDTO> {

    @Override
    public Uni<Message> toEntity(MessageDTO messageDTO) {
        return CustomUser.findByName(messageDTO.author()).map(customUser -> new Message(messageDTO.message(), customUser, messageDTO.dueDate()));
    }

    @Override
    public Uni<Message> updateWithDTO(Message message, MessageDTO messageDTO) {
        message.message = messageDTO.message();
        message.dueDate = messageDTO.dueDate();
        return message.updateAuthor(messageDTO.author());
    }

    @Override
    public MessageDTO toDisplayDTO(Message message) {
        return new MessageDTO(message.getMessage(), message.getAuthor().getName(), message.getDueDate());
    }

}
