# Projeto Spring Cloud com Eureka e Microsserviços

Este projeto demonstra uma arquitetura básica de microsserviços utilizando Spring Cloud e Docker. Ele consiste em três componentes principais:

* **Eureka Server (`eureka-server`):** Um servidor de registro e descoberta de serviços.
* **Microsserviço 01 (`microsservico01`):** Uma aplicação Spring Boot que se registra no Eureka Server e pode interagir com outros serviços.
* **Microsserviço 02 (`microsservico02`):** Outra aplicação Spring Boot que também se registra no Eureka Server.

## Spring Cloud

Spring Cloud é um framework que fornece ferramentas para construir aplicações distribuídas na nuvem. Ele oferece soluções para padrões comuns 
em arquiteturas de microsserviços, como descoberta de serviços, configuração distribuída, roteamento inteligente, balanceamento de carga, circuit breaking e muito mais.

Neste projeto, utilizamos o **Eureka** para a descoberta de serviços. O Eureka permite que os microsserviços se localizem dinamicamente, sem 
a necessidade de configuração manual de endereços.

## Funcionamento dos Componentes

1.  **Eureka Server (`eureka-server`)**:
    * É a espinha dorsal do sistema de descoberta de serviços.
    * Ao iniciar, ele se torna um registro central onde os microsserviços podem se registrar, informando sua localização (endereço IP e porta).
    * Os outros microsserviços consultam o Eureka Server para descobrir a localização de outras instâncias de serviço.
    * Neste projeto, o `eureka-server` é uma aplicação Spring Boot anotada com `@EnableEurekaServer`.

2.  **Microsserviço 01 (`microsservico01`)**:
    * É uma aplicação Spring Boot que representa um dos seus serviços de negócio.
    * Ao iniciar, ele se registra no Eureka Server, tornando-se visível para outros serviços.
    * Para se registrar, ele utiliza a dependência `spring-cloud-starter-netflix-eureka-client` e é anotado com `@EnableDiscoveryClient`
    * (ou `@EnableEurekaClient`, que também cumpre essa função).
    * Ele pode usar o `DiscoveryClient` do Spring Cloud para consultar o Eureka Server e obter informações sobre outros serviços registrados.

3.  **Microsserviço 02 (`microsservico02`)**:
    * Similar ao Microsserviço 01, é outra aplicação Spring Boot que representa um serviço de negócio.
    * Ele também se registra no Eureka Server ao iniciar, utilizando as mesmas dependências e anotações do Microsserviço 01.
    * Pode interagir com o Microsserviço 01 ou outros serviços registrados através da descoberta fornecida pelo Eureka.

## Executando com Docker

Para executar este projeto utilizando Docker, você precisará ter o Docker instalado em sua máquina. Os seguintes passos descrevem o processo
geral (assumindo que você tenha `Dockerfiles` para cada projeto e um `docker-compose.yml` para orquestrar os contêineres):

1.  **Construir as Imagens Docker**:
    * Navegue até o diretório de cada projeto (`eureka-server`, `microsservico01`, `microsservico02`) e construa as imagens Docker usando
    * o respectivo `Dockerfile`. Por exemplo:
        ```bash
        docker build -t eureka-server ./eureka-server
        docker build -t microsservico01 ./microsservico01
        docker build -t microsservico02 ./microsservico02
        ```
        Certifique-se de que os `Dockerfiles` copiam o arquivo JAR construído (geralmente em `target/*.jar`) para dentro da imagem e
      definem o comando para executar a aplicação Java.

2.  **Orquestrar com Docker Compose (`docker-compose.yml`)**:
    * Crie um arquivo `docker-compose.yml` na raiz do seu projeto (ou em um diretório pai) para definir e gerenciar os contêineres
    * dos três serviços. Um exemplo básico poderia ser:

        ```yaml
        version: '3.8'
        services:
          discovery-server:
            image: eureka-server
            ports:
              - "8761:8761"
            networks:
              - spring-cloud-network

          microsservico01:
            image: microsservico01
            ports:
              - "8081:8081"
            environment:
              eureka.client.service-url.defaultZone: http://discovery-server:8761/eureka
            depends_on:
              - discovery-server
            networks:
              - spring-cloud-network

          microsservico02:
            image: microsservico02
            ports:
              - "8082:8082"
            environment:
              eureka.client.service-url.defaultZone: http://discovery-server:8761/eureka
            depends_on:
              - discovery-server
            networks:
              - spring-cloud-network

        networks:
          spring-cloud-network:
            driver: bridge
        ```

    * **Explicação do `docker-compose.yml`**:
        * `version`: Define a versão do formato do Docker Compose.
        * `services`: Define os contêineres que serão executados.
            * `discovery-server`: Utiliza a imagem `eureka-server` e mapeia a porta 8761 do contêiner para a porta 8761 da máquina host.
            * `microsservico01` e `microsservico02`: Utilizam suas respectivas imagens, mapeiam suas portas (8081 e 8082),
            * e configuram a variável de ambiente `eureka.client.service-url.defaultZone` para apontar para o serviço `discovery-server`
            * na rede Docker. `depends_on` garante que o Eureka Server seja iniciado antes dos microsserviços.
        * `networks`: Define uma rede bridge para que os contêineres possam se comunicar usando seus nomes de serviço.

3.  **Iniciar os Contêineres**:
    * Navegue até o diretório onde o seu `docker-compose.yml` está localizado e execute o seguinte comando:
        ```bash
        docker-compose up --build -d
        ```
        O flag `--build` irá construir as imagens (se necessário), e o flag `-d` irá executar os contêineres em segundo plano.

4.  **Acessar os Serviços**:
    * Você poderá acessar o Eureka Server no seu navegador em `http://localhost:8761`. Lá, você deverá ver os microsserviços
    * `microsservico01` e `microsservico02` registrados.
    * Você poderá interagir com os microsserviços através das portas mapeadas (por exemplo, `http://localhost:8081` e `http://localhost:8082`),
    * dependendo das APIs que você implementou em cada um.
  
5.  **Spring Eureka em execução**

```Spring Eureka em execução com 02 instâncias registradas (microsservico01 e microsservico02)```

![Minha Imagem](https://github.com/isaccanedo/spring-cloud/raw/main/images/eureka.png)


6.  **Docker em execução**

![Minha Imagem](https://github.com/isaccanedo/spring-cloud/raw/main/images/docker.png)
