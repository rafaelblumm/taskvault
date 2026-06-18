# Situação: 1. Usuários
## **Caso de uso:** 1.1 Cadastro de Usuário
### Informações Gerais
- **Atores:** Administrador padrão ou de sistema
- **Pré-Condições:** Tiver efetuado login de um administrador
- **Pós-Condições:** Novo usuário cadastrado no sistema

---

## Fluxo Principal

| Ações do Ator | Resposta do Sistema |
|:---:|:---:|
| 1. O ator seleciona "Cadastrar Usuário" no menu de ações de usuário |  |
|   |2. Sistema apresenta tela de cadastro de usuários com dados para preencher |
|3. O ator preenche os dados do usuário e solicita gravação: nome de usuário; nome; senha; e-mail e cargo (administrador, Administrador do sistema, usuário padrão ou visitante) | |
|  | 4. Sistema registra o usuário no sistema |

---

## Fluxos Alternativos

### Alternativa 1
- No passo **4**, antes de efetuar a gravação:
  - O sistema valida os campos de e-mail e nome de usuário, previnindo a criação de integrantes com mesmos dados.

---

## Regras de Negócio
- O e-mail e nome de usuário devem ser único.
- Apenas administradores podem cadastrar usuários.

---

## **Caso de uso:** 1.2 Obter informações de um usuário
### Informações Gerais
- **Atores:** Qualquer cargo disponível
- **Pré-Condições:** Tiver efetuado um login
- **Pós-Condições:** Apresentar na tela as informações do usuário solicitado

---

## Fluxo Principal

| Ações do Ator | Resposta do Sistema |
|:---:|:---:|
| 1. O ator seleciona "Obter informações de um usuário" no menu de ações de usuário |  |
|   |2. Sistema solicita um dado único do banco de dados de usuários (nesse caso, nome de usuário) |
|3. O ator preenche os dados do usuário e solicita informação | |
|  | 4. Sistema mostra todos dados cadastrados referentes ao nome de usuário recebido |

---

## Fluxos Alternativos

### Alternativa 1
- No passo **4**, antes de efetuar a mostra dos dados:
  - O sistema valida existência do usuário solicitado, retornando um aviso caso não exista.

---

## Regras de Negócio
- Usuários inativos (deletados logicamente) não podem ter suas informações acessadas.

---

## **Caso de uso:** 1.3 Atualizar informações de um usuário
### Informações Gerais
- **Atores:** Administradores, ou usuários que solicitem a atualização de suas próprias informações
- **Pré-Condições:** Tiver efetuado um login cadastrado com cargo de Administrador ou usuário
- **Pós-Condições:** Dados do usuário são atualizados no banco de dados

---

## Fluxo Principal

| Ações do Ator | Resposta do Sistema |
|:---:|:---:|
| 1. O ator seleciona "Atualizar informações de um usuário" no menu de ações de usuário |  |
|   |2. Sistema apresenta dados que podem ser alterados |
|3. O ator preenche os dados de necessidade e solicita gravação: nome, senha, e-mail e cargo (administrador, administrador do sistema, usuário padrão ou visitante) |
|  | 4. Sistema grava a atualização no banco de dados |

---

## Fluxos Alternativos

### Alternativa 1
- No passo **2**, quando o usuário solicitar atualização:
  - O sistema verifica o cargo e a relação do ator com o usuário:
    - Caso seja administrador: Acesso à alteração de qualquer usuário e alteração de cargo.
    - Caso seja usuário padrão: Somente acesso à alteração de seus próprios dados
    - Caso o ator seja o próprio usuário: Acesso à alteração de senha.

### Alternativa 2
- No passo **4**, antes de efetuar a atualização:
  - O sistema valida existência do e-mail, para garantir que não exista repetição do mesmo, retornando um aviso.

---

## Regras de Negócio
- Usuários inativos (deletados logicamente) não podem ter suas informações atualizadas.
- Somente administradores podem atualizar informações de outros integrantes, assim como alterar cargos.
- A alteração de senha somente é permitida quando a requisição é de atualização de dados próprios.

---

## **Caso de uso:** 1.4 Remoção de um usuário
### Informações Gerais
- **Atores:** Administradores
- **Pré-Condições:** Tivier efetuado login de um administrador
- **Pós-Condições:** Inativação do usuário

---

## Fluxo Principal

| Ações do Ator | Resposta do Sistema |
|:---:|:---:|
| 1. O ator seleciona "Remover um usuário" no menu de ações de usuário |  |
|   |2. Sistema solicita nome de usuário do cadastro para identificar remoção |
|3. O ator preenche os dados e solicita remoção, preenchendo um campo de senha para evitar deleção acidental |
|  | 4. Sistema desativa o usuário |

