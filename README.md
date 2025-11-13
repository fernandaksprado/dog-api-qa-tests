# Testes Automatizados – Dog API (QA)

![Tests](https://img.shields.io/badge/tests-passing-brightgreen)
![Java](https://img.shields.io/badge/java-17-blue)
![Maven](https://img.shields.io/badge/build-maven-success)
![Last Commit](https://img.shields.io/github/last-commit/fernandaksprado/dog-api-qa-tests)


Documento elaborado por: Fernanda Prado  
Execução realizada em macOS – Novembro/2025

Este repositório contém uma suíte de testes automatizados desenvolvida para validar os principais endpoints da **Dog API**:  
https://dog.ceo/dog-api/documentation/

Os testes foram escritos em **Java 17**, utilizando **Maven**, **JUnit 5**, **Rest Assured** e **Jackson** para desserialização de JSON.

---

## 1. Objetivo

Garantir que a API:

- Responda com **status HTTP adequado**;
- Retorne dados no **formato correto**;
- Tenha comportamento consistente em **diferentes cenários**, incluindo casos de sucesso e erro;
- Exponha respostas válidas e estruturadas para consumo da aplicação.

Os endpoints cobertos são:

- `GET /breeds/list/all`
- `GET /breed/{breed}/images`
- `GET /breeds/image/random`

---

## 2. Requisitos de ambiente

Para executar o projeto, você precisa de:

- **Java 17** ou superior instalado e configurado no `PATH`;
- **Maven 3.8+** instalado;
- Acesso à internet (a API é pública e não requer token de autenticação).

Verifique as versões instaladas:

```
java -version
mvn -version
```

---

## 3. Como executar os testes

1. Clone este repositório ou faça o download do `.zip`;
2. Acesse a pasta raiz do projeto;
3. Execute os testes com:

```
mvn test
```

O Maven irá:

- Baixar todas as dependências;
- Compilar o projeto;
- Executar a suíte de testes automatizados.

---

## 4. Estrutura do projeto

```
dog-api-qa-tests/
├── pom.xml
├── README.md
└── src
    └── test
        └── java
            └── br
                └── com
                    └── qa
                        └── dogapi
                            ├── BaseTest.java
                            └── DogApiTests.java
```

---

## 5. Cenários de teste implementados

### 5.1. `GET /breeds/list/all`

Validações:

- `statusCode == 200`
- Campo `status` igual a `"success"`
- Campo `message` não nulo nem vazio
- Presença de raças conhecidas (`hound`, `bulldog`, `terrier`)
- Desserialização JSON usando Jackson

### 5.2. `GET /breed/{breed}/images`

**Cenário de sucesso:**  
- Raça válida: `"hound"`  
- Valida:
  - `statusCode == 200`
  - `status == "success"`
  - Lista de URLs não vazia
  - URLs começam com `https://images.dog.ceo/`

**Cenário negativo:**  
- Raça inexistente  
- Valida:
  - `statusCode == 404`
  - `status == "error"`
  - Mensagem contendo `"Breed not found"`

### 5.3. `GET /breeds/image/random`

**Imagem única:**  
- `statusCode == 200`
- `status == "success"`
- URL inicia com `https://images.dog.ceo/`
- Contém `.jpg`

**Múltiplas imagens:**  
- `quantity = 3`  
- Lista deve conter exatamente 3 URLs válidas

---

## 6. Relatório de resultados

Gerar relatório HTML:

```
mvn surefire-report:report
```

Arquivo gerado em:

```
target/site/surefire-report.html
```

O relatório mostra:

- Total de testes executados
- Sucessos e falhas
- Tempo de execução
- Detalhes completos de falhas

---

## 7. Como rodar em pipeline

Exemplo GitHub Actions:

```
- name: Run Dog API tests
  run: mvn -B test
```

---

## 8. Observações finais

- A API não necessita autenticação.  
- Estrutura pensada para ser clara, modular e extensível.  
- Classe `BaseTest` facilita inclusão de novos endpoints.  
- Cenário negativo adicional incluído para validação robusta do endpoint `/images`.

---