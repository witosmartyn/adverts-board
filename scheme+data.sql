create table category (id bigint not null auto_increment, name varchar(255), primary key (id)) ENGINE=InnoDB;
create table city (id bigint not null auto_increment, name varchar(255), primary key (id)) ENGINE=InnoDB;
create table images (id bigint not null auto_increment, name varchar(255), pic longblob, size bigint not null, type varchar(255), item_id bigint not null, primary key (id)) ENGINE=InnoDB;
create table items (id bigint not null auto_increment, avatar_id varchar(255), created datetime, description varchar(2047), name varchar(50), phone varchar(255), price double precision not null, updated datetime, version bigint, category_id bigint, city_id bigint, user_id bigint not null, primary key (id)) ENGINE=InnoDB;
create table permission (id bigint not null auto_increment, name varchar(255), primary key (id)) ENGINE=InnoDB;
create table role (id bigint not null auto_increment, name varchar(255), primary key (id)) ENGINE=InnoDB;
create table roles_permissions (role_id bigint not null, pemission_id bigint not null) ENGINE=InnoDB;
create table users (id bigint not null auto_increment, name varchar(255), account_non_expired bit not null, account_non_locked bit not null, credentials_non_expired bit not null, enabled bit not null, password varchar(64) not null, username varchar(64) not null, primary key (id)) ENGINE=InnoDB;
create table users_roles (user_id bigint not null, role_id bigint not null, primary key (user_id, role_id)) ENGINE=InnoDB;
alter table users add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);
alter table images add constraint FK5yu3ybwgjqqj4s78xyfbr01yu foreign key (item_id) references items (id);
alter table items add constraint FKmwj262911sqtm7lw9yhmf125 foreign key (category_id) references category (id);
alter table items add constraint FKc6d4vhuegt76s9ml7un0b38ie foreign key (city_id) references city (id);
alter table items add constraint FKft8pmhndq1kntvyfaqcybhxvx foreign key (user_id) references users (id);


INSERT INTO `role` (`id`, `name`)
VALUES
  (1,'ADMIN'),
  (2,'ACTUATOR'),
  (3,'USER');

INSERT INTO `permission` (`id`, `name`)
VALUES
  (1,'READ_PERMISSION'),
  (2,'WRITE_PERMISSION'),
  (3,'EDIT_PERMISSION'),
  (4,'CHANGE_PASSWORD_PERMISSION');


INSERT INTO `category` (`id`, `name`)
VALUES
  (1,'ANIMALS'),
  (2,'CHILD'),
  (3,'CARS'),
  (4,'HOUSES'),
  (5,'ELECTRONICS'),
  (6,'EQUIPMENT'),
  (7,'FOODS'),
  (8,'SPORT'),
  (9,'OTHERS'),
  (10,'CLOTHES');



INSERT INTO `city` (`id`, `name`)
VALUES
  (1,'KYIV'),
  (2,'DNIPROPETROVSK'),
  (3,'CHERNIHIV'),
  (4,'KHARKIV'),
  (5,'ZHYTOMYR'),
  (6,'POLTAVA'),
  (7,'KHERSON'),
  (8,'ZAPORIZHIA'),
  (9,'LUHANSK'),
  (10,'DONETSK'),
  (11,'VINNYTSIA'),
  (12,'MYKOLAIV'),
  (13,'KIROVOHRAD'),
  (14,'SUMY'),
  (15,'LVIV'),
  (16,'CHERKASY'),
  (17,'KHMELNYTSKYI'),
  (18,'VOLYN'),
  (19,'RIVNE'),
  (20,'IVANO_FRANKIVSK'),
  (21,'TERNOPIL'),
  (22,'ZAKARPATTIA'),
  (23,'CHERNIVTSI');
