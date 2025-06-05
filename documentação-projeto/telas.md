## Telas do Projeto LunarLog

### 1. Tela de Login (Pública)

**Descrição**: Acesso para funcionários da agência.

**Elementos**:

- Título "Acesso de Funcionário"

- Campo de Email (obrigatório, formato de email)

- Campo de Senha (obrigatório)

- Botão "Entrar" (inicialmente desativado até que ambos os campos sejam preenchidos e válidos)

- Link "Registrar nova conta"

**Comportamentos**:

- Se o email não for válido, exibir mensagem abaixo do campo: "Por favor, insira um email válido".

- Se o login falhar, exibir mensagem geral acima dos campos: "Email ou senha incorretos".

- Botão "Entrar" fica ativo quando ambos os campos estão preenchidos e o email é válido.

---

### 2. Tela de Registro (Pública)

**Descrição**: Cadastro de novos funcionários

**Elementos**:

- Título: "Cadastro de Funcionário"

- Campo CPF (obrigatório, formato XXX.XXX.XXX-XX, máscara automática)

- Campo Nome Completo (obrigatório)

- Campo Email (obrigatório, formato de email)

- Campo Senha (obrigatório, mínimo de 8 caracteres)

- Campo Confirmar Senha (obrigatório, deve ser igual ao campo Senha)

- Dropdown "Cargo" (obrigatório, opções: Admin, Entregador, Operador)

- Botão "Registrar" (inicialmente desativado)

- Link "Já tenho conta" (leva para a tela de login)

**Comportamentos**:

- Validação em tempo real:

- CPF: máscara automática e validação do formato. Se inválido, mensagem: "CPF inválido".

- Email: se inválido, mensagem: "Por favor, insira um email válido".

- Senha: mínimo 8 caracteres. Se menor, mensagem: "A senha deve ter pelo menos 8 caracteres".

- Confirmar Senha: deve ser igual à senha. Se diferente, mensagem: "As senhas não coincidem".

- Botão "Registrar" só fica ativo quando todos os campos são válidos e preenchidos.

- Após registro bem-sucedido, redireciona para a tela de login com mensagem: "Registro realizado com sucesso! Faça login."

---

### 3. Dashboard (Após Login)

**Descrição**: Tela inicial após login, com menu de navegação baseado no cargo do funcionário.

**Elementos**:

- Barra lateral (menu) com opções baseadas no cargo:

- Admin: Todas as opções.

- Operador: "Enviar Pacote", "Gerenciar Envios", "Atualizar Status".

- Entregador: "Gerenciar Entregas".

- Conteúdo principal: Mensagem de boas-vindas e resumo das ações disponíveis.

- Cabeçalho com nome do funcionário e botão "Sair".

---

### 4. Enviar Pacote (Operador/Admin)

**Descrição**: Tela para registrar um novo envio de pacote.

**Elementos**:

- Três seções: Remetente, Destinatário, Pacote.

- **Seção Remetente**:

- Campo CPF (obrigatório, com máscara)

- Campos: Nome Completo, Email, Telefone, Endereço (CEP, Número, Complemento).

- **Seção Destinatário**: mesma estrutura do remetente.

- **Seção Pacote**:

- Dimensões (Altura, Largura, Comprimento em cm) - obrigatórios, numéricos.

- Peso (em gramas) - obrigatório, numérico.

- Dropdown "Tipo de Envio" (obrigatório, opções carregadas do banco).

- Campo "Valor do Frete" (não editável, calculado automaticamente).

- Botão "Enviar" (inicialmente desativado até a complitude dos campos).

**Comportamentos**:

- Ao digitar o CEP, buscar endereço via BrasilAPI e preencher automaticamente (logradouro, bairro, cidade, estado). O usuário só precisa confirmar e preencher o número e complemento.

- O cálculo do frete é automático ao preencher os campos necessários (dimensões, peso, CEPs de origem e destino). Se houver isenção, mostrar "Frete Isento".

- Validação: Todos os campos obrigatórios devem ser preenchidos e válidos.

- Botão "Enviar" só fica ativo quando todas as seções estão válidas.

- Após envio bem-sucedido, esconder conteúdo e exibir:

- Código de rastreio (grande, destacado)

- Palavra-chave (grande, destacada)

- Botão "Copiar" ao lado de cada

- Botão "Fechar"

---

### 5. Gerenciar Envios (Operador/Admin)

**Descrição**: Tela para consultar e gerenciar todos os envios (equivalente à consulta de status com dados internos).

**Elementos**:

- Campo de busca por código de rastreio (opcional, mas se preenchido busca o envio específico).

- Tabela com lista de envios (código, remetente, destinatário, status atual, data de envio).

- Cada linha tem botão "Ver Detalhes".

- Botão "Novo Envio" (leva para a tela de enviar pacote).

