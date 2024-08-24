create sequence CustomUser_SEQ start with 1 increment by 50;
create sequence LogEntry_SEQ start with 1 increment by 50;
create sequence Message_SEQ start with 1 increment by 50;
create table CustomUser (id bigint not null, hashedPassword varchar(255), name varchar(255) unique, roles varchar(255), primary key (id));
create table LogEntry (id bigint not null, initiator_id bigint, message_id bigint, type varchar(255) check (type in ('delete','add','done','undone')), primary key (id));
create table Message (dueDate date, author_id bigint, id bigint not null, message varchar(255), primary key (id));
alter table if exists LogEntry add constraint FKdogt0amcganvcuivnf09daiqh foreign key (initiator_id) references CustomUser;
alter table if exists LogEntry add constraint FKgjtasff5o9xdc7a4cf9rlbw0o foreign key (message_id) references Message;
alter table if exists Message add constraint FKhyc6bdjqqk00tp0w6myb3ogq5 foreign key (author_id) references CustomUser;

