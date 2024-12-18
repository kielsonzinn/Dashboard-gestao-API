# DashboardGestaoAPI

### Subir banco de dados postgresql

```shell
docker run -d \
  --name postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=DashboardGestaoAPI \
  -p 5433:5432 \
  postgres
```

### Suba o kafka

```shell
docker-compose up -d
docker exec -it 74be6a6dfaa7 /opt/kafka/bin/kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic execution
```

### Subir DashboardGestaoAPI

```shell
git clone git@github.com:kielsonzinn/Dashboard-gestao-API.git
```
- So rodar a aplicaçao

### Subir automatic-code-review-dashboard-runner

```shell
git clone git@github.com:andreruizrt/automatic-code-review-dashboard-runner.git
```
- So rodar a aplicaçao