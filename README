# Spring Isolation Levels Testing

This is a test project to try out database isolation levels and concurrent write issues to convince ourselves of
- the need for `serializable` isolation, especially in read-modify-update cycles
- the `serializable` isolation guarantees are correctly handled in Spring

# How to run
```
./gradlew build
docker-compose up --build
```

# Testing database concurrency

Use following request to test adding a user concurrently with different isolation levels
```
POST http://localhost:8080/api/users/test/concurrent-writes?isolation={ISOLATION_LEVEL}
```

## Examples

### With serializable isolation level

```
POST http://localhost:8080/api/users/test/concurrent-writes?isolation=SERIALIZABLE

{
    "success": [
        {
            "id": 4,
            "name": "mark5",
            "email": "mark@poussard.io",
            "password": "1234"
        }
    ],
    "failures": [
        "User mark5 already exists.",
        "User mark5 already exists.",
        "User mark5 already exists.",
        "User mark5 already exists.",
        "User mark5 already exists.",
        "User mark5 already exists.",
        "User mark5 already exists.",
        "User mark5 already exists.",
        "User mark5 already exists.",
        "User mark5 already exists.",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into user (email,name,password) values (?,?,?)]; SQL [insert into user (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into user (email,name,password) values (?,?,?)]; SQL [insert into user (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into user (email,name,password) values (?,?,?)]; SQL [insert into user (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into user (email,name,password) values (?,?,?)]; SQL [insert into user (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into user (email,name,password) values (?,?,?)]; SQL [insert into user (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into user (email,name,password) values (?,?,?)]; SQL [insert into user (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into user (email,name,password) values (?,?,?)]; SQL [insert into user (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into user (email,name,password) values (?,?,?)]; SQL [insert into user (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into user (email,name,password) values (?,?,?)]; SQL [insert into user (email,name,password) values (?,?,?)]"
    ]
}
```

### With default isolation level

```
POST http://localhost:8080/api/users/test/concurrent-writes?isolation=DEFAULT

{
    "success": [
        {
            "id": 28,
            "name": "mark9",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 29,
            "name": "mark9",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 33,
            "name": "mark9",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 25,
            "name": "mark9",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 27,
            "name": "mark9",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 26,
            "name": "mark9",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 30,
            "name": "mark9",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 32,
            "name": "mark9",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 31,
            "name": "mark9",
            "email": "mark@poussard.io",
            "password": "1234"
        },
        {
            "id": 24,
            "name": "mark9",
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