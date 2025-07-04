# 🧪 Avaliação Técnica – Desenvolvedor(a) Backend Pleno (Java + Spring)

## 📝 Objetivo

Criar uma API REST para gerenciamento de releases de sistemas. A API deverá ser desenvolvida utilizando **Java** com **Spring Boot**. seguindo as especificações abaixo.

## 🔗 Lista de Requests

Para realiszar os requests, foi criado um arquivo chamado [`request.http`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/request.http), que mostra as requisições REST que foram feitas para este projeto.

```http
# Aqui vai a lista de requisições na ordem em que elas devem ser feitas

# 1. Register
POST http://localhost:8080/auth/register
Content-Type: application/json

{
    "login": "darieldonMedeiros",
    "password": "123456789",
    "role": "ADMIN"
}

# 2. Login
POST http://localhost:8080/auth/login
Content-Type: application/json

{
    "login": "darieldonMedeiros",
    "password": "123456789"
}

# 3. Create Release (Somente ADMIN pode criar Release)
@token = COLAR_SEU_TOKEN_AQUI
# Aqui é o local que ficará salvo o token após ser feito o login

POST http://localhost:8080/releases
Authorization: Bearer {{token}}
Content-Type: application/json

{
    "system": "string",
    "version": "string",
    "commits": ["string", "string"] ,
    "notes": "string",
    "user": "string"
}

# A partir daqui não tem uma ordem específica para fazer as operações

# 4. Get Release By ID
@id = 1 # ID que pode ser utilizado em várias requisições
GET http://localhost:8080/releases/{{id}}
Authorization: Bearer {{token}}

# 5. Update Release Notes
PUT http://localhost:8080/releases/{{id}}
Authorization: Bearer {{token}}
Content-Type: application/json

{
    "notes": "darieldon"
}

# 6. Delete Release By ID
DELETE http://localhost:8080/releases/{{id}}
Authorization: Bearer {{token}}

# 7. Get All Releases
GET http://localhost:8080/releases
Authorization: Bearer {{token}}


# 8. Patch commit
PATCH http://localhost:8080/releases/2/commits
Authorization: Bearer {{token}}
Content-Type: application/json

{
    "commits": ["string1", "string2"]
}



```

## 🗃️ Modelo de Dados

Crie uma tabela chamada `releases`
utilizando o banco de dados H2, com o seguintes campos:

|   Campo    |     Tipo      |         Observações          |
| :--------: | :-----------: | :--------------------------: |
|     id     | autoIncrement |        Chave primária        |
|   system   | String (255)  | Trim automático, obrigatório |
|  version   |  String (30)  |         Obrigatório          |
|  commits   |  Json/Array   |  Lista de commits (strings)  |
|   notes    |     Text      |           Opcional           |
|    user    | String (100)  |         Obrigatório          |
| userUpdate | String (100)  |  Deve ser extraído do token  |
| releasedAt |   DateTime    |  Data de criação automática  |
| deletedAt  |   DateTime    |   Usado para "soft delete"   |

### Explicação da Tabela

Para criar a tabela `releases` no banco de dados H2 foi criada a classe [`ReleaseEntity.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/entities/ReleaseEntity.java) na pasta entities. As configurações do banco de dados H2 estão no arquivo [`application.yml`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/resources/application.yml) e quando o código é executado, gera a seguinte tabela sem dados no seguinte link: http://localhost:8080/h2-console.

![Tabela Criada no Banco H2](./imgs/TabelaBancoH2.jpg)

<center><b>Tabela Criada no banco H2</b></center>

Como a coluna commits no arquivo `ReleaseEntity` é um `List<String>`, logo foi necessário criar uma classe com o seguinte nome: [`StringListConverter.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/services/StringListConverter.java), que faz a conversão de um `List<String>` para `String` e vice-versa para que a coluna de commits apareça na tabela `releases`.

## 📍Endpoints

### 🔷 **Create Release**

`POST /releases`

**Request Body:**

```Json
{
    "system": "string",
    "version": "string",
    "commits": ["string", "string"],
    "notes": "string",
    "user": "string"
}
```

**Response:**

```Json
{
    "id": 1,
    "message": "Release criado com sucesso!"
}
```

#### Solução POST

