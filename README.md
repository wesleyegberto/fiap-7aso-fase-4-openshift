# Fiap - Fase 4 - Projeto de Entrega

## Atividade

- Etapa 1: baixe uma imagem de container, realize as modificações necessárias, adicione tag, commite e depois faça o push da imagem customizada. Bônus: Crie um dockerfile para imagem customizada.
  - Entrega: arquivo [Dockerfile](./Dockerfile)
- Etapa 2: faça o deploy de uma aplicação no OpenShift, adicione uma rota/ingress controller, adicione camada de persistência de dados, e realize deploy de um DB.
  - Entregas:
    - Rota: service e route declarados nos arquivos [ApiProdutos-Service.yml](./ApiProdutos-Service.yml) e [ApiProdutos-Route.yml](./ApiProdutos-Route.yml);
    - Camada persistência de dados: PVC declarado no arquivo [BancoDados-Pvc.yml](./BancoDados-Pvc.yml);
    - Deploy do BD: deployment declarado no arquivo [BancoDados-Deployment.yml](./BancoDados-Deployment.yml).
- Etapa 3: manipule variáveis por meio de secrets, altere a aplicação e configure escalabilidade dinâmica HPA.
  - Entregas:
    - Secrets declarado no arquivo [BancoDados-Secret.yml](./BancoDados-Secret.yml) que são utilizados pelos deployments do BD e da API;
    - HPA da API declarado no arquivo [ApiProdutos-Hpa.yml](./ApiProdutos-Hpa.yml).
- Etapa 4: utilize os conceitos DevOps e GitOps e commite para um repositório público todo o código necessário para o provisionamento automatizado dos recursos que haviam sido criados de forma manual até a etapa 3.
  - Entregas:
    - Github Action declarado no arquivo [.github/workflows/deploy-openshift.yml](./.github/workflows/deploy-openshift.yml) para fazer o build da imagem do Docker e o push para o registry do Quay.io;
    - Github Action declarado no arquivo [.github/workflows/deploy-resources.yml](./.github/workflows/deploy-resources.yml) para fazer o provisionamento dos recursos.

## Projeto

![](img/topologia.png)

### Banco de Dados

Banco de dados é uma instância do MySQL.
Criado através de um Pod como exemplo.

### API Produtos

API para gerenciar produtos (exemplo das aulas).
Criado através de um Deployment contendo o Pod.

URLs:

- Local: `http://localhost:8080`
- OpenShift (exemplo): `http://api-produtos-entrega.apps.na46.prod.nextcle.com`

Listagem de produtos:

```sh
curl $URL/products
```

Cadastro de um produto:

```sh
curl -X POST $URL/products -H 'Content-type:application/json' -d '{"name": "PS5", "description":"Console PlayStation 5"}'
```

## Rodar Projeto Local com Docker

```sh
docker-compose up
```

## Build do Projeto

```sh
# login no Quay.io
podman login -u $QUAY_USER quay.io

# build da imagem
podman build -t fiap/fiap-api-produtos:1 .

# envia imagem para o registry
podman push fiap/fiap-api-produtos:1 quay.io/$QUAY_USER/fiap-api-produtos:1
```

## Deploy no OpenShift

Logar no cluster pelo CLI (acessos do DO180):

```sh
oc login -u $RHT_OCP4_DEV_USER -p $RHT_OCP4_DEV_PASSWORD $RHT_OCP4_MASTER_API

oc new-project entrega
```

Rodar os comandos:

```sh
# criar o secrets
oc create -f BancoDados-Secret.yml

# criar o banco de dados MySQL
oc create -f BancoDados-Pvc.yml
oc create -f BancoDados-Deployment.yml
oc create -f BancoDados-Service.yml

# criar a aplicação
oc create -f ApiProdutos-Deployment.yml
oc create -f ApiProdutos-Hpa.yml
oc create -f ApiProdutos-Service.yml
oc create -f ApiProdutos-Route.yml
```

## Stress Test

Para rodar o stress test instale o [K6](https://k6.io/).
Talvez seja preciso rodar `ulimit -n 250000` para permitir maior número de conexões na máquina local.

```sh
sh stress-test-run.sh
```

O stress test roda aumentando a carga de 100 VU durante 30 seg e mantém no nível por mais 1 min antes de aumentar novamente.
A quantidade de VU é aumentada até 400 antes de finalizar o teste.
