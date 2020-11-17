
delete from addresses;
delete from users;
delete from order_items;
delete from orders;
delete from employees;

insert into users(id, name, email,disabled) values(1,'morgan','morgan@gmail.com', 0);
insert into users(id, name, email,disabled) values(2,'janet','janet@gmail.com', 0);
insert into users(id, name, email,disabled) values(3,'joe','joe1@gmail.com', 1);

insert into addresses(id,city,user_id) values(1, 'Toronto',1);
insert into addresses(id,city,user_id) values(2, 'Vaughan',1);
insert into addresses(id,city,user_id) values(3, 'Markham',2);


insert into orders(id, cust_name, cust_email) values(1,'morgan','morgan@gmail.com');
insert into orders(id, cust_name, cust_email) values(2,'janet','janet@gmail.com');
insert into orders(id, cust_name, cust_email) values(3,'joe','joe@gmail.com');

insert into order_items(id, product_code,quantity,order_id) values(1,'P001', 2, 1);
insert into order_items(id, product_code,quantity,order_id) values(2,'P002', 3, 1);
insert into order_items(id, product_code,quantity,order_id) values(3,'P003', 1, 2);

SET IDENTITY_INSERT [dbo].[employees]  ON;

INSERT INTO employees(employeeid,address,birthdate,city,country
           ,[extension],[firstname],[hiredate],[homephone],[lastname]
           ,[notes],[photo],[photopath],[postalcode],[region]
           ,[reportsto] ,[title],[titleofcourtesy])
     VALUES(1, '100 Kenedey St' ,'1980-01-16' ,'Toronto' ,'Canada'
           ,'1234' , 'Mike' ,'2010-02-19','416-123-4567','Test'
           ,null ,null ,null ,'M2N 0K1' ,'GTA'
           ,null ,'VP' ,'Mr');

INSERT INTO employees(employeeid,address,birthdate,city,country
           ,[extension],[firstname],[hiredate],[homephone],[lastname]
           ,[notes],[photo],[photopath],[postalcode],[region]
           ,[reportsto] ,[title],[titleofcourtesy])
     VALUES(2, '200 Kenedey St' ,'1978-05-16' ,'Markham' ,'Canada'
           ,'2341' , 'Maria' , null,'416-123-1234','Test'
           ,null ,null ,null ,'M2J 1B2' ,'GTA'
           ,null ,'Sales Rep' ,'Ms');

SET IDENTITY_INSERT [dbo].[employees]  OFF;

-- Reseed tables
--DECLARE @newID int
--SET @newID = (SELECT max(employeeid) FROM employees)
--IF @newID IS NULL  SET @newID=0
--DBCC CHECKIDENT ('employees', RESEED, @newID);
