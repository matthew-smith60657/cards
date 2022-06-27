BEGIN TRANSACTION;

DROP TABLE IF EXISTS war;
DROP TABLE IF EXISTS go_fish;
DROP TABLE IF EXISTS user_table;

CREATE TABLE war
(
        war_id serial,
        played_war int DEFAULT 0,
        won_war int DEFAULT 0,
        shortest_win int DEFAULT 0,
        longest_win int DEFAULT 0,
        
        constraint pk_war_id primary key (war_id)
);

CREATE TABLE go_fish
(
        go_fish_id serial,
        played_go_fish int DEFAULT 0,
        won_go_fish int DEFAULT 0,
        most_points_go_fish int DEFAULT 0,
        
        constraint pk_go_fish_id primary key (go_fish_id)
);

CREATE TABLE user_table
(
        user_id serial,
        name varchar(100) not null,
        password_not_secure_at_all varchar(100) not null,
        war_id int,
        go_fish_id int,
        
        constraint pk_user_id primary key (user_id),
        constraint fk_user_war foreign key (war_id) references war(war_id),
        constraint fk_user_go_fish foreign key (go_fish_id) references go_fish(go_fish_id)
);
        
ROLLBACK;
COMMIT;