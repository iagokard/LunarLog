<!--
  Componente Svelte - EnviarPacote.svelte (Versão com Formulário)
  
  Esta versão transforma os painéis de Remetente e Destinatário em formulários
  com inputs, validação de campos obrigatórios e uma estrutura de dados
  pronta para ser enviada a uma API via POST, mantendo o estilo original.
-->
<script lang="ts">
  import { onMount } from "svelte";

  // --- Tipos para organização de dados ---
  type Endereco = {
    nome: string;
    cpf: string;
    email: string;
    telefone: string;
    cep: string;
    logradouro: string;
    numero: string;
    complemento?: string; // Campo opcional
    bairro: string;
    cidade: string;
    estado: string;
  };

  type Dimensoes = {
    peso: number | null;
    altura: number | null;
    largura: number | null;
    comprimento: number | null;
  };
  
  type FreteResponse = {
      valor: number;
      entregaEstimada: string;
  }

  // --- Estados Reativos do Componente ---

  // Objetos para guardar os dados do formulário, prontos para a API
  let remetente: Endereco = { nome: '', cpf: '', email: '', telefone: '', cep: '', logradouro: '', numero: '', complemento: '', bairro: '', cidade: '', estado: '' };
  let destinatario: Endereco = { nome: '', cpf: '', email: '', telefone: '', cep: '', logradouro: '', numero: '', complemento: '', bairro: '', cidade: '', estado: '' };
  
  let dimensoes: Dimensoes = { peso: null, altura: null, largura: null, comprimento: null };
  let tipoEnvio: 'Express' | 'Normal' = 'Express';

  // Estados para controle da API
  let isLoading = false;
  let freteCalculado: FreteResponse | null = null;
  let erroApi: string | null = null;

  // --- Lógica de Negócio ---

  /**
   * Simula o envio do formulário completo.
   */
  const handleEnviarPacote = async () => {
    isLoading = true;
    freteCalculado = null;
    erroApi = null;

    // Simulação de chamada de API com delay de 1.5 segundos
    await new Promise(resolve => setTimeout(resolve, 1500));

    try {
        // Objeto de dados pronto para ser enviado como JSON
        const payload = {
            remetente,
            destinatario,
            dimensoes,
            tipoEnvio
        };

        console.log("Payload para a API:", payload);
        
        // Simulação de cálculo de frete
        const valorFinal = 50 + (dimensoes.peso || 0) * 0.1;
        
        freteCalculado = {
            valor: parseFloat(valorFinal.toFixed(2)),
            entregaEstimada: "Entre 3-5 dias úteis"
        };

    } catch (e) {
        erroApi = "Não foi possível processar o envio. Tente novamente.";
    } finally {
        isLoading = false;
    }
  };

</script>

