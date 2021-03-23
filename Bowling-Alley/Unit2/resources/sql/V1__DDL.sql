create table bowler
(
    nick   varchar(50),
    "full" varchar(50),
    email  varchar(50)
);

create table reference
(
    key   varchar(100),
    entry varchar(200)
);

create table score
(
    nick  varchar(50),
    date  varchar(50),
    score varchar(50)
);

create table user
(
    id       varchar(200),
    password varchar(200),
    name     varchar(500),
    userType varchar(100)
);