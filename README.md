# HubSpot Integration API

Este projeto é uma API Spring Boot desenvolvida para integrar com os serviços do HubSpot, permitindo operações como a criação de contatos de forma eficiente e segura. O código foi estruturado para ser extensível e escalável, utilizando boas práticas e bibliotecas que promovem robustez e simplicidade no desenvolvimento.

---

## **Instruções para Execução**

### **Pré-requisitos**
Certifique-se de que os seguintes itens estejam instalados no ambiente:
- **Java 17 ou superior**
- **Maven 3.6+**
- **Docker (opcional, para execução com contêineres)**

### **Configuração do Ambiente**
1. Clone o repositório:
   ```bash
   git clone [<URL_DO_REPOSITORIO>](https://github.com/GabrielOlivetro/Case-Tecnico-Integracao-com-HubSpot)
   cd hubspot-integration-api
   ```

2. Configure as propriedades do projeto no arquivo `application.yml` ou `application.properties`. Exemplo:
   ```yaml
   hubspot:
     client-id: "SEU_CLIENT_ID"
     client-secret: "SEU_CLIENT_SECRET"
     redirect-uri: "http://localhost:8080/auth/callback"
     scopes: "crm.objects.contacts.write oauth crm.objects.contacts.read"
   ```

3. Opcionalmente, configure as dependências do Resilience4j para gerenciar limites de requisições:
   ```yaml
   resilience4j:
     ratelimiter:
       instances:
         hubspotContact:
           limit-for-period: 100
           limit-refresh-period: 10s
           timeout-duration: 500ms
   ```

### **Executando o Projeto**
#### 1. Usando Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

#### 2. Usando Docker:
   - Construa a imagem Docker:
     ```bash
     docker build -t hubspot-integration-api .
     ```
   - Execute o contêiner:
     ```bash
     docker run -p 8080:8080 hubspot-integration-api
     ```

A API estará disponível em `http://localhost:8080`.

---

## **Documentação Técnica**

### **Decisões de Projeto**
1. **Estrutura em Camadas**:
   - Separação clara entre controladores, serviços e configurações para facilitar manutenção e extensibilidade.
   - Uso de DTOs para transferência de dados entre a API e serviços.

2. **Bibliotecas Utilizadas**:
   - **Spring Boot**: Para simplicidade no desenvolvimento e configuração.
   - **Resilience4j**: Para gerenciar limites de requisição e oferecer maior resiliência.
   - **Jackson (YAML)**: Para facilitar a leitura de arquivos de configuração.
   - **WebClient**: Substituto moderno para `RestTemplate`, suportando programação reativa.

3. **Configuração Centralizada**:
   - Propriedades do HubSpot gerenciadas centralmente em `application.yml` ou `application.properties`.
   - Suporte à leitura de arquivos YAML externos para configurações adicionais.

4. **Rate Limiting**:
   - Rate limiting implementado com Resilience4j para respeitar os limites da API do HubSpot e evitar penalizações.

---

## **Possíveis Melhorias Futuras**
1. **Implementar Testes Automatizados**:
   - Testes unitários com JUnit e Mockito para garantir a qualidade do código.
   - Testes de integração para validar a interação com a API do HubSpot.

2. **Adicionar Suporte a Webhooks**:
   - Expandir a funcionalidade para lidar com eventos do HubSpot em tempo real.

3. **Segurança**:
   - Implementar autenticação com OAuth2 em endpoints críticos.
   - Criptografar informações sensíveis, como tokens, antes de armazená-las.

4. **Melhorar a Observabilidade**:
   - Adicionar métricas com Prometheus e visualizações com Grafana.
   - Implementar logs estruturados com ferramentas como ELK (Elasticsearch, Logstash, Kibana).

5. **Suporte a Mais Operações**:
   - Expandir a API para incluir funcionalidades como leitura de negócios, empresas e outros objetos do CRM.

---

## **Contribuição**
Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou enviar pull requests com melhorias e correções.
