BEGIN TRANSACTION;

DROP TABLE IF EXISTS war, go_fish, user_table CASCADE;

CREATE TABLE war
(
        war_id serial,
        played_war int DEFAULT 0,
        won_war int DEFAULT 0,
        shortest_win int DEFAULT null,
        longest_win int DEFAULT null,

        constraint pk_war_id primary key (war_id)
);

CREATE TABLE go_fish
(
        go_fish_id serial,
        played_go_fish int DEFAULT 0,
        won_go_fish int DEFAULT 0,
        most_points_go_fish int DEFAULT null,

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
INSERT INTO war (played_war, won_war, shortest_win, longest_win)
    VALUES (3, 2, 50, 250),
           (2, 0, null, null);

INSERT INTO go_fish (played_go_fish, won_go_fish, most_points_go_fish)
    VALUES (2, 1, 5),
           (3, 3, 9);

INSERT INTO user_table (name, password_not_secure_at_all, war_id, go_fish_id)
    VALUES ('dummy1', 'mypwd', 1, 1),
           ('dummy2', 'otherpwd', 2, 2);

COMMIT;