---

## Fluxos Alternativos

### Alternativa 1
- No passo **3**, quando o usuário entrar na pagina de remoção:
  - O sistema verifica se o cargo é de administrador, não autorizando a remoção por parte de qualquer outro cargo.

### Alternativa 2
- No passo **4**, antes de efetuar a remoção:
  - O sistema valida existência do usuário, retornando um aviso caso não exista.

---

## Regras de Negócio
- Usuários inativos (deletados logicamente) não podem ser identificados.
- Somente administradores podem remover outros integrantes.

---

# Situação: 2. Tarefas
## **Caso de uso:** 2.1 Criação de tarefa
### Informações Gerais
- **Atores:** Qualquer cargo exceto vistante
- **Pré-Condições:** Tiver efetuado login de um usuário que não seja visitante
- **Pós-Condições:** Nova tarefa cadastrada no sistema

---

## Fluxo Principal

|                                                      Ações do Ator                                                      |                           Resposta do Sistema                           |
|:-----------------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------:|
|                                     1. O ator seleciona "Criar" no menu de tarefas                                      |                                                                         |
|                                                                                                                         | 2. Sistema apresenta tela de criação de tarefa com dados para preencher |
| 3. O ator preenche os dados da tarefa e solicita gravação: Título; descrição; status; usuário destinado e data prevista |                                                                         |
|                                                                                                                         |                 4. Sistema registra a tarefa no sistema                 |

---

## Fluxos Alternativos

### Alternativa 1
- No passo **4**, antes de efetuar a gravação:
  - O sistema valida o campo de usuário destinado, previnindo a criação de tarefas destinadas a usuários não existentes ou inativos.

---

## Regras de Negócio
- Usuário destinado deve estar presente e ativo no banco de dados.
- Visitantes não podem criar tarefas.

---

## **Caso de uso:** 2.2 Procura de tarefa
### Informações Gerais
- **Atores:** Qualquer cargo
- **Pré-Condições:** Tiver efetuado login
- **Pós-Condições:** Apresentação na tela com todas as tarefas que satisfazem as condições

---

## Fluxo Principal

|                                                                                   Ações do Ator                                                                                    |                         Resposta do Sistema                         |
|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------:|
|                                               1. O ator seleciona "filtros avançados", ou apenas a barra de busca no menu de tarefas                                               |                                                                     |
|                                                                                                                                                                                    |  2. Sistema requer preenchimento da barra de busca, ou dos filtros  |
| 3. O ator escreve o nome da tarefa, ou preenche os dados da tarefa e solicita procura: título; status; criador; responsável; criado antes/depois de; data prevista antes/depois de |                                                                     |
|                                                                                                                                                                                    | 4. Sistema mostra na tela todas tarefas que satisfazem as condições |

---

## Fluxos Alternativos

### Sem fluxos alternativos

---

## Regras de Negócio
- Tarefas deletadas não podem ser acessadas
- Comentários são conectados a tarefas. Ou seja, ao acessar uma tarefa, a procura de comentários é executada.

---

## **Caso de uso:** 2.3 Edição de tarefa
### Informações Gerais
- **Atores:** Administrador ou o próprio usuário que criou a tarefa
- **Pré-Condições:** Tiver efetuado login como administrador ou ser o criador da tarefa
- **Pós-Condições:** Atualização dos dados da tarefa no banco de dados

---

## Fluxo Principal

|                                                        Ações do Ator                                                         |                        Resposta do Sistema                        |
|:----------------------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------:|
|                         1. O ator seleciona uma tarefa para visualizar, e depois clica em "alterar"                          |                                                                   |
|                                                                                                                              | 2. Sistema requer preenchimento dos dados que podem ser alterados |
| 3. O ator preenche os dados da tarefa e solicita atualização: título; descrição; status; usuário responsável e data prevista |                                                                   |
|                                                                                                                              |           4. Sistema atualiza no sistema os dados novos           |

---

## Fluxos Alternativos

### Alternativa 1
- No passo **4**, antes de efetuar a atualização:
  - O sistema valida a existência do novo usuário responsável, retornando erro caso não o encontre.

---

## Regras de Negócio
- Tarefas deletadas não podem ser atualizadas
- Usuários inativos não podem ser designados para novas tarefas
- Somente administradores podem editar tarefas de outros usuários

---

