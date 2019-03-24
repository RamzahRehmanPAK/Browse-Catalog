create schema OnlineStoreCurren Black Dial Leather Strap Watch

CREATE TABLE OnlineStore.Category(
	id int ,
   title varchar(30) not null,
   parentCategory int foreign key references OnlineStore.Category(id) null
   PRIMARY KEY(id)
);
go

CREATE TABLE OnlineStore.Item(

code varchar (30),
title varchar (60)  not null,
description varchar(100) null,
price int not null,
available int not null,
category int   foreign key references OnlineStore.Category(id) not null,
 PRIMARY KEY(code)
 )
 go

 insert into OnlineStore.Item values
 (1,'Qum Silk Shirt','Generation Off-White Crepe Fusion Qum Silk Collection Tunic for Women',3000,5,7),
 (2,'Bracelet Watch','JR Sports Leather Analog Bracelet Watch For Women - Red & Golden',1000,8,8),
 (3,'Cotton Shirt','STALLION Grey Cotton Shirt For Men, all sizes available',4000,3,9),
 (4,'Leather Watch','Curren Black Dial Leather Strap Watch',4000,3,10)
