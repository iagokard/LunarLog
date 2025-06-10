<!--
  Componente Svelte - EnviarPacote.svelte (Versão funcional com API)
-->
<script lang="ts">
  import { onMount } from "svelte";
  import type {
    ShipmentType,
    TomTomGeocodeResult,
    TomTomRouteResult,
    FreightCalculation,
  } from "$lib/types/freight";

  // Configurações
  const apiAddr = import.meta.env.VITE_API_ADDR || "http://localhost:8080";
  const tomtomKey = import.meta.env.VITE_TOMTOM_API_KEY;

  // Estados reativos
  let shipmentTypes: ShipmentType[] = [];
  let selectedShipmentType: ShipmentType | null = null;

  let remetente = {
    nome: "",
    cpf: "",
    email: "",
    phone: "",
    cep: "",
    logradouro: "",
    numero: "",
    complemento: "",
    bairro: "",
    cidade: "",
    estado: "",
  };

  let destinatario = {
    nome: "",
    cpf: "",
    email: "",
    phone: "",
    cep: "",
    logradouro: "",
    numero: "",
    complemento: "",
    bairro: "",
    cidade: "",
    estado: "",
  };

  let dimensoes = {
    peso: null,
    altura: null,
    largura: null,
    comprimento: null,
    descricao: "",
  };

  let freteCalculado: FreightCalculation | null = null;
  let erroApi: string | null = null;
  let isLoading = false;
  let cepError = { remetente: false, destinatario: false };
  let showErrorModal = false;
  let showSuccessModal = false;
  let shipmentResponse: { trackingCode: string; keyword: string } | null = null;

  // Buscar tipos de envio ao montar o componente
  onMount(async () => {
    try {
      const response = await fetch(`${apiAddr}/public/envios/tipo`);
      if (response.ok) {
        const data = await response.json();
        shipmentTypes = data.shipmentTypes;
        if (shipmentTypes.length > 0) {
          selectedShipmentType = shipmentTypes[0];
        }
      } else {
        throw new Error("Falha ao carregar tipos de envio");
      }
    } catch (err) {
      erroApi = "Não foi possível carregar os tipos de envio";
      showErrorModal = true;
    }
  });

  // Função para buscar endereço pelo CEP (BrasilAPI)
  async function buscarEnderecoPorCEP(
    cep: string,
    tipo: "remetente" | "destinatario",
  ) {
    if (cep.length !== 8) return;

    try {
      const response = await fetch(
        `https://brasilapi.com.br/api/cep/v2/${cep}`,
      );
      if (!response.ok) {
        throw new Error("CEP não encontrado");
      }

      const data = await response.json();
      const endereco = {
        logradouro: data.street || "",
        bairro: data.neighborhood || "",
        cidade: data.city || "",
        estado: data.state || "",
      };

      if (tipo === "remetente") {
        remetente = { ...remetente, ...endereco };
        cepError.remetente = false;
      } else {
        destinatario = { ...destinatario, ...endereco };
        cepError.destinatario = false;
      }
    } catch (error) {
      if (tipo === "remetente") {
        cepError.remetente = true;
      } else {
        cepError.destinatario = true;
      }
    }
  }

  // Função para geocodificar um CEP
  const geocodeCep = async (
    cep: string,
  ): Promise<{ lat: number; lon: number; address: string }> => {
    const response = await fetch(
      `https://api.tomtom.com/search/2/geocode/${cep}.json?key=${tomtomKey}`,
    );

    if (!response.ok) {
      throw new Error("CEP não encontrado");
    }

    const data: TomTomGeocodeResult = await response.json();

    if (!data.results || data.results.length === 0) {
      throw new Error(`CEP: ${cep} não encontrado`);
    }

    return {
      lat: data.results[0].position.lat,
      lon: data.results[0].position.lon,
      address: data.results[0].address.freeformAddress,
    };
  };

  // Função para calcular a rota
  let arrivalTimeInSeconds: number = 0;
  const calculateRoute = async (
    start: { lat: number; lon: number },
    end: { lat: number; lon: number },
  ): Promise<number> => {
    const response = await fetch(
      `https://api.tomtom.com/routing/1/calculateRoute/${start.lat},${start.lon}:${end.lat},${end.lon}/json?key=${tomtomKey}`,
    );

    if (!response.ok) {
      throw new Error("Não foi possível calcular a rota");
    }

    const data: TomTomRouteResult = await response.json();

    if (!data.routes || data.routes.length === 0) {
      throw new Error("Rota não encontrada");
    }

    arrivalTimeInSeconds = data.routes[0].summary.travelTimeInSeconds;
    return data.routes[0].summary.lengthInMeters;
  };

  // Função para calcular o frete automaticamente
  $: {
    if (
      selectedShipmentType &&
      remetente.cep &&
      remetente.cep.length === 8 &&
      destinatario.cep &&
      destinatario.cep.length === 8 &&
      dimensoes.peso > 0 &&
      dimensoes.altura > 0 &&
      dimensoes.largura > 0 &&
      dimensoes.comprimento > 0
    ) {
      calcularFrete();
    } else {
      freteCalculado = null;
    }
  }

  async function calcularFrete() {
    if (!selectedShipmentType) return;

    try {
      // Obter coordenadas dos CEPs
      const [senderCoords, receiverCoords] = await Promise.all([
        geocodeCep(remetente.cep),
        geocodeCep(destinatario.cep),
      ]);

      // Calcular distância
      const distance = await calculateRoute(
        { lat: senderCoords.lat, lon: senderCoords.lon },
        { lat: receiverCoords.lat, lon: receiverCoords.lon },
      );

      // Calcular volume em cm³
      const volume =
        dimensoes.altura * dimensoes.largura * dimensoes.comprimento;

      // Aplicar isenções
      const effectiveWeight = Math.max(
        0,
        dimensoes.peso - selectedShipmentType.maxWeightExemption,
      );
      const effectiveDistance = Math.max(
        0,
        distance - selectedShipmentType.maxDistanceExemption,
      );
      const effectiveVolume = Math.max(
        0,
        volume - selectedShipmentType.maxVolumeExemption,
      );

      // Calcular frete
      let freightValue =
        effectiveWeight * (selectedShipmentType.freightPerWeight / 1000) +
        effectiveDistance * (selectedShipmentType.freightPerDistance / 100000) +
        effectiveVolume * (selectedShipmentType.freightPerVolume / 1000);

      // Garantir valor mínimo
      freightValue = Math.max(0, freightValue);

      // Calcular data de entrega
      const now = new Date();
      const deliveryDate = new Date(
        now.getTime() +
          (selectedShipmentType.name === "EXPRESS"
            ? arrivalTimeInSeconds * 3 * 1000
            : arrivalTimeInSeconds * 9 * 1000),
      );

      // Formatar resultado
      freteCalculado = {
        value: freightValue,
        estimatedDelivery: deliveryDate.toLocaleDateString("pt-BR"),
      };
    } catch (err) {
      console.error("Erro ao calcular frete:", err);
    }
  }

  // Função para enviar o pacote
  async function handleEnviarPacote() {
    isLoading = true;
    erroApi = null;

    try {
      // Verificar se temos frete calculado
      if (!freteCalculado) {
        throw new Error("Por favor, aguarde o cálculo do frete");
      }

      // Construir payload conforme ShipmentRequestDTO
      const payload = {
        sender: {
          cpf: remetente.cpf,
          fullName: remetente.nome,
          email: remetente.email,
          phone: remetente.phone,
        },
        receiver: {
          cpf: destinatario.cpf,
          fullName: destinatario.nome,
          email: destinatario.email,
          phone: destinatario.phone,
        },
        packageEntity: {
          heightCm: dimensoes.altura,
          widthCm: dimensoes.largura,
          depthCm: dimensoes.comprimento,
          weightGrams: dimensoes.peso,
          description: dimensoes.descricao || "",
        },
        shipmentType: selectedShipmentType?.name || "STANDARD",
        originLocation: {
          cep: remetente.cep,
          street: remetente.logradouro,
          number: remetente.numero,
          complement: remetente.complemento || "",
          neighborhood: remetente.bairro,
          city: remetente.cidade,
          state: remetente.estado,
        },
        destinationLocation: {
          cep: destinatario.cep,
          street: destinatario.logradouro,
          number: destinatario.numero,
          complement: destinatario.complemento || "",
          neighborhood: destinatario.bairro,
          city: destinatario.cidade,
          state: destinatario.estado,
        },
        freightValue: Math.round(freteCalculado.value), // Em centavos
      };

      // Enviar para API
      const response = await fetch(`${apiAddr}/public/envios/criar`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Erro ao criar envio");
      }

      const responseData = await response.json();
      shipmentResponse = {
        trackingCode: responseData.trackingCode,
        keyword: responseData.keyword,
      };
      showSuccessModal = true;
    } catch (err: any) {
      erroApi = err.message || "Erro desconhecido";
      showErrorModal = true;
    } finally {
      isLoading = false;
    }
  }

  // Fechar modais
  function fecharModal() {
    showErrorModal = false;
    showSuccessModal = false;
  }

  // Resetar formulário após cadastro
  function resetarFormulario() {
    remetente = {
      nome: "",
      cpf: "",
      email: "",
      phone: "",
      cep: "",
      logradouro: "",
      numero: "",
      complemento: "",
      bairro: "",
      cidade: "",
      estado: "",
    };

    destinatario = {
      nome: "",
      cpf: "",
      email: "",
      phone: "",
      cep: "",
      logradouro: "",
      numero: "",
      complemento: "",
      bairro: "",
      cidade: "",
      estado: "",
    };

    dimensoes = {
      peso: null,
      altura: null,
      largura: null,
      comprimento: null,
      descricao: "",
    };

    freteCalculado = null;
    shipmentResponse = null;
  }
