# springboot-redis-playground

## setup

- run redis container

```shell
docker compose up -d
```

## run local

- access [http://localhost:8083/swagger-ui/](http://localhost:8083/swagger-ui/)
  - you can test api using Swagger UI

- check date persisted

```shell
$ docker compose exec redis bash -c redis-cli
> keys *
1) "USER"

> type USER
hash

> hgetall USER
1) "1"
2) "{\"@class\":\"com.kiyotakeshi.model.User\",\"id\":1,\"firstName\":\"yamada\",\"lastName\":\"taro\",\"email\":\"string\",\"age\":20}"
3) "4"
4) "{\"@class\":\"com.kiyotakeshi.model.User\",\"id\":4,\"firstName\":\"tanaka\",\"lastName\":\"jiro\",\"email\":\"string\",\"age\":15}"
5) "3"
6) "{\"@class\":\"com.kiyotakeshi.model.User\",\"id\":3,\"firstName\":\"takahashi\",\"lastName\":\"saburo\",\"email\":\"string\",\"age\":31}"
7) "2"
8) "{\"@class\":\"com.kiyotakeshi.model.User\",\"id\":2,\"firstName\":\"sato\",\"lastName\":\"shiro\",\"email\":\"string\",\"age\":29}"

# key の case は区別される
> hgetall user
(empty array)

> hkeys USER
1) "1"
2) "4"
3) "3"
4) "2"

# @see https://redis.io/commands/hget
> hget USER "1"
"{\"@class\":\"com.kiyotakeshi.model.User\",\"id\":1,\"firstName\":\"yamada\",\"lastName\":\"taro\",\"email\":\"string\",\"age\":20}"

> hget USER 1
"{\"@class\":\"com.kiyotakeshi.model.User\",\"id\":1,\"firstName\":\"yamada\",\"lastName\":\"taro\",\"email\":\"string\",\"age\":20}"
```
