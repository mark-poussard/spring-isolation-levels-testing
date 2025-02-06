# Spring Isolation Levels Testing

This is a test project to try out different data constraints enforcement techniques in distributed systems.

The main constraint we explore here is value uniqueness, we assume that most other complex constraints can be derived from this one.

The project tests out database isolation levels and concurrent write issues to convince ourselves of
- the need for `serializable` isolation, especially in read-modify-update cycles
- the `serializable` isolation guarantees are handled as expected in Spring

The project also explores other avenues of data uniqueness enforcement, namely through asynchronous log-based message brokers (Kafka).

Using a log-based message broker, we enforce uniqueness through judicious partitioning and bridge the async to sync gap so that the user receives feedback on their operation.

# How to run
```
./gradlew run
```

# Testing database concurrency

Use following request to test adding a user concurrently with different isolation levels
```
POST http://localhost:8080/api/users/test/concurrent-create?isolation={ISOLATION_LEVEL}&index=NO_INDEX
```

## Example : Write-skew avoided through serializable isolation

### Serializable isolation level

```
POST http://localhost:8080/api/users/test/concurrent-create?isolation=SERIALIZABLE&index=NO_INDEX

{
    "success": [
        {
            "id": 4,
            "name": "mark",
            "email": "mark@poussard.io",
            "password": "1234"
        }
    ],
    "failures": [
        "User mark already exists.",
        "User mark already exists.",
        "User mark already exists.",
        "User mark already exists.",
        "User mark already exists.",
        "User mark already exists.",
        "User mark already exists.",
        "User mark already exists.",
        "User mark already exists.",
        "User mark already exists.",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into noIdxUser (email,name,password) values (?,?,?)]; SQL [insert into noIdxUser (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into noIdxUser (email,name,password) values (?,?,?)]; SQL [insert into noIdxUser (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into noIdxUser (email,name,password) values (?,?,?)]; SQL [insert into noIdxUser (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into noIdxUser (email,name,password) values (?,?,?)]; SQL [insert into noIdxUser (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into noIdxUser (email,name,password) values (?,?,?)]; SQL [insert into noIdxUser (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into noIdxUser (email,name,password) values (?,?,?)]; SQL [insert into noIdxUser (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into noIdxUser (email,name,password) values (?,?,?)]; SQL [insert into noIdxUser (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into noIdxUser (email,name,password) values (?,?,?)]; SQL [insert into noIdxUser (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into noIdxUser (email,name,password) values (?,?,?)]; SQL [insert into noIdxUser (email,name,password) values (?,?,?)]"
    ]
}
```

### Default isolation level

```
POST http://localhost:8080/api/users/test/concurrent-create?isolation=DEFAULT&index=NO_INDEX

{
    "success": [
        {
            "id": 28,
            "name": "mark",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 29,
            "name": "mark",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 33,
            "name": "mark",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 25,
            "name": "mark",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 27,
            "name": "mark",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 26,
            "name": "mark",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 30,
            "name": "mark",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 32,
            "name": "mark",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 31,
            "name": "mark",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 24,
            "name": "mark",
            "email": "mark@poussard.io",
            "password": "1234"
        }
    ],
    "failures": [
        "Query did not return a unique result: 10 results were returned",
        "Query did not return a unique result: 10 results were returned",
        "Query did not return a unique result: 10 results were returned",
        "Query did not return a unique result: 10 results were returned",
        "Query did not return a unique result: 10 results were returned",
        "Query did not return a unique result: 10 results were returned",
        "Query did not return a unique result: 10 results were returned",
        "Query did not return a unique result: 10 results were returned",
        "Query did not return a unique result: 10 results were returned",
        "Query did not return a unique result: 10 results were returned"
    ]
}
```

## Example : High contention on legal data inserts with serializable isolation

### Serializable isolation level

```
POST http://localhost:8080/api/users/test/neighbour-create?isolation=SERIALIZABLE&index=NO_INDEX

{
    "success": [
        {
            "id": 291,
            "name": "mark5",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 318,
            "name": "mark34",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 328,
            "name": "mark39",
            "email": "mark@poussard.io",
            "password": "1234"
        }
    ],
    "failures": [
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into noIdxUser (email,name,password) values (?,?,?)]; SQL [insert into noIdxUser (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into noIdxUser (email,name,password) values (?,?,?)]; SQL [insert into noIdxUser (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into noIdxUser (email,name,password) values (?,?,?)]; SQL [insert into noIdxUser (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into noIdxUser (email,name,password) values (?,?,?)]; SQL [insert into noIdxUser (email,name,password) values (?,?,?)]",
        ...
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into noIdxUser (email,name,password) values (?,?,?)]; SQL [insert into noIdxUser (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into noIdxUser (email,name,password) values (?,?,?)]; SQL [insert into noIdxUser (email,name,password) values (?,?,?)]"
    ]
}
```

### Default isolation level

```
POST http://localhost:8080/api/users/test/neighbour-create?isolation=DEFAULT&index=NO_INDEX

{
    "success": [
        {
            "id": 248,
            "name": "mark5",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 249,
            "name": "mark7",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 244,
            "name": "mark1",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 245,
            "name": "mark6",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 250,
            "name": "mark9",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        ...
        {
            "id": 288,
            "name": "mark42",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 290,
            "name": "mark46",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 287,
            "name": "mark47",
            "email": "mark@poussard.io",
            "password": "1234"
        }
    ],
    "failures": []
}
```

# Testing uniqueness enforcement through a log-based broker

In our log-based broker testing, the isolation level is always DEFAULT and there is no index on the table.

```
POST http://localhost:8080/api/users/msg-based/test/concurrent-create
Headers
    - Idempotency-Key : {UNIQUE_ID}
```

```
POST http://localhost:8080/api/users/msg-based/test/neighbour-create
Headers
    - Idempotency-Key : {UNIQUE_ID}
```

# Cheatsheet

Force recreate
```
docker-compose up --force-recreate
```

List docker process
```
sudo docker ps -a
```

Remove docker process
```
sudo docker rm $PROCESS_ID
```

Connect to docker database
```
docker exec -it mysql_container mysql -uroot -proot mydb
```