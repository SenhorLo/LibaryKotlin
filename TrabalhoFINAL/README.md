# Biblioteca Digital - Projeto Final Mobile II

Este é o projeto final da disciplina de Desenvolvimento Mobile II. Trata-se de um sistema de gerenciamento e aluguel de livros que integra persistência local com Room e consumo de API remota com Retrofit.

## 👥 Integrantes da Dupla
* [Nome do Aluno 1]
* [Nome do Aluno 2]

## 🚀 Proposta do Projeto
O aplicativo permite que administradores gerenciem um acervo local de livros e que usuários comuns possam buscar livros na Google Books API, adicioná-los à biblioteca e realizar o aluguel/devolução.

## 🛠 Tecnologias Utilizadas
* **Kotlin**: Linguagem principal.
* **Room**: Persistência de dados local (SQLite).
* **Retrofit & Gson**: Consumo da Google Books API.
* **Glide**: Carregamento de imagens (capas dos livros).
* **ViewBinding**: Interação segura com os layouts XML.
* **ViewModel & LiveData**: Arquitetura MVVM para gerenciamento de estado.
* **Fragments & TabLayout**: Navegação moderna na área do usuário.

## 🏗 Arquitetura Adotada
O projeto segue o padrão **MVVM (Model-View-ViewModel)** com o uso de um **Repository** para mediar a comunicação entre a fonte de dados local (Room) e remota (Retrofit).

## 🔑 Instruções de Execução
1. Faça o clone do repositório.
2. Abra o projeto no **Android Studio (Iguana ou superior)**.
3. Sincronize o Gradle.
4. Execute em um Emulador ou dispositivo físico (API 24+).
5. **Acesso Admin**: 
   * Usuário: `admin`
   * Senha: `admin`
6. **Acesso Usuário**: Qualquer outro nome/senha (Simulado).

## 📸 Funcionalidades Principais
* **Login**: Tela inicial com autenticação simulada.
* **Admin**: CRUD completo (Adicionar, Listar, Editar e Remover).
* **Usuário - Biblioteca**: Listagem de livros locais com opção de Alugar/Devolver.
* **Usuário - Buscar**: Integração com Google Books API para busca em tempo real e importação.
