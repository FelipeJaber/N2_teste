# Sistema de Vendas de Livros

### Feito por:
- Felipe Jaber
- Breno Cardoso
- Karen Quézia
- Maria Laura Menezes

## Link para o video do projeto funcionando: https://drive.google.com/drive/folders/17qiITSZTbyd3Lbh2c2rrfmqrKNZLwWZB?usp=sharing

## Instruções de Execução

### Projeto Avaliação N2 - Teste De Software

### Pré-requisitos
- Java 25 ou superior
- Maven instalado (ou usar o wrapper mvnw)

### Como Executar

1. Navegue até o diretório do projeto:
```bash
cd "C:\Users\Usuario\IdeaProjects\n2 teste"
```

2. Execute a aplicação com Maven:
```bash
.\mvnw spring-boot:run
```

A aplicação iniciará em: **http://localhost:8080**

### Credenciais Padrão
- **Usuário**: admin
- **Senha**: admin123

### Dados Iniciais Criados Automaticamente

Ao iniciar a aplicação, os seguintes dados são criados automaticamente:

**Usuários:**
- ID: 1, Username: admin, Password: admin123

**Livros:**
- O Senhor dos Anéis - J.R.R. Tolkien - R$ 85,90 - Fantasia
- 1984 - George Orwell - R$ 45,00 - Ficção Científica
- Dom Casmurro - Machado de Assis - R$ 35,50 - Romance

## API Endpoints

### Usuários
- `GET /users` - Listar todos os usuários
- `GET /users/{id}` - Obter usuário por ID
- `POST /users` - Criar novo usuário
- `PUT /users/{id}` - Atualizar usuário
- `DELETE /users/{id}` - Excluir usuário
- `POST /users/login` - Fazer login (passa username e password no body)

### Livros
- `GET /books` - Listar todos os livros
- `GET /books/{id}` - Obter livro por ID
- `POST /books` - Criar novo livro
- `PUT /books/{id}` - Atualizar livro
- `DELETE /books/{id}` - Excluir livro

### Vendas
- `GET /sales` - Listar todas as vendas
- `GET /sales/{id}` - Obter venda por ID
- `POST /sales` - Registrar nova venda
- `PUT /sales/{id}` - Atualizar venda
- `DELETE /sales/{id}` - Excluir venda

## Testando a API

### Usando cURL

```bash
# Listar usuários
curl -X GET http://localhost:8080/users

# Listar livros
curl -X GET http://localhost:8080/books

# Fazer login
curl -X POST http://localhost:8080/users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Criar novo livro
curl -X POST http://localhost:8080/books \
  -H "Content-Type: application/json" \
  -d '{"title":"1984","author":"George Orwell","price":45.00,"genre":"Ficção Científica"}'
```

## Estrutura do Banco de Dados

O projeto usa **H2 Database** (banco em memória). As tabelas são:
- `app_user` - Usuários do sistema
- `book` - Livros disponíveis
- `sale` - Registro de vendas

## Tecnologias Utilizadas

- **Backend**: Spring Boot 4.0.6
- **Frontend**: HTML, CSS, JavaScript puro
- **Banco de Dados**: H2 (em memória)
- **ORM**: Hibernate JPA
- **Build**: Maven
- **Java**: 25

## Funcionalidades

✅ Autenticação básica de usuários  
✅ CRUD de usuários  
✅ CRUD de livros (título, autor, preço, gênero)  
✅ CRUD de vendas  
✅ Dashboard com estatísticas  
✅ Interface responsiva  
✅ Full Stack - Frontend HTML + API REST  

## Testes Automatizados

### Testes Unitários (JUnit + Mockito)
Validação da lógica da camada de serviço de usuários.

**Cenários testados:**
- autenticação com credenciais válidas
- autenticação com credenciais inválidas

### Testes de API (REST-assured)
Validação dos endpoints REST e persistência no banco H2.

**Cenários testados:**
- cadastro de livro com confirmação de persistência
- exclusão de livro com confirmação de remoção

### Testes End-to-End (Selenium WebDriver)
Simulação do fluxo real do usuário na interface web.

**Cenários testados:**
- cenário de cadastro/exclusão de venda
- cenário de cadastro/exclusão de usuário

## Problemas Comuns

### A API não está retornando dados
1. Verifique se a aplicação está rodando em http://localhost:8080
2. Abra o Console do Navegador (F12) para ver mensagens de erro
3. Verifique se você fez login com sucesso

### CORS Error
- Os CORS já estão configurados para aceitar requisições de qualquer origem

### Banco de Dados não persiste
- O H2 em memória é resetado sempre que a aplicação é reiniciada
- Para usar um banco persistente, altere a configuração em `application.properties`


