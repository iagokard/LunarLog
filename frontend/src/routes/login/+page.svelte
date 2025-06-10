<script>
    import "$lib/styles/login.css";
</script>

<div class="container" id="container">
    <div class="form-container sign-up">
        <form id="registerForm">
            <h1>Cadastro de Funcionário</h1>
            <div class="social-icons">
                <a href="#" class="icon"
                    ><i class="fa-brands fa-google-plus-g"></i></a
                >
                <a href="#" class="icon"
                    ><i class="fa-brands fa-facebook-f"></i></a
                >
                <a href="#" class="icon"><i class="fa-brands fa-github"></i></a>
                <a href="#" class="icon"
                    ><i class="fa-brands fa-linkedin"></i></a
                >
            </div>
            <span>ou use o seu email para se registrar</span>

            <input
                type="text"
                placeholder="CPF (XXX.XXX.XXX-XX)"
                id="cpfRegistro"
            />
            <span class="error-message" id="cpfError"></span>
            <input type="text" placeholder="Nome Completo" id="nomeRegistro" />
            <span class="error-message" id="nomeError"></span>
            <input type="email" placeholder="Email" id="emailRegistro" />
            <span class="error-message" id="emailRegistroError"></span>
            <input
                type="password"
                placeholder="Senha (mínimo 8 caracteres)"
                id="senhaRegistro"
            />
            <span class="error-message" id="senhaRegistroError"></span>
            <input
                type="password"
                placeholder="Confirmar Senha"
                id="confirmarSenhaRegistro"
            />
            <span class="error-message" id="confirmarSenhaError"></span>
            <select id="cargoRegistro">
                <option value="">Selecione o Cargo</option>
                <option value="Admin">Admin</option>
                <option value="Entregador">Entregador</option>
                <option value="Operador">Operador</option>
            </select>
            <span class="error-message" id="cargoError"></span>
            <button type="submit" id="submitRegisterBtn" disabled
                >Registrar</button
            >
        </form>
    </div>

    <div class="form-container sign-in">
        <form id="loginForm">
            <h1>Acesso de Funcionário</h1>
            <div class="social-icons">
                <a href="#" class="icon"
                    ><i class="fa-brands fa-google-plus-g"></i></a
                >
                <a href="#" class="icon"
                    ><i class="fa-brands fa-facebook-f"></i></a
                >
                <a href="#" class="icon"><i class="fa-brands fa-github"></i></a>
                <a href="#" class="icon"
                    ><i class="fa-brands fa-linkedin"></i></a
                >
            </div>
            <span>ou use o seu email e sua senha</span>
            <span class="error-message global-message" id="loginGlobalError"
            ></span> <input type="email" placeholder="Email" id="emailLogin" />
            <span class="error-message" id="emailLoginError"></span>
            <input type="password" placeholder="Senha" id="senhaLogin" />
            <span class="error-message" id="senhaLoginError"></span>
            <a href="#">Esqueceu sua senha?</a>
        </form>
    </div>

    <div class="toggle-container">
        <div class="toggle">
            <div class="toggle-panel toggle-left">
                <h1>Já faz parte da equipe?</h1>
                <p>Bem-vindo de volta!</p>
                <button class="hidden" id="login">Login</button>
            </div>
            <div class="toggle-panel toggle-right">
                <h1>Não tem conta?</h1>
                <p>Venha se juntar à nossa equipe de rastreamento!</p>
                <button class="hidden" id="register"
                    >Registrar Nova Conta</button
                >
            </div>
        </div>
    </div>
</div>

