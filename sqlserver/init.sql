IF DB_ID('TEST') IS NULL
BEGIN
    CREATE DATABASE TEST;
END
GO

USE TEST;
GO

IF OBJECT_ID('dbo.customer', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.customer (
        id INT IDENTITY(1,1) NOT NULL,
        name VARCHAR(100) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
        email VARCHAR(150) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
        created_at DATETIME2 DEFAULT GETDATE() NULL,
        CONSTRAINT PK_customer PRIMARY KEY (id)
    );
END
GO

IF NOT EXISTS (SELECT 1 FROM dbo.customer)
BEGIN
    INSERT INTO dbo.customer (name, email)
    VALUES
        ('Budi', 'budi@gmail.com'),
        ('Andi', 'andi@gmail.com'),
        ('Citra', 'citra@gmail.com'),
        ('Deni', 'deni@gmail.com');
END
GO