</script>

<!-- Container principal -->
<div class="flex min-h-screen w-full bg-slate-50 font-sans text-gray-800">
  <!-- Sidebar de Navegação -->
  <aside
    class="hidden w-64 flex-col items-center space-y-10 bg-white p-4 shadow-lg md:flex"
  >
    <nav class="w-full">
      <ul class="flex flex-col gap-3">
        <li>
          <a
            href="#/"
            class="flex items-center gap-3 rounded-lg p-3 text-gray-500 hover:bg-gray-100 hover:text-purple-700"
            ><svg
              class="h-5 w-5"
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 20 20"
              fill="currentColor"
              ><path
                fill-rule="evenodd"
                d="M4.25 2A2.25 2.25 0 0 0 2 4.25v2.5A2.25 2.25 0 0 0 4.25 9h2.5A2.25 2.25 0 0 0 9 6.75v-2.5A2.25 2.25 0 0 0 6.75 2h-2.5ZM2 13.25A2.25 2.25 0 0 1 4.25 11h2.5A2.25 2.25 0 0 1 9 13.25v2.5A2.25 2.25 0 0 1 6.75 18h-2.5A2.25 2.25 0 0 1 2 15.75v-2.5ZM11 4.25A2.25 2.25 0 0 1 13.25 2h2.5A2.25 2.25 0 0 1 18 4.25v2.5A2.25 2.25 0 0 1 15.75 9h-2.5A2.25 2.25 0 0 1 11 6.75v-2.5ZM13.25 11A2.25 2.25 0 0 0 11 13.25v2.5A2.25 2.25 0 0 0 13.25 18h2.5A2.25 2.25 0 0 0 18 15.75v-2.5A2.25 2.25 0 0 0 15.75 11h-2.5Z"
                clip-rule="evenodd"
              /></svg
            ><span class="font-medium">Painel</span></a
          >
        </li>
        <li>
          <a
            href="#/"
            class="flex items-center gap-3 rounded-lg bg-purple-600 p-3 font-semibold text-white shadow-md"
            ><svg
              class="h-5 w-5"
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 20 20"
              fill="currentColor"
              ><path
                d="M3.5 3A1.5 1.5 0 0 0 2 4.5v2.766a2.5 2.5 0 0 0 1.413 2.263A2.5 2.5 0 0 1 6.5 11h7a2.5 2.5 0 0 1 2.5 2.5v.234a2.5 2.5 0 0 0 1.413-2.263V4.5A1.5 1.5 0 0 0 16.5 3h-13Z"
              /><path
                d="M3 10.5A1.5 1.5 0 0 1 4.5 9h11a1.5 1.5 0 0 1 1.5 1.5v4A1.5 1.5 0 0 1 15.5 15h-.691a2.502 2.502 0 0 0-2.31 2h-4a2.502 2.502 0 0 0-2.31-2H4.5a1.5 1.5 0 0 1-1.5-1.5v-4Z"
              /></svg
            ><span>Enviar Pacotes</span></a
          >
        </li>
      </ul>
    </nav>
  </aside>

  <!-- Conteúdo Principal com formulário -->
  <main class="w-full flex-1 p-4 sm:p-6 lg:p-10">
    <form on:submit|preventDefault={handleEnviarPacote}>
      <h1
        class="mb-8 text-center text-3xl font-bold text-gray-800 lg:text-left lg:text-4xl"
      >
        Enviar Pacote
      </h1>

      <!-- Seção de Remetente e Destinatário com Inputs -->
      <div class="grid grid-cols-1 gap-8 lg:grid-cols-2">
        <!-- Card Remetente -->
        <div class="rounded-xl bg-white p-6 shadow-md">
          <h2 class="mb-6 text-xl font-bold text-gray-700">Remetente</h2>
          <div class="grid grid-cols-1 gap-x-4 gap-y-3 sm:grid-cols-2">
            <input
              bind:value={remetente.nome}
              type="text"
              placeholder="Nome Completo"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400 sm:col-span-2"
            />
            <input
              bind:value={remetente.cpf}
              type="text"
              placeholder="CPF"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400"
            />
            <input
              bind:value={remetente.cep}
              type="text"
              placeholder="CEP"
              required
              on:blur={() => buscarEnderecoPorCEP(remetente.cep, "remetente")}
              class={`w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400 ${cepError.remetente ? "border-red-500" : ""}`}
            />
            {#if cepError.remetente}
              <p class="text-red-500 text-xs col-span-2">CEP não encontrado</p>
            {/if}
            <input
              bind:value={remetente.email}
              type="email"
              placeholder="E-mail"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400 sm:col-span-2"
            />
            <input
              bind:value={remetente.logradouro}
              type="text"
              placeholder="Endereço"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400 sm:col-span-2"
            />
            <input
              bind:value={remetente.numero}
              type="text"
              placeholder="Número"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400"
            />
            <input
              bind:value={remetente.complemento}
              type="text"
              placeholder="Complemento (Opcional)"
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400"
            />
            <input
              bind:value={remetente.bairro}
              type="text"
              placeholder="Bairro"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400"
            />
            <input
              bind:value={remetente.cidade}
              type="text"
              placeholder="Cidade"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400"
            />
            <input
              bind:value={remetente.estado}
              type="text"
              placeholder="Estado"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400"
            />
            <input
              bind:value={remetente.phone}
              type="text"
              placeholder="Telefone"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400 sm:col-span-2"
            />
          </div>
        </div>

        <!-- Card Destinatário -->
        <div class="rounded-xl bg-white p-6 shadow-md">
          <h2 class="mb-6 text-xl font-bold text-gray-700">Destinatário</h2>
          <div class="grid grid-cols-1 gap-x-4 gap-y-3 sm:grid-cols-2">
            <input
              bind:value={destinatario.nome}
              type="text"
              placeholder="Nome Completo"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400 sm:col-span-2"
            />
            <input
              bind:value={destinatario.cpf}
              type="text"
              placeholder="CPF"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400"
            />
            <input
              bind:value={destinatario.cep}
              type="text"
              placeholder="CEP"
              required
              on:blur={() =>
                buscarEnderecoPorCEP(destinatario.cep, "destinatario")}
              class={`w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400 ${cepError.destinatario ? "border-red-500" : ""}`}
            />
            {#if cepError.destinatario}
              <p class="text-red-500 text-xs col-span-2">CEP não encontrado</p>
            {/if}
            <input
              bind:value={destinatario.email}
              type="email"
              placeholder="E-mail"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400 sm:col-span-2"
            />
            <input
              bind:value={destinatario.logradouro}
              type="text"
              placeholder="Endereço"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400 sm:col-span-2"
            />
            <input
              bind:value={destinatario.numero}
              type="text"
              placeholder="Número"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400"
            />
            <input
              bind:value={destinatario.complemento}
              type="text"
              placeholder="Complemento (Opcional)"
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400"
            />
            <input
              bind:value={destinatario.bairro}
              type="text"
              placeholder="Bairro"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400"
            />
            <input
              bind:value={destinatario.cidade}
              type="text"
              placeholder="Cidade"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400"
            />
            <input
              bind:value={destinatario.estado}
              type="text"
              placeholder="Estado"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400"
            />
            <input
              bind:value={destinatario.phone}
              type="text"
              placeholder="Telefone"
              required
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-2 px-3 text-sm focus:border-purple-400 focus:ring-purple-400 sm:col-span-2"
            />
          </div>
        </div>
      </div>

      <div class="my-8 flex justify-center">
        <div class="inline-flex rounded-full bg-white p-1 shadow-inner">
          <select
            bind:value={selectedShipmentType}
            class="cursor-pointer appearance-none rounded-full bg-purple-600 px-4 py-2 text-sm font-semibold text-white transition-colors hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2"
          >
            {#each shipmentTypes as type}
              <option value={type}>
                {type.name === "EXPRESS" ? "Envio Expresso" : "Envio Normal"}
              </option>
            {/each}
          </select>
        </div>
      </div>

      <!-- Seção de Dimensões e Valor do Frete -->
      <div class="mt-8 grid grid-cols-1 items-start gap-8 lg:grid-cols-3">
        <div class="rounded-xl bg-white p-6 shadow-md lg:col-span-2">
          <div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
            <input
              type="number"
              placeholder="Peso (g)"
              bind:value={dimensoes.peso}
              required
              min="1"
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-3 pl-4 pr-4 text-sm focus:border-purple-400 focus:ring-purple-400"
            />
            <input
              type="number"
              placeholder="Altura (cm)"
              bind:value={dimensoes.altura}
              required
              min="1"
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-3 pl-4 pr-4 text-sm focus:border-purple-400 focus:ring-purple-400"
            />
            <input
              type="number"
              placeholder="Largura (cm)"
              bind:value={dimensoes.largura}
              required
              min="1"
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-3 pl-4 pr-4 text-sm focus:border-purple-400 focus:ring-purple-400"
            />
            <input
              type="number"
              placeholder="Comprimento (cm)"
              bind:value={dimensoes.comprimento}
              required
              min="1"
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-3 pl-4 pr-4 text-sm focus:border-purple-400 focus:ring-purple-400"
            />
            <textarea
              bind:value={dimensoes.descricao}
              placeholder="Descrição do pacote (Opcional)"
              class="w-full rounded-lg border-gray-200 bg-slate-100 py-3 pl-4 pr-4 text-sm focus:border-purple-400 focus:ring-purple-400 sm:col-span-2"
            ></textarea>
          </div>
        </div>

        <!-- Card de Submissão -->
        <div class="rounded-xl bg-white p-6 text-center shadow-md">
          {#if erroApi}
            <div class="text-red-500">{erroApi}</div>
          {:else if freteCalculado}
            <h3 class="font-semibold text-gray-500">Envio Calculado!</h3>
            <p class="my-2 text-4xl font-bold text-gray-800">
              R$ {freteCalculado.value.toFixed(2).replace(".", ",")}
            </p>
            <p class="mb-4 text-sm text-gray-500">
              Chegará em: {freteCalculado.estimatedDelivery}
            </p>
          {:else}
            <h3 class="font-semibold text-gray-500">Tudo pronto?</h3>
            <p class="my-4 text-gray-400">
              {#if selectedShipmentType && remetente.cep && destinatario.cep}
                Preencha as dimensões para calcular o frete
              {:else}
                Preencha todos os campos para enviar o pacote
              {/if}
            </p>
          {/if}
          <button
            type="submit"
            class="flex w-full items-center justify-center rounded-lg bg-purple-600 py-3 text-base font-bold text-white shadow-md transition hover:bg-purple-700 disabled:cursor-not-allowed disabled:bg-purple-400"
            disabled={isLoading || !freteCalculado}
          >
            {#if isLoading}
              <svg
                class="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                ><circle
                  class="opacity-25"
                  cx="12"
                  cy="12"
                  r="10"
                  stroke="currentColor"
                  stroke-width="4"
                ></circle><path
                  class="opacity-75"
                  fill="currentColor"
                  d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                ></path></svg
              >
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

<!-- Modal de Erro -->
{#if showErrorModal}
  <div
    class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50"
  >
    <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
      <div class="flex justify-between items-center mb-4">
        <h3 class="text-xl font-bold text-red-600">Erro</h3>
        <button
          on:click={fecharModal}
          class="text-gray-500 hover:text-gray-700"
          aria-label="Fechar modal"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M6 18L18 6M6 6l12 12"
            />
          </svg>
        </button>
      </div>

      <div class="space-y-3">
        <p><strong>Mensagem:</strong> {erroApi}</p>
      </div>

      <div class="mt-6 flex justify-end">
        <button
          on:click={fecharModal}
          class="bg-purple-500 text-white px-4 py-2 rounded-md hover:bg-purple-600 transition"
        >
          Fechar
        </button>
      </div>
    </div>
  </div>
{/if}

<!-- Modal de Sucesso -->
{#if showSuccessModal && shipmentResponse}
  <div
    class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50"
  >
    <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
      <div class="flex justify-between items-center mb-4">
        <h3 class="text-xl font-bold text-green-600">Pacote Cadastrado!</h3>
        <button
          on:click={fecharModal}
          class="text-gray-500 hover:text-gray-700"
          aria-label="Fechar modal"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M6 18L18 6M6 6l12 12"
            />
          </svg>
        </button>
      </div>

      <div class="space-y-4 text-center">
        <div class="text-4xl">📦</div>
        <p class="text-lg text-gray-700">
          Seu pacote foi cadastrado com sucesso!
        </p>

        <div class="bg-gray-50 p-4 rounded-lg">
          <div class="flex justify-between items-center mb-2">
            <span class="font-semibold">Código de Rastreio:</span>
            <span class="font-mono text-purple-600"
              >{shipmentResponse.trackingCode}</span
            >
          </div>
          <div class="flex justify-between items-center">
            <span class="font-semibold">Palavra-chave:</span>
            <span class="font-mono text-purple-600"
              >{shipmentResponse.keyword}</span
            >
          </div>
        </div>

        <p class="text-sm text-gray-500 mt-4">
          Anote essas informações para acompanhar o status da sua entrega.
        </p>
      </div>

      <div class="mt-6 flex justify-center">
        <button
          on:click={() => {
            fecharModal();
            resetarFormulario();
          }}
          class="bg-purple-500 text-white px-6 py-2 rounded-md hover:bg-purple-600 transition"
        >
          Novo Envio
        </button>
      </div>
    </div>
  </div>
{/if}