**Comportamentos**:

- Se o campo de busca for preenchido, filtra a tabela para o envio correspondente.

- Ao clicar em "Ver Detalhes", abre um modal com:

- Todos os dados do envio (remetente, destinatário, pacote, histórico de status, palavra-chave).

- Botão "Atualizar Status" (leva para a tela de atualizar status com o código preenchido).

---

### 6. Atualizar Status (Operador/Admin)

**Descrição**: Tela para registrar a chegada do pacote em um novo local ou marcar como perdido.

**Elementos**:

- Campo "Código de Rastreio" (obrigatório).

- Botão "Buscar" (para carregar os dados do envio).

- Seção de informações do envio (só aparece após busca):

- Remetente, Destinatário, Status Atual.

- Seção "Novo Status":

- Dropdown "Ação" (obrigatório, opções: "Registrar Chegada", "Pacote Perdido").

- Se "Registrar Chegada":

- Campos para o "Local Atual":	cep
		                        número (do endereço)
	                        	complemento (opcional)
		                        Nome do local (ex: Centro de distribuições de encomendas de Salvador)

- Campos do "Próximo Local" (mesmos campos do local atual).

- Se "Pacote Perdido": sem campos adicionais.

- Botão "Atualizar" (inicialmente desativado).

**Comportamentos**:

- Após buscar o envio, exibir informações resumidas.

- O botão "Atualizar" só fica ativo quando a ação é selecionada e, se necessário, os campos adicionais estão preenchidos.

- Após atualização, exibir mensagem de sucesso e enviar e-mail para remetente/destinatário.

---

### 7. Gerenciar Entregas (Entregador/Admin)

**Descrição**: Tela para entregadores registrarem a entrega (entregue, destinatário ausente).

**Elementos**:

- Campo "Código de Rastreio" (obrigatório).

- Botão "Buscar" (para carregar os dados do envio).

- Seção de informações do envio (só aparece após busca): Remetente, Destinatário, Endereço de Entrega, Palavra-chave (visível).

- Botões de ação:

- "Entregue" (verde)

- "Destinatário Ausente" (amarelo)

- "Cancelar" (cinza, limpa a busca)

**Comportamentos**:

- Ao buscar o envio, exibir os dados e os botões de ação.

- Ao clicar em "Entregue" ou "Destinatário Ausente", abrir modal de confirmação.

- Após confirmação, atualizar o status e recarregar tela

---

### 8. Consulta de Status (Pública)

**Descrição**: Tela pública para rastrear um pacote.

**Elementos**:

- Campo "Código de Rastreio" (obrigatório).

- Botão "Rastrear" (inicialmente desativado, ativa quando o campo tem 10 caracteres).

- Resultado (só aparece após busca):

- Título: "Status do Pacote"

- Lista de histórico (do mais recente para o mais antigo):

- Cada item: data, local atual, próximo local, status (ex: "Em trânsito", "Entregue").

- Se não encontrado, mensagem: "Nenhum pacote encontrado com este código."

---

### 9. Calculadora de Frete (Pública)

**Descrição**: Tela pública para calcular o frete de um envio.

**Elementos**:

- Dropdown "Tipo de Envio" (obrigatório).

- Dimensões (Altura, Largura, Comprimento em cm) - obrigatórios.

- Peso (em gramas) - obrigatório.

- CEP Origem (obrigatório, máscara) e CEP Destino (obrigatório, máscara).

- Botão "Calcular" (inicialmente desativado).

- Resultado:

- "Valor do Frete: R$ X,XX" ou "Frete Isento".

**Comportamentos**:

- Botão "Calcular" só ativa quando todos os campos obrigatórios estão preenchidos.

- Ao calcular, mostrar o resultado abaixo do botão.

---

### 10. Sobre Nós (Pública)

**Descrição**: Tela com informações do projeto e equipe.

**Elementos**:

- Título: "LunarLog - Rastreamento de Encomendas"

- Texto descritivo do projeto (objetivo, simulação).

- Lista da stack:

- Backend: Java, Spring Boot

- Frontend: Svelte

- Database: PostgreSQL

- Seção "Equipe":

- Para cada membro: nome, link para GitHub e LinkedIn.

---

### Regras Gerais de Interface

- **Validação**: Sempre que um campo obrigatório estiver vazio ou inválido, exibir mensagem abaixo do campo em vermelho.

- **Botões**: Sempre desativados até que os campos obrigatórios da tela sejam preenchidos e válidos.

- **Mensagens de Sucesso/Erro**: Usar toasts ou modais para feedback de ações.

- **Formatação**:

- Valores monetários: R$ 0,00 (ex: 30500 centavos → R$ 305,00).

- Peso: gramas → kg (ex: 300g → 0,3 kg).

- Distância: metros → km (ex: 1500m → 1,5 km).
