# Oracle Database用
```oracle

```


# PostgreSQL用
```postgresql
create table user_t (
  user_id integer not null constraint user_t_user_id_pk primary key,
  name varchar(30)
);

create table product_t (
  product_no integer not null constraint product_t_product_no_pk primary key,
  name varchar(30)
);

create table stock_t (
  product_no integer not null constraint stock_t_pkey primary key,
  quantity integer
);

create table order_t (
  order_id varchar(64) not null constraint order_t_pkey primary key,
  product_no integer,
  customer integer,
  quantity integer
);
```