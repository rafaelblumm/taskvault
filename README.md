# TaskVault

Projeto da disciplina Engenharia de Software III

- [Arquivos de configuração](#arquivos-de-configuração)
  - [.env](#env)
  - [secrets/db_password.txt](#secretsdb_passwordtxt)
  - [secrets/db_root_password.txt](#secretsdb_root_passwordtxt)
  - [secrets/jwt_issuer.txt](#secretsjwt_issuertxt)
  - [secrets/jwt_key.txt](#secretsjwt_keytxt)
- [Nota para desenvolvedores](#nota-para-desenvolvedores)
  - [Execução do ambiente Docker](#execução-do-ambiente-docker)
  - [Execução do JAR](#execução-do-jar)

## Arquivos de configuração

Para configurar o acesso da aplicação ao banco de dados, é necessário criar três
arquivos. Os caminhos apresentados são relativos ao diretório raiz da aplicação.
Para gerenciar os segredos da aplicação, é utilizado o
[Docker Compose secrets](https://docs.docker.com/reference/compose-file/secrets/),
que gerencia segredos em arquivos ao copiá-los diretamente para os containers
Docker.

### .env

O arquivo `.env` contém informações não sigilosas de conexão com o banco de dados
com as seguintes chaves disponíveis:

| Chave        | Descrição                              | Exemplo     | Obrigatório |
| ------------ | -------------------------------------- | ----------- | ----------- |
| `DB_HOST`    | Endereço do banco de dados             | _localhost_ | **Sim**     |
| `DB_PORT`    | Porta do banco de dados                | _3306_      | **Sim**     |
| `DATABASE`   | Nome do schema no banco de dados       | _taskvault_ | **Sim**     |
| `DB_USER`    | Usuário da aplicação no banco de dados | _appuser_   | **Sim**     |
| `LOG_LEVEL`  | Nível de log da aplicação              | _debug_     | Não         |

Exemplo: [.env.example](docs/.env.example)

### secrets/db_password.txt

O arquivo deve conter **SOMENTE** a senha do usuário do banco de dados informado na
chave `DB_USER` do arquivo `.env`.

Exemplo:

```plaintext
SuperSecretUserPassword123
```

### secrets/db_root_password.txt

O arquivo deve conter **SOMENTE** a senha do usuário _root_ do banco de dados. Esse
acesso é utilizado para configuração inicial do banco de dados.

Exemplo:

```plaintext
SuperSecretRootPassword321
```

### secrets/jwt_issuer.txt

O arquivo deve conter **SOMENTE** o nome do _issuer_ de tokens JWT.

Exemplo:

```plaintext
MyJwtIssuer
```

### secrets/jwt_key.txt

O arquivo deve conter **SOMENTE** a chave de assinatura de tokens JWT.

Exemplo:

```plaintext
MySuperSecretJwtKey
```

## Nota para desenvolvedores

### Execução do ambiente Docker

O ambiente da aplicação é executado em containers Docker e, dessa forma, é necessário
possuir o [Docker instalado](https://www.docker.com/get-started/). As configurações de
infraestrutura da aplicação (conexão com o BD) são realizadas em alguns arquivos que
devem ser criados no repositório. Por se tratarem de arquivos com informações sigilosas,
não devem ser versionados junto com os demais arquivos do repositório.

Para iniciar os containers, é necessário executar o comando abaixo no diretório raiz
do projeto:

```bash
docker compose up
```

Para _buildar_ os containers novamente em caso de alguma alteração no projeto, é
necessário informar o parâmetro `--build` para recriar o ambiente:

```bash
docker compose up --build
```

### Execução do JAR

Em modo desevolvimento, é necessário executar a aplicação Spring com o perfil de
desenvolvimento _dev_. Para definir as credenciais de acesso ao banco de dados,
pode ser criado o arquivo `src/main/resources/application-dev.properties` com

```properties
# URL de conexão com o banco de dados no formato:
# jdbc:<TIPO_BD>://<ENDEREÇO_BD>:<PORTA_BD>/<NOME_SCHEMA>
spring.datasource.url: jdbc:mysql://localhost:3306/taskvault
# Nome de usuário do banco de dados
spring.datasource.username: user
# Senha do usuário do banco de dados
spring.datasource.password: pass
# Nome do issuer de JWTs
app.jwt.issuer: MyTokenIssuer
# Chave para assinar JWTs
app.jwt.key: MyTokenKey
```

Para executar a aplicação em modo desenvolvimento, basta executar o seguinte
comando no diretório raiz do projeto:

```bash
java -jar target/taskvault-server-0.0.1.jar --spring.profiles.active=dev
```