## **Caso de uso:** 2.4 Deleção de tarefa
### Informações Gerais
- **Atores:** Administrador ou o próprio usuário que criou a tarefa
- **Pré-Condições:** Tiver efetuado login como administrador ou ser o criador da tarefa
- **Pós-Condições:** Tarefa marcada como inativa pelo banco de dados

---

## Fluxo Principal

|                                Ações do Ator                                |            Resposta do Sistema            |
|:---------------------------------------------------------------------------:|:-----------------------------------------:|
| 1. O ator seleciona uma tarefa para visualizar, e depois clica em "deletar" |                                           |
|                                                                             | 2. Sistema requer confirmação da deleção  |
|                         3. O ator faz a confirmação                         |                                           |
|                                                                             | 4. Sistema executa a inativação da tarefa |

---

## Fluxos Alternativos

### Sem fluxos alternativos

---

## Regras de Negócio
- Somente administradores podem deletar tarefas de outros usuários

---

# Situação: 3. Comentários
## **Caso de uso:** 3.1 Criação de comentário
### Informações Gerais
- **Atores:** Qualquer cargo exceto visitante
- **Pré-Condições:** Tiver efetuado login como qualquer cargo exceto visitante
- **Pós-Condições:** Comentário criado, conectado à tarefa

---

## Fluxo Principal

|                                Ações do Ator                                 |                  Resposta do Sistema                  |
|:----------------------------------------------------------------------------:|:-----------------------------------------------------:|
| 1. O ator seleciona uma tarefa para visualizar e seleciona "novo comentário" |                                                       |
|                                                                              |      2. Sistema requer preenchimento de um texto      |
|             3. O ator escreve o necessário e seleciona "comentar"             |                                                       |
|                                                                              | 4. Sistema salva comentário para todos usuários verem |

---

## Fluxos Alternativos

### Sem fluxos alternativos

---

## Regras de Negócio
- Tarefas inativas não podem receber novos comentários

---
## **Caso de uso:** 3.2 Deleção de comentário
### Informações Gerais
- **Atores:** Somente o criado da tarefa, ou administradores
- **Pré-Condições:** Tiver efetuado login como o criado da tarefa, ou administrador
- **Pós-Condições:** Comentário deletado

---

## Fluxo Principal

|                              Ações do Ator                               |                 Resposta do Sistema                  |
|:------------------------------------------------------------------------:|:----------------------------------------------------:|
| 1. O ator seleciona uma tarefa para visualizar e procura a tarefa criada |                                                      |
|                                                                          | 2. Sistema apresenta um botão para deleção da tarefa |
|                   3. O ator seleciona o botão "deletar"                   |                                                      |
|                                                                          |            4. Sistema deleta o comentário            |

---

## Fluxos Alternativos

### Sem fluxos alternativos

---

## Regras de Negócio
- Tarefas deletadas não podem ser vistas por nenhum usuário.
- Quando uma tarefa é deletada, todos os comentários conectados à ela são também excluidos.

---

# Situação: 4. Inicialização e encerramento da sessão
## **Caso de uso:** 4.1 Login
### Informações Gerais
- **Atores:** Qualquer usuário
- **Pré-Condições:** Ter seus dados cadastrados no banco de dados
- **Pós-Condições:** Acesso às funcionalidades da APi

---

## Fluxo Principal

|                      Ações do Ator                      |                     Resposta do Sistema                      |
|:-------------------------------------------------------:|:------------------------------------------------------------:|
|       1. O ator entra no servidor pelo navegador        |                                                              |
|                                                         | 2. Sistema apresenta um formulário de preenchimento de dados |
| 3. O ator preenche os dados com nome de usuário e senha |                                                              |
|                                                         |         4. Sistema concede acesso às funcionalidades         |

---

## Fluxos Alternativos

### Sem fluxos alternativos

---

## Regras de Negócio
- Para efetuar um login, é preciso que um administrador tenha cadastrado o usuário no sistema.
- Usuários que foram inativados não podem mais efetuar login.

---

## **Caso de uso:** 4.2 Logout
### Informações Gerais
- **Atores:** Qualquer usuário
- **Pré-Condições:** Ter efetuado login
- **Pós-Condições:** termino da sessão

---

## Fluxo Principal

|                         Ações do Ator                         |             Resposta do Sistema              |
|:-------------------------------------------------------------:|:--------------------------------------------:|
| 1. O ator clica na opção "sair", a qualquer momento da sessão |                                              |
|                                                               |   2. Sistema finaliza a sessão do usuário    |

---

## Fluxos Alternativos

### Sem fluxos alternativos

---

## Regras de Negócio

### Sem regras de negócio

---