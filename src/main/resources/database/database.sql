
create table Player (
    id varchar(36) primary key,
    username varchar(50) not null unique,
    password varchar(100) not null,
    score int default 0 -- start with 0 scoree by default
);

create table Activity (
    id varchar(36) primary key,
    player_id varchar(36),
    start_time timestamp,
    end_time timestamp,
    constraint fk_activity_player foreign key (player_id)
        references player(id)
);