<!-- Container principal -->
<div class="flex min-h-screen w-full bg-slate-50 font-sans text-gray-800">
  
  <!-- Sidebar de Navegação -->
  <aside class="hidden w-64 flex-col items-center space-y-10 bg-white p-4 shadow-lg md:flex">
    <div class="text-2xl font-bold text-purple-700">LunarLog</div>
    <nav class="w-full">
      <ul class="flex flex-col gap-3">
        <li><a href="#/" class="flex items-center gap-3 rounded-lg p-3 text-gray-500 hover:bg-gray-100 hover:text-purple-700"><svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M4.25 2A2.25 2.25 0 0 0 2 4.25v2.5A2.25 2.25 0 0 0 4.25 9h2.5A2.25 2.25 0 0 0 9 6.75v-2.5A2.25 2.25 0 0 0 6.75 2h-2.5ZM2 13.25A2.25 2.25 0 0 1 4.25 11h2.5A2.25 2.25 0 0 1 9 13.25v2.5A2.25 2.25 0 0 1 6.75 18h-2.5A2.25 2.25 0 0 1 2 15.75v-2.5ZM11 4.25A2.25 2.25 0 0 1 13.25 2h2.5A2.25 2.25 0 0 1 18 4.25v2.5A2.25 2.25 0 0 1 15.75 9h-2.5A2.25 2.25 0 0 1 11 6.75v-2.5ZM13.25 11A2.25 2.25 0 0 0 11 13.25v2.5A2.25 2.25 0 0 0 13.25 18h2.5A2.25 2.25 0 0 0 18 15.75v-2.5A2.25 2.25 0 0 0 15.75 11h-2.5Z" clip-rule="evenodd" /></svg><span class="font-medium">Painel</span></a></li>
        <li><a href="#/" class="flex items-center gap-3 rounded-lg bg-purple-600 p-3 font-semibold text-white shadow-md"><svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor"><path d="M3.5 3A1.5 1.5 0 0 0 2 4.5v2.766a2.5 2.5 0 0 0 1.413 2.263A2.5 2.5 0 0 1 6.5 11h7a2.5 2.5 0 0 1 2.5 2.5v.234a2.5 2.5 0 0 0 1.413-2.263V4.5A1.5 1.5 0 0 0 16.5 3h-13Z" /><path d="M3 10.5A1.5 1.5 0 0 1 4.5 9h11a1.5 1.5 0 0 1 1.5 1.5v4A1.5 1.5 0 0 1 15.5 15h-.691a2.502 2.502 0 0 0-2.31 2h-4a2.502 2.502 0 0 0-2.31-2H4.5a1.5 1.5 0 0 1-1.5-1.5v-4Z" /></svg><span>Enviar Pacotes</span></a></li>
      </ul>
    </nav>
  </aside>

  <!-- Conteúdo Principal com formulário -->
  <main class="w-full flex-1 p-4 sm:p-6 lg:p-10">
    <form on:submit|preventDefault={handleEnviarPacote}>
        <h1 class="mb-8 text-center text-3xl font-bold text-gray-800 lg:text-left lg:text-4xl">Enviar Pacote</h1>

        <!-- Seção de Remetente e Destinatário com Inputs -->
        <div class="grid grid-cols-1 gap-8 lg:grid-cols-2">
          
          <!-- Card Remetente -->
          <div class="rounded-xl bg-white p-6 shadow-md">
            <h2 class="mb-6 text-xl font-bold text-gray-700">Remetente</h2>
            <div class="grid grid-cols-1 gap-x-4 gap-y-3 sm:grid-cols-2">
                <input bind:value={remetente.nome} type="text" placeholder="Nome Completo" required class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400 sm:col-span-2" />
                <input bind:value={remetente.cpf} type="text" placeholder="CPF" required class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400" />
                <input bind:value={remetente.cep} type="text" placeholder="CEP" required class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400" />
                <input bind:value={remetente.email} type="email" placeholder="E-mail" required class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400 sm:col-span-2" />
                <input bind:value={remetente.logradouro} type="text" placeholder="Endereço" required class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400 sm:col-span-2" />
                <input bind:value={remetente.numero} type="text" placeholder="Número" required class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400" />
                <input bind:value={remetente.complemento} type="text" placeholder="Complemento (Opcional)" class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400" />
            </div>
          </div>

          <!-- Card Destinatário -->
          <div class="rounded-xl bg-white p-6 shadow-md">
             <h2 class="mb-6 text-xl font-bold text-gray-700">Destinatário</h2>
             <div class="grid grid-cols-1 gap-x-4 gap-y-3 sm:grid-cols-2">
                <input bind:value={destinatario.nome} type="text" placeholder="Nome Completo" required class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400 sm:col-span-2" />
                <input bind:value={destinatario.cpf} type="text" placeholder="CPF" required class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400" />
                <input bind:value={destinatario.cep} type="text" placeholder="CEP" required class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400" />
                <input bind:value={destinatario.email} type="email" placeholder="E-mail" required class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400 sm:col-span-2" />
                <input bind:value={destinatario.logradouro} type="text" placeholder="Endereço" required class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400 sm:col-span-2" />
                <input bind:value={destinatario.numero} type="text" placeholder="Número" required class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400" />
                <input bind:value={destinatario.complemento} type="text" placeholder="Complemento (Opcional)" class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400" />
            </div>
          </div>
        </div>

        <div class="my-8 flex justify-center">
  <div class="inline-flex rounded-full bg-white p-1 shadow-inner">
    <select class="cursor-pointer appearance-none rounded-full bg-purple-600 px-4 py-2 text-sm font-semibold text-white transition-colors hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2">
      <option value="normal">Envio Normal</option>
      <option value="expresso">Envio Expresso</option>
    </select>
  </div>
</div>
        
        <!-- Seção de Dimensões e Valor do Frete -->
        <div class="mt-8 grid grid-cols-1 items-start gap-8 lg:grid-cols-3">
            <div class="rounded-xl bg-white p-6 shadow-md lg:col-span-2">
                <div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
                    <input type="number" placeholder="Peso (g)" bind:value={dimensoes.peso} required class="w-full rounded-lg border-gray-200 bg-slate-100 py-3 pl-4 pr-4 text-sm focus:border-purple-400 focus:ring-purple-400" />
                    <input type="number" placeholder="Altura (cm)" bind:value={dimensoes.altura} required class="w-full rounded-lg border-gray-200 bg-slate-100 py-3 pl-4 pr-4 text-sm focus:border-purple-400 focus:ring-purple-400" />
                    <input type="number" placeholder="Largura (cm)" bind:value={dimensoes.largura} required class="w-full rounded-lg border-gray-200 bg-slate-100 py-3 pl-4 pr-4 text-sm focus:border-purple-400 focus:ring-purple-400" />
                    <input type="number" placeholder="Comprimento (cm)" bind:value={dimensoes.comprimento} required class="w-full rounded-lg border-gray-200 bg-slate-100 py-3 pl-4 pr-4 text-sm focus:border-purple-400 focus:ring-purple-400" />
                </div>
            </div>

            <!-- Card de Submissão -->
            <div class="rounded-xl bg-white p-6 text-center shadow-md">
              {#if erroApi}
                  <div class="text-red-500">{erroApi}</div>
              {:else if freteCalculado}
                  <h3 class="font-semibold text-gray-500">Envio Calculado!</h3>
                  <p class="my-2 text-4xl font-bold text-gray-800">R$ {freteCalculado.valor.toFixed(2).replace('.', ',')}</p>
                  <p class="mb-4 text-sm text-gray-500">{freteCalculado.entregaEstimada}</p>
              {:else}
                   <h3 class="font-semibold text-gray-500">Tudo pronto?</h3>
                   <p class="my-4 text-gray-400">Preencha todos os campos para enviar o pacote.</p>
              {/if}
              <button type="submit" class="flex w-full items-center justify-center rounded-lg bg-purple-600 py-3 text-base font-bold text-white shadow-md transition hover:bg-purple-700 disabled:cursor-not-allowed disabled:bg-purple-400" disabled={isLoading}>
                {#if isLoading}
                  <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
                  Processando...
                {:else}
                  Enviar Pacote
                {/if}
              </button>
            </div>
        </div>
    </form>
  </main>
</div>
