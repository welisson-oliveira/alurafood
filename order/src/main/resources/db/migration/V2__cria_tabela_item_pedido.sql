CREATE TABLE order_item (
  id SERIAL primary key,
  description varchar(255) DEFAULT NULL,
  quantity integer NOT NULL,
  order_id integer NOT NULL,
  CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id)
)