# API

## 1. Visão Geral

Este projeto é uma API REST para gerenciamento de Chamados de Serviço, construída com **Java 17**, usando o framework **Spring Boot 3.5.0**, com persistência em **PostgreSQL 9.5+**. O código utiliza **Maven** como gerenciador de dependências e o plugin **Lombok** para reduzir boilerplate.

## 2. Tecnologias Utilizadas e Justificativa

- **Java 17**: versão LTS, com melhorias de performance, segurança e suporte a novas funcionalidades modernas.
- **Spring Boot 3.5.0**: framework consolidado para criação de aplicações Java, com configuração simplificada e integração nativa com várias tecnologias, acelerando o desenvolvimento.
- **PostgreSQL 9.5+**: banco de dados relacional robusto, open-source, com suporte a diversas funcionalidades avançadas e ótima escalabilidade.
- **Maven**: gerenciador de dependências que facilita a construção e gerenciamento do projeto.
- **Lombok**: reduz código repetitivo, gerando getters, setters, construtores automaticamente, deixando o código mais limpo e fácil de manter.
- **Swagger (Springdoc OpenAPI)**: documentação interativa da API, facilitando testes e entendimento dos endpoints.

## 3. Configuração e Execução da Aplicação

### Pré-requisitos:

- Java 17 instalado
- Maven instalado
- Banco de dados PostgreSQL 9.5+ rodando localmente ou acessível
- (Opcional) Postman ou curl para testar a API

### Passos para configurar e executar:

- Clone o repositório:git clone https://github.com/seu-usuario/seu-repositorio.git
- Configure as credenciais do banco no arquivo application.properties:
  spring.datasource.url=jdbc:postgresql://localhost:5432/nomeBanco
  spring.datasource.username=usuario_Banco
  spring.datasource.password=senha_Banco
  spring.jpa.hibernate.ddl-auto=update
- Build do projeto:mvn clean install
- Execute a aplicação: mvn spring-boot:run

A aplicação estará rodando no endereço padrão: http://localhost:8080

## 4. Documentação da API (Swagger)

Este projeto possui documentação interativa da API gerada automaticamente com Swagger (Springdoc OpenAPI).

Após iniciar a aplicação, acesse a interface de documentação no navegador:
http://localhost:8080/service-desk/swagger-ui/index.html#/

Na interface Swagger você pode:

- Visualizar todos os endpoints disponíveis.
- Ver detalhes das requisições e respostas.
- Testar os endpoints diretamente pela interface.
- Foi criado end-point específico para verificar a saúde do projeto.

## 5. Exemplos de chamadas aos Endpoints
- Retorna a lista de tickets.
curl -X GET http://localhost:8080/api/tickets

- Salvar dados do novo ticket
curl -X POST http://localhost:8080/api/tickets \
-H "Content-Type: application/json" \
-d '{
"titulo": "Novo ticket",
"descricao": "Descrição do ticket",
"categoria": "Categoria A"
}'

- Atualizar dados do ticket
  curl -X 'PUT' \
  'http://localhost:8080/api/tickets/18' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "titulo": "string",
  "descricao": "string",
  "categoria": "string",
  "sentimento": "string"
  }'

- Verificar a saúde da aplicação
- curl -X GET http://localhost:8080/api/health


## 6. Notas sobre credenciais
Para fins de desenvolvimento e testes, utilize credenciais fictícias no arquivo de configuração (application.properties).
