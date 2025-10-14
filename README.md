# Portfólio de Automação de Testes de API (Java) e Performance (JMeter)

## 🎯 Objetivo

Este projeto foi criado como um portfólio prático para demonstrar competências em automação de testes de API, utilizando o ecossistema Java, e testes de performance com JMeter. O foco é a criação de testes limpos, organizados e que sigam as melhores práticas de mercado, como as solicitadas em desafios técnicos para posições de QA Pleno.

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java (JDK 17)
* **Gestor de Dependências:** Maven
* **Biblioteca de Testes de API:** REST Assured
* **Framework de Testes:** JUnit 5
* **Ferramenta de Teste de Performance:** Apache JMeter

---

## 🧪 Testes de API (Java / JUnit / REST Assured)

O código-fonte dos testes funcionais se encontra em: `src/test/java/ReqResTests.java`.

Os seguintes cenários foram automatizados para a API `reqres.in`:

* **GET `/api/users?page=2`**: Valida a consulta a uma lista de utilizadores.
* **GET `/api/users/2`**: Valida a consulta a um utilizador específico, utilizando autenticação via API Key.
* **GET `/api/unknown/{id}`**: Valida a consulta a um recurso específico, demonstrando o uso de `path parameters`.
* **GET `/api/unknown/23`**: Valida o "caminho triste", garantindo que a API retorna `404 Not Found` para um recurso inexistente.
* **POST `/api/register`**: Valida a criação (registo) de um novo utilizador, enviando um `body` em JSON e verificando a resposta de sucesso.
* **DELETE `/api/users/2`**: Valida a exclusão de um utilizador, verificando o código de sucesso `204 No Content`.

### Como Executar os Testes Funcionais

1.  Clone este repositório.
2.  Abra o projeto numa IDE (como o IntelliJ IDEA).
3.  Execute o seguinte comando Maven no terminal:
    ```bash
    mvn test
    ```
4.  Os relatórios de execução serão gerados em `target/surefire-reports`.

---

## 🚀 Testes de Performance (JMeter)

Este projeto também inclui um cenário de teste de carga básico para o endpoint de registo de utilizador.

* **Ficheiro do Plano de Teste:** `testes-performance-jmeter/cenario-registro.jmx`
* **Objetivo:** Simular 10 utilizadores a registarem-se num período de 5 segundos.
* **Critérios de Sucesso:** Validar que todos os registos retornam HTTP 200 e analisar o tempo médio de resposta.

### Como Executar os Testes de Performance

1.  Tenha o [Apache JMeter](https://jmeter.apache.org/download_jmeter.cgi) instalado.
2.  Abra o JMeter.
3.  Vá em **File > Open** e selecione o ficheiro `cenario-registro.jmx` deste projeto.
4.  Clique no botão de "Play" verde para iniciar o teste.
5.  Analise os resultados nos listeners "View Results Tree" e "Summary Report".
