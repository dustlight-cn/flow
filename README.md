# Flow
[简介](#简介) | [部署](#部署) | [依赖](#依赖)

## 简介
基于 Zeebe 的 BPM 服务。

## 部署
> 此服务依赖 Elasticsearch、Zeebe、RabbitMQ 以及 Datacenter。

### Helm 部署
选择此部署方式必须先安装 [Helm](https://helm.sh)。  
请查看 Helm 的 [文档](https://helm.sh/docs) 获取更多信息。

当 Helm 安装完毕后，使用下面命令添加仓库：

    helm repo add flow https://dustlight-cn.github.io/flow

若您已经添加仓库，执行命令 `helm repo update` 获取最新的包。
您可以通过命令 `helm search repo flow` 来查看他们的 charts。

创建配置文件 values.yaml：
```yaml
ingress:
  className: "nginx" # Ingress Class Name
  host: "flow.dustlight.cn" # 服务域名
  tls:
    enabled: false # 是否开启 HTTPS
    crt: "" 
    key: ""

config:
  elasticsearch:
    endpoints:
      - "elasticsearch-master:9200" # Elasticsearch 地址
  zeebe:
    address: "zeebe-zeebe-gateway.zeebe:26500" # Zeebe 网关
  datacenter:
    releaseName: "my-dc" # Datacenter 的 ReleaseName
    endpoint: "http://datacenter-service" # datacenter 地址
  rabbitmq:
    host: "rabbitmq-svc" # RabbitMQ 服务地址
    port: 5672 # RabbitMQ 服务端口
    username: "" # RabbitMQ 用户名
    password: "" # RabbitMQ 密码
  auth:
    clientId: "test" #替换为自己的 ClientID
    clientSecret: "e6423085f5165a58f8949e763c6691ffe44e2f86" #替换为自己的 ClientSecret
```

安装：

    helm install -f values.yaml my-flow flow/flow-service

卸载：

    helm delete my-flow

## 依赖
### Zeebe
[Zeebe 部署文档](https://github.com/camunda-community-hub/camunda-cloud-helm/tree/main/charts/zeebe-cluster-helm)

```bash
helm repo add zeebe https://helm.camunda.io
helm install zb zeebe/zeebe-cluster-helm
```

### RabbitMQ

[RabbitMQ 部署文档](https://www.rabbitmq.com/kubernetes/operator/operator-overview.html)

### Datacenter

[Datacenter 部署文档](https://dustlight-cn.github.io/datacenter)