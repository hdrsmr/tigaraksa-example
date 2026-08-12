DELETE FROM customer;

WITH numbers AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1
    FROM numbers
    WHERE n < 10000
)
INSERT INTO customer (name, email, created_at)
SELECT CONCAT('nama_', n),
       CONCAT('nama_', n, '@gmail.com'),
       GETDATE()
FROM numbers
OPTION (MAXRECURSION 0);
