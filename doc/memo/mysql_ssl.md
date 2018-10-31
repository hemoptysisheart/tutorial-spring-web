# MySQL SSL

MySQL이 기본적으로 SSL 커넥션을 사용하도록 변경됐다.
그 때문에 MySQL 업데이트 후 접속할 수 없는 문제가 생긴다.

## 기본 MySQL 설정

`Homebrew`로 설치.

```zsh
➜  / cat /usr/local/etc/my.cnf
# Default Homebrew MySQL server config
[mysqld]
# Only allow connections from localhost
bind-address = 127.0.0.1
```
=&gt;
```zsh
# Default Homebrew MySQL server config
[mysqld]
# Only allow connections from localhost
bind-address = 127.0.0.1
skip_ssl
```

## JDBC URL

이후 JDBC URL에 `allowPublicKeyRetrieval=true&useSSL=false`을 추가.

`jdbc:mysql://localhost/tutorial_spring_web?connectionCollation=utf8mb4_bin`
=&gt;
`jdbc:mysql://localhost/tutorial_spring_web?connectionCollation=utf8mb4_bin&allowPublicKeyRetrieval=true&useSSL=false`

물론 운영 DB에서는 SSL 설정을 사용하는 것이 좋다.
