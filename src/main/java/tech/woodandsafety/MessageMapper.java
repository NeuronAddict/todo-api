package tech.woodandsafety;

import jakarta.inject.Singleton;

@Singleton
public class MessageMapper implements Mapper<MessageDTO, Message> {

    @Override
    public Message toEntity(MessageDTO messageDTO) {
        Message message = new Message();
        message.message = messageDTO.message();
        message.author = messageDTO.author();
        message.dueDate = messageDTO.dueDate();
        return message;
    }
}
