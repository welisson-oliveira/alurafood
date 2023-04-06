CREATE TABLE orders (
  id SERIAL primary key,
  date_time timestamp NOT NULL,
  status varchar(255) NOT NULL
)