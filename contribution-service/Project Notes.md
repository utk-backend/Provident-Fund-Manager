

## Creating DB And User
``` 
PS C:\Users\utk60> psql -U postgres
Password for user postgres:

postgres=# CREATE USER pfmanager WITH PASSWORD 'pfmanager@123!';
CREATE ROLE
postgres=# CREATE DATABASE pfmanager;
CREATE DATABASE
postgres=# GRANT ALL PRIVILEGES ON DATABASE pfmanager TO pfmanager;
GRANT

postgres=# \c pfmanager
You are now connected to database "pfmanager" as user "postgres".

pfmanager=# GRANT ALL ON SCHEMA public TO pfmanager
pfmanager-# ;
GRANT
pfmanager=# ALTER SCHEMA public OWNER TO pfmanager;
ALTER SCHEMA
pfmanager=# \l
                                                             List of databases
   Name    |  Owner   | Encoding | Locale Provider |      Collate       |       Ctype        | Locale | ICU Rules |   Access privileges
-----------+----------+----------+-----------------+--------------------+--------------------+--------+-----------+------------------------
 pfmanager | postgres | UTF8     | libc            | English_India.1252 | English_India.1252 |        |           | =Tc/postgres          +
           |          |          |                 |                    |                    |        |           | postgres=CTc/postgres +
           |          |          |                 |                    |                    |        |           | pfmanager=CTc/postgres
 postgres  | postgres | UTF8     | libc            | English_India.1252 | English_India.1252 |        |           |
 template0 | postgres | UTF8     | libc            | English_India.1252 | English_India.1252 |        |           | =c/postgres           +
           |          |          |                 |                    |                    |        |           | postgres=CTc/postgres
 template1 | postgres | UTF8     | libc            | English_India.1252 | English_India.1252 |        |           | =c/postgres           +
           |          |          |                 |                    |                    |        |           | postgres=CTc/postgres
(4 rows)


pfmanager=# \du
                             List of roles
 Role name |                         Attributes
-----------+------------------------------------------------------------
 pfmanager |
 postgres  | Superuser, Create role, Create DB, Replication, Bypass RLS

```

## Hibernate SQL Queries

```
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
```
Hibernate: 
    create table contribution_entries (
        transaction_id uuid not null,
        amount numeric(19,2) not null,
        created_by varchar(255),
        currency varchar(3) not null,
        effective_date date not null,
        entry_type varchar(20) not null check ((entry_type in ('CONTRIBUTION','REVERSAL'))),
        note varchar(500),
        recorded_at timestamp(6) with time zone not null,
        reference varchar(100),
        reverses_transaction_id uuid,
        participant_id varchar(50) not null,
        primary key (transaction_id)
    )
Hibernate: 
    create table idempotency_records (
        idempotency_key varchar(255) not null,
        created_at timestamp(6) with time zone not null,
        request_hash varchar(64) not null,
        transaction_id uuid not null,
        primary key (idempotency_key)
    )
Hibernate: 
    create table participants (
        participant_id varchar(50) not null,
        created_at timestamp(6) with time zone not null,
        name varchar(255) not null,
        status varchar(20) not null check ((status in ('ACTIVE','INACTIVE'))),
        primary key (participant_id)
    )
Hibernate: 
    alter table if exists contribution_entries 
       add constraint FK2lt6nqpng2u99r2j29ivfrmhf 
       foreign key (participant_id) 
       references participants
```