A utilização da classe [`ReleaseController.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/controller/ReleaseController.java) junto da utilização do aplicativo <a href="https://www.postman.com/downloads/" target="_blank"><img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white"></a> que é onde de fato é feita a requisição de criação do release. Logo abaixo são mostrados o código da requisição POST e a imagem do aplicativo Postman.

```Java
    @PostMapping
    public ResponseEntity<Object> createRelease(@RequestBody @Valid ReleaseDTO releaseDTO){
        var releaseEntity = new ReleaseEntity();
        BeanUtils.copyProperties(releaseDTO, releaseEntity);
        releaseEntity.setReleasedAt(LocalDateTime.now());
        var savedRelease = releaseServices.saveRelease(releaseEntity);

        // Montando a resposta
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedRelease.getId());
        response.put("message", "Release criado com sucesso!");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
```

![Endpoint POST](./imgs/POST.jpg)

<center><b>Endoint POST</b></center>

### 🔷 **Get Release by ID**

`GET /releases/{id}`

**Response:**

```Json
{
  "message": "Release listado com sucesso.",
  "id": 1,
  "system": "string",
  "version": "string",
  "commits": ["string", "string"],
  "notes": "string",
  "user": "string",
  "userUpdate": "string",
  "releasedAt": "2025-05-26T14:00:00Z"
}
```

#### Solução GET

Da mesma forma que foi feito acima no endpoint POST, logo abaixo será mostrado o código do endpoint GET, assim como a imagem da requisição feita no Postman.

```Java
    @GetMapping("/{id}")
    public ResponseEntity<ReleaseResponseDTO> getReleaseByID(@PathVariable(value = "id") Long id){
        Optional<ReleaseEntity> releaseEntityOptional = releaseServices.findById(id);
        if (releaseEntityOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        ReleaseEntity releaseEntity = releaseEntityOptional.get();
        ReleaseResponseDTO releaseResponseDTO = new ReleaseResponseDTO(
            "Release listado com sucesso!",
            releaseEntity.getId(),
            releaseEntity.getSystem(),
            releaseEntity.getVersion(),
            releaseEntity.getCommits(),
            releaseEntity.getNotes(),
            releaseEntity.getUser(),
            releaseEntity.getUserUpdate(),
            releaseEntity.getReleasedAt()
        );

        return ResponseEntity.status(HttpStatus.OK).body(releaseResponseDTO);
    }
```

![Endpoint GET](./imgs/GET.jpg)

<center><b>Endoint GET</b></center>
<br>

Com a implementação do Spring Security através do JWT, o valor updateUser é atualizado de acordo com o código encontrado no arquivo [`ReleaseServices.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/services/ReleaseServices.java) mostrado abaixo.

```Java
    @Transactional
    public ReleaseEntity saveRelease(ReleaseEntity releaseEntity){
        String userUpdate = SecurityContextHolder.getContext().getAuthentication().getName();
        releaseEntity.setUserUpdate(userUpdate);
        return releaseRepository.save(releaseEntity);
    }

    public void updateReleaseNotes(Long id, UpdateNotesDTO updateNotesDTO) {
        String userUpdate = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<ReleaseEntity> releaseEntityOptional = findById(id);
        releaseEntityOptional.ifPresent(releaseEntity -> {
            releaseEntity.setUserUpdate(userUpdate);
            releaseEntity.setNotes(updateNotesDTO.notes());
            releaseRepository.save(releaseEntity);
        });
    }
```

### 🔷 **Update Release Notes**

`PUT /releases/{id}`

**Request Body:**

```Json
{
    "notes": "string"
}
```

**Response:**

```Json
{
    "message": "Release atualizado com sucesso!"
}
```

#### Solução PUT

Seguindo o mesmo padrão, logo abaixo tem o código da requisição PUT e a imagem da requisição no Postman, além de uma requisição GET, mostrando que de fato houve a mudança do valor em `notes`.

```Java
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateReleaseNotes(@PathVariable(value = "id") Long id, @RequestBody @Valid UpdateNotesDTO updateNotesDTO) {
        releaseServices.updateReleaseNotes(id, updateNotesDTO);
        GenericResponseDTO response = new GenericResponseDTO("Release atualizado com sucesso!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
```

![Endpoint PUT](./imgs/PUT.jpg)

<center><b>Endoint PUT</b></center>
<br>

![Endpoint PUT](./imgs/GET_verificacao.jpg)

<center><b>Endoint GET de Verificação</b></center>

### 🔷 **Delete Release (Soft Delete)**

`DELETE /releases/{id}`

**Response:**

```Json
{
    "message":"Release deletado com sucesso!"
}
```

#### Solução DELETE

Assim como foi feito nos itens anteriores, será mostrado o código responsável pelo delete, a imagem da requisição feita no Postman, a requisição GET não conseguindo acessar o item deletado e além disso será mostrada a imagem no banco H2 mostrando que de fato o item ainda se encontra na tabela, porém a informação deleted_at encontra-se preenchida.

```Java
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRelease(@PathVariable(value = "id") Long id){
        releaseServices.deleteRelease(id);
        GenericResponseDTO response = new GenericResponseDTO("Release deletado com sucesso!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
```

![Endpoint DELETE](./imgs/DELETE.jpg)

<center><b>Endoint DELETE</b></center>
<br>

![Endpoint DELETE](./imgs/GET_deleted.jpg)

<center><b>Endoint GET não conseguindo acessar o id 1</b></center>
<br>

![Endpoint DELETE](./imgs/H2_deletedAt.jpg)

<center><b>Tabela no banco H2 mostrando que o id ainda se encontra </b></center>

### 🔷 **Patch commit**

`PATCH /releases/{id}/commits`

**Request Body:**

```Json
{
    "commits": "string"
}
```

**Response:**

```Json
{
    "message": "Commits atualizados com sucesso!"
}
```

#### Solução PATCH

A requisição PATCH foi algo que eu mesmo pensei em adicionar, durante a resolução da avaliação, no VSCODE aparece a lista de Commits então eu pensei em algo com uma funcionalidade parecida.
Abaixo tem o código que faz a requisição `PATCH` EM ReleaseController.

```Java

    @PatchMapping("/{id}/commits")
    @Operation(summary = "Atualizar", description = "Atualizar commits do release com o ID x")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Commits atualizados com sucesso!"),
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro inesperado"),
    })
    public ResponseEntity<GenericResponseDTO> addCommitsToRelease(@PathVariable(value = "id") Long id, @RequestBody @Valid AddCommitsRequestDTO addCommitsRequestDTO){
        releaseServices.addCommitsToRelease(id, addCommitsRequestDTO);
        GenericResponseDTO response = new GenericResponseDTO("Commits atualizados com sucesso!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
```

Logo abaixo tem uma imagem de como ficou a requisição `PATCH` no Postman.

![Endpoint PATCH](./imgs/PATCH.jpg)

<center><b>Requisição PATCH </b></center>
<br>

### 🔷 **Autenticação JWT**

A autenticação JWT foi realizada utilizando Spring Security. Os arquivos que compõem as parte de autenticação são os seguintes:

1. Pasta configs

   - [`SecurityConfigurations.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/configs/SecurityConfigurations.java)
   - [`SecurityFilter.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/configs/SecurityFilter.java)

2. Pasta controller (Onde se criou 2 métodos POST: um para login e outro para register)

   - [`AuthenticationController.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/controller/AuthenticationController.java)

3. Pasta dto

   - [`AuthenticationDTO.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/dto/AuthenticationDTO.java)
   - [`LoginResponseDTO.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/dto/LoginResponseDTO.java)
   - [`RegisterDTO.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/dto/RegisterDTO.java)

4. Pasta entities

   - [`UserEntity.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/entities/UserEntity.java)
   - [`UserRole.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/entities/UserRole.java)

5. Pasta repository

   - [`UserRepository.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/repository/UserRepository.java)

6. Pasta services (onde de fato tem a classe que faz a criação do token utilizando o JWT)
   - [`AuthorizationService.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/services/AuthorizationService.java)
   - [`TokenService.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/services/TokenService.java)

Como foi mencionado no item 2, foram feitas 2 requisições POST no AuthenticationController, onde a requisição via Postman é mostrada abaixo.

![Endpoint POST](./imgs/REGISTER.jpg)

<center><b>Endpoint PUT: Registra o login, senha e role do usuário </b></center>
<br>

![Endpoint POST](./imgs/LOGIN.jpg)

<center><b>Endpoint PUT: Realiza o login do usuário e gera o token via JWT </b></center>
<br>

**Observação: só quem possui a role `"ADMIN"` pode utilizar o comando Post para gerar o release.**

### 🔷 **Tratamento de Erros e Validações**

Este item foi feito de forma parcial ao criar a pasta exception que possui 2 arquivos:

1. [`GlobalException.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/exception/GlobalException.java): a classe que possui a annotation `@ControllerAdvice` que permite a centralização da lógica de tratamento de exceções.
2. [`ResourceNotFoundException.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/exception/ResourceNotFoundException.java) classe simples que é acionada se o `HttpStatus.NOT_FIND` acontecer.

### 🔷 **Swagger**

Através da pasta configs, foi criado o arquivo [`OpenAPIConfiguration.java`](<(https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/configs/OpenApiConfigurations.java)>), que instancia o Swagger para a aplicação.

Foi necessário também criar uma Função de validação (`@Bean`) no arquivo [`SecurityConfigurations.java`](https://github.com/DarieldonMedeiros/DesafioSpringBoot/blob/main/src/main/java/com/zipdin/avaliacao/configs/SecurityConfigurations.java) para que as instâncias do Swagger fossem ignoradas pelo Spring Security.

```Java
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> { web.ignoring().requestMatchers(
                                    "/v2/api-docs/**",
                                    "v3/api-docs/**",
                                    "/swagger-resources/**",
                                    "/swagger-ui.html",
                                    "/swagger-ui/**",
                                    "/webjars/**"
            );
        };
    }
```

A seguir, tem uma imagem de como fica o Swagger, obtido pelo link http://localhost:8080/swagger-ui/index.html#/

![Endpoint POST](./imgs/SWAGGER.jpg)

<center><b>Visual do Swagger na Web </b></center>
<br>

Pelo Swagger, tem como fazer a autenticação via um Bearer, que é obtido pela requisição de cadastro em seguida do login no Postman.

### 🔷 **Dockerfile**

Antes de rodar o Dockerfile, primeiramente é necessário criar o pacote .jar da aplicação. Ao abrir o prompt de comando/PowerShell no windows, o comando utilizado foi o seguinte:

```PowerShell
./mvnw clean package -DskipTests
```

Após a criação do arquivo .jar na pasta target, o arquivo Dockerfile utilizado foi o que está logo abaixo:

```Dockerfile
FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install

FROM openjdk:17-jdk-slim

EXPOSE 8080
COPY --from=build /target/avaliacao-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
```

Com esse arquivo Dockerfile, é possível criar a imagem com o seguinte comando:

```PowerShell
docker build --tag zipdin/avaliacao .
```

Após a criação da imagem, agora é necessário o seguinte comando para rodar a imagem.

```PowerShell
docker run --name avaliacao -p 8080:8080 zipdin/avaliacao
```

## ✅ Requisitos Técnicos

- Java 17+ com Spring Boot: **_(Foi utilizado o Java 17 nesta avaliação) ✅_**
- Persistência com JPA: **_Implementado com sucesso! ✅_**
- Commit da prova em repositório Git público ou zip: **_Implementado com sucesso! ✅_**
- Autenticação JWT (mockada ou simples): **_Implementado com sucesso! ✅_**

- Tratamento de erros e validações: **_Implementado parcialmente! ✅_**
- Swagger ou documentação de endpoints: **_Implementado com sucesso! ✅_**

## 🔧 Pontos Bônus (não obrigatórios)

- Criar paginação e filtros de pesquisa nas APIs de listagem: **_Implementado com sucesso a parte de paginação através do Get All! ✅_**
- Criar um dockerfile para a aplicação, que permita a fácil construção de uma imagem Docker da aplicação: **_Implementado com sucesso! ✅_**

## 📲 Dependências Utilizadas no pom.xml

- Spring DATA JPA
- Validation
- Lombok
- Spring WEB
- Spring Security
- Java JWT
- SpringDoc OpenAPI (Swagger)
- H2 Database

OBS.: O único item que de fato não consegui fazer foi os Testes Unitários e/ou de integração.
## 🧑🏻‍💼 Autor

Darieldon de Brito Medeiros

### Seguem abaixo o meu email e meu linkedin

<a href = "darieldonbm99@outlook.com"><img src="https://img.shields.io/badge/Microsoft_Outlook-0078D4?style=for-the-badge&logo=microsoft-outlook&logoColor=white" target="_blank"></a>
<a href="https://www.linkedin.com/in/darieldon-de-brito-medeiros" target="_blank"><img src="https://img.shields.io/badge/-LinkedIn-%230077B5?style=for-the-badge&logo=linkedin&logoColor=white" target="_blank"></a>
