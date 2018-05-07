# --- !Ups

create table "category" (
  "id" integer not null primary key autoincrement,
  "name" varchar not null
);

create table "product" (
  "id" integer not null primary key autoincrement,
  "name" varchar not null,
  "description" text not null,
  category int not null,
  foreign key(category) references category(id)
);

create table "basket" (
  "id" integer not null primary key autoincrement,
  product int not null,
  "amount" integer not null,
  foreign key(product) references product(id)
);

create table "order" (
  "id" integer not null primary key autoincrement,
  "date" varchar not null,
  "value" double not null,
  basket int not null,
  foreign key(Basket) references Basket(id)
};

create table "review" (
  "id" integer not null primary key autoincrement,
  product int not null,
  "description" text not null,
  foreign key(product) references product(id)
);

# --- !Downs

drop table "product" if exists;
drop table "category" if exists;
drop table "basket" if exists;
drop table "order" if exists;
