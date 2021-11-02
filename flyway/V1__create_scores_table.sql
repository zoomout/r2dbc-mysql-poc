create table if not exists scores (
  id            serial primary key,
  name          varchar(255),
  score        int,
  box           bigint
);
