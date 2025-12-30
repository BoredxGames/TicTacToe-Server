create table Player
(
    id       varchar(36) primary key,
    username varchar(50)  not null unique,
    password varchar(100) not null,
    score    int default 0

);


create table Room
(
    id               varchar(36) primary key,
    first_player_id  varchar(36),
    second_player_id varchar(36),
    winner_id        varchar(36),
    status           int,
    created_at       timestamp default current_timestamp,

    constraint fk_room_first_player foreign key (first_player_id)
        references player (id),

    constraint fk_room_second_player foreign key (second_player_id)
        references player (id),

    constraint fk_room_winner foreign key (winner_id)
        references player (id)
);

create table Player_Room
(
    player_id varchar(36),
    room_id   varchar(36),

    primary key (player_id, room_id),

    constraint fk_player_room_player foreign key (player_id)
        references player (id),

    constraint fk_player_room_room foreign key (room_id)
        references room (id)
);


create table Activity
(
    player_id  varchar(36),
    start_time timestamp,
    end_time   timestamp,
    constraint pk_activity primary key (player_id, start_time),

    constraint fk_activity_player foreign key (player_id)
        references player (id)
);
