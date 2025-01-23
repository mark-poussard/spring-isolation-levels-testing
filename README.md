# Spring Isolation Levels Testing

This is a test project to try out database isolation levels and concurrent write issues to convince ourselves of
- the need for `serializable` isolation, especially in read-modify-update cycles
- the `serializable` isolation guarantees are handled as expected in Spring

# How to run
```
./gradlew build
docker-compose up --build
```

# Testing database concurrency

Use following request to test adding a user concurrently with different isolation levels
```
POST http://localhost:8080/api/users/test/concurrent-create?isolation={ISOLATION_LEVEL}
```

## Example : Write-skew avoided through serializable isolation

### Serializable isolation level

```
POST http://localhost:8080/api/users/test/concurrent-create?isolation=SERIALIZABLE

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

### Default isolation level

```
POST http://localhost:8080/api/users/test/concurrent-create?isolation=DEFAULT

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
POST http://localhost:8080/api/users/test/neighbour-create?isolation=SERIALIZABLE

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
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into user (email,name,password) values (?,?,?)]; SQL [insert into user (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into user (email,name,password) values (?,?,?)]; SQL [insert into user (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into user (email,name,password) values (?,?,?)]; SQL [insert into user (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into user (email,name,password) values (?,?,?)]; SQL [insert into user (email,name,password) values (?,?,?)]",
        ...
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into user (email,name,password) values (?,?,?)]; SQL [insert into user (email,name,password) values (?,?,?)]",
        "could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [insert into user (email,name,password) values (?,?,?)]; SQL [insert into user (email,name,password) values (?,?,?)]"
    ]
}
```

### Default isolation level

```
POST http://localhost:8080/api/users/test/neighbour-create?isolation=DEFAULT

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