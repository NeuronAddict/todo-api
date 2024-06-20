package tech.woodandsafety.mapper;

import jakarta.inject.Singleton;
import tech.woodandsafety.data.Message;
import tech.woodandsafety.dto.MessageDTO;

@Singleton
public class MessageMapper implements Mapper<MessageDTO, Message> {

    @Override
    public Message toEntity(MessageDTO messageDTO) {
        return new Message(messageDTO.message(), messageDTO.author(), messageDTO.dueDate());
    }

    @Override
    public MessageDTO toDTO(Message message) {
        return new MessageDTO(message.getMessage(), message.getAuthor(), message.getDueDate());
    }

}
