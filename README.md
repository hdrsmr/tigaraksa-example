masuk ke tigaraksa-example:

docker compose up -d

setelah service Up semua:

buat Database:

"CREATE DATABASE TEST;"

"USE TEST;"

CREATE TABLE dbo.customer 
( id INT IDENTITY(1,1) NOT NULL, name VARCHAR(100) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL, email VARCHAR(150) COLLATE SQL_Latin1_General_CP1_CI_AS NULL, created_at DATETIME2 DEFAULT GETDATE() NULL, CONSTRAINT PK_customer PRIMARY KEY (id) );


Run ---> mvn spring-boot:run

http://localhost:8383/swagger-ui/index.html
