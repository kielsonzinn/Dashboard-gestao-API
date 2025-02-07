# DashboardGestaoAPI

- Projeto principal com a documentação [acr-gateway](https://github.com/andreruizrt/acr-gateway)

### Suba o kafka

```shell
docker exec -it 74be6a6dfaa7 /opt/kafka/bin/kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic execution
```

### Subir automatic-code-review-dashboard-runner

```shell
git clone git@github.com:andreruizrt/automatic-code-review-dashboard-runner.git
```
- So rodar a aplicaçao

### Segunda entrega

- feign client para o dashboard chamar a auth
