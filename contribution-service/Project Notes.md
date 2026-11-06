

## Creating DB And User
```shell
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

```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
```shell
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

## Expanding and Looking into JAR
```shell
Expand-Archive -LiteralPath ".\contribution-service-0.0.1-SNAPSHOT.jar" -DestinationPath ".\extracted"
Copy-Item ".\contribution-service-0.0.1-SNAPSHOT.jar" ".\temp.zip"
Expand-Archive -LiteralPath ".\temp.zip" -DestinationPath ".\extracted\"
```

## LOGGING: 
```properties
# --- DEBUGGING LIQUIBASE ISSUE -------------------------------------------------

logging.level.liquibase=DEBUG
logging.level.liquibase.changelog=DEBUG
logging.level.liquibase.database=DEBUG
```

## DEPENDENCY DETAILS
```shell
.\gradle dependencyInsight --dependency liquibase-core --configuration runtimeClasspath
.\gradlew.bat clean build --refresh-dependencies 
```

> The dependencies were not complete: 
```gradle
//	implementation 'org.liquibase:liquibase-core'
	implementation 'org.springframework.boot:spring-boot-starter-liquibase'
```

```sql 
drop table if exists public.contribution_entries cascade;
```

---
### OVERALL PROJECT REQUIREMENTS: 
I'd follow this implementation order:
1. Contribution Service<br>
   Complete the ledger, APIs, database, and tests.
<br>
2. Participant Service<br>
   Build participant and employer management, then integrate it with the Contribution Service.
<br>
3. Payroll Import Service<br>
   Introduce CSV processing, file storage, and RabbitMQ.
<br>
4. Reconciliation Service<br>
   Implement batch comparisons and discrepancy detection.
<br>
5. Notification Service<br>
   Introduce asynchronous notifications and event handling.
<br>
6. Spring MVC Web Application<br>
   Build the user-facing application on top of the completed APIs.
<br>
---

