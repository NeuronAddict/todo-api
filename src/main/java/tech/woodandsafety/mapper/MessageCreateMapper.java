package tech.woodandsafety.mapper;

import jakarta.inject.Singleton;
import tech.woodandsafety.data.CustomUser;
import tech.woodandsafety.data.Message;
import tech.woodandsafety.dto.MessageDTO;

import java.time.Duration;

@Singleton
public class MessageCreateMapper implements CreateMapper<Message, MessageDTO, MessageDTO> {



    @Override
    public Message toEntity(MessageDTO messageDTO) {
        return new Message(messageDTO.message(), CustomUser.findByName(messageDTO.author()).await().atMost(Duration.ofSeconds(3)), messageDTO.dueDate());
    }

    @Override
    public MessageDTO toDisplayDTO(Message message) {
        return new MessageDTO(message.getMessage(), message.getAuthor().getName(), message.getDueDate());
    }

}
