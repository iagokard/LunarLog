<script lang="ts">
	import { onMount } from "svelte";
	import type {
		ShipmentType,
		TomTomGeocodeResult,
		TomTomRouteResult,
		FreightCalculation,
		ApiError,
	} from "$lib/types/freight";

	// Configurações
	const apiAddr = import.meta.env.VITE_API_ADDR || "http://localhost:8080";
	const tomtomKey = import.meta.env.VITE_TOMTOM_API_KEY;

	// Estados reativos
	let shipmentTypes: ShipmentType[] = [];
	let selectedShipmentType: ShipmentType | null = null;
	let senderCep = "";
	let receiverCep = "";
	let weight = "";
	let height = "";
	let width = "";
	let length = "";
	let freightResult: FreightCalculation | null = null;
	let error: ApiError | null = null;
	let showErrorModal = false;
	let isLoading = false;
	let cepError: { sender: boolean; receiver: boolean } = {
		sender: false,
		receiver: false,
	};

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
				const errorData: ApiError = await response.json();
				throw new Error(
					errorData.message || "Failed to load shipment types",
				);
			}
		} catch (err) {
			// Tratamento seguro de erro
			const message =
				err instanceof Error ? err.message : "Erro desconhecido";

			error = {
				status: 500,
				error: "Erro de conexão",
				message:
					message || "Não foi possível carregar os tipos de envio",
			};
			showErrorModal = true;
		}
	});

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

		if (
			!data.results ||
			data.results.length === 0 ||
			data.summary.numResults === 0
		) {
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

	// Função para calcular o frete
	const calculateFreight = async () => {
		if (!selectedShipmentType) {
			error = {
				status: 400,
				error: "Tipo de envio não selecionado",
				message: "Selecione um tipo de envio antes de calcular",
			};
			showErrorModal = true;
			return;
		}

		// Resetar estados
		isLoading = true;
		freightResult = null;
		error = null;
		showErrorModal = false;
		cepError = { sender: false, receiver: false };

		try {
			// Validar inputs
			const weightNum = parseFloat(weight);
			const heightNum = parseFloat(height);
			const widthNum = parseFloat(width);
			const lengthNum = parseFloat(length);

			if (isNaN(weightNum)) throw new Error("Peso inválido");
			if (isNaN(heightNum)) throw new Error("Altura inválida");
			if (isNaN(widthNum)) throw new Error("Largura inválida");
			if (isNaN(lengthNum)) throw new Error("Comprimento inválido");

			// Obter coordenadas dos CEPs
			const [senderCoords, receiverCoords] = await Promise.all([
				geocodeCep(senderCep),
				geocodeCep(receiverCep),
			]);

			// Calcular distância
			const distance = await calculateRoute(
				{ lat: senderCoords.lat, lon: senderCoords.lon },
				{ lat: receiverCoords.lat, lon: receiverCoords.lon },
			);

			// Calcular volume em cm³
			const volume = heightNum * widthNum * lengthNum;

			// Aplicar isenções
			const effectiveWeight = Math.max(
				0,
				weightNum - selectedShipmentType.maxWeightExemption,
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
				effectiveWeight *
					(selectedShipmentType.freightPerWeight / 1000) +
				effectiveDistance *
					(selectedShipmentType.freightPerDistance / 1000) +
				effectiveVolume *
					(selectedShipmentType.freightPerVolume / 1000);

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
			freightResult = {
				value: freightValue / 100, // Convertendo para reais
				estimatedDelivery: deliveryDate.toLocaleDateString("pt-BR"),
			};
		} catch (err) {
			// Tratamento seguro de erro
			let errorMessage = "Erro desconhecido";

			if (err instanceof Error) {
				errorMessage = err.message;
			} else if (typeof err === "string") {
				errorMessage = err;
			}

			// Tratamento específico para erro de CEP
			if (errorMessage.includes("CEP não encontrado")) {
				cepError = {
					sender: errorMessage.includes(senderCep),
					receiver: errorMessage.includes(receiverCep),
				};
			} else {
				error = {
					status: 400,
					error: "Erro no cálculo",
					message:
						errorMessage || "Ocorreu um erro durante o cálculo",
				};
				showErrorModal = true;
			}
		} finally {
			isLoading = false;
		}
	};

	// Fechar modal
	const closeModal = () => {
		showErrorModal = false;
	};

	// Formatar valor monetário
	const formatCurrency = (value: number) => {
		return value === 0
			? "ISENTO"
			: `R$ ${value.toFixed(2).replace(".", ",")}`;
	};
</script>

<section id="calculadora">
	<div class="text-center mt-10">
		<h1 class="text-pink-500 text-2xl font-bold underline">
			Calculadora de Frete
		</h1>
	</div>

	<div class="mt-10 flex justify-center">
		<div class="bg-white rounded-xl shadow-md p-6 w-11/12 max-w-6xl">
			<!-- Seletor de tipo de envio -->
			<div class="mb-6">
				<label class="block text-sm font-medium text-gray-700 mb-2">
					Tipo de Envio
				</label>
				<select
					bind:value={selectedShipmentType}
					class="border p-2 rounded-md w-full"
				>
					{#if shipmentTypes.length === 0}
						<option disabled>Carregando tipos...</option>
					{:else}
						{#each shipmentTypes as type}
							<option value={type}>
								{type.name}
								{type.name === "EXPRESS"
									? "(Entrega Rápida)"
									: "(Entrega Padrão)"}
							</option>
						{/each}
					{/if}
				</select>
			</div>

			<!-- Campos do formulário -->
			<div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
				<div>
					<input
						type="text"
						placeholder="Remetente (CEP)"
						class={`border p-2 rounded-md w-full ${cepError.sender ? "border-red-500" : ""}`}
						bind:value={senderCep}
					/>
					{#if cepError.sender}
						<p class="text-red-500 text-xs mt-1">CEP inválido</p>
					{/if}
				</div>

				<div>
					<input
						type="text"
						placeholder="Destinatário (CEP)"
						class={`border p-2 rounded-md w-full ${cepError.receiver ? "border-red-500" : ""}`}
						bind:value={receiverCep}
					/>
					{#if cepError.receiver}
						<p class="text-red-500 text-xs mt-1">CEP inválido</p>
					{/if}
				</div>

				<input
					type="number"
					placeholder="Peso (Gramas)"
					class="border p-2 rounded-md w-full"
					bind:value={weight}
					min="0"
				/>

				<input
					type="number"
					placeholder="Altura (Centímetros)"
					class="border p-2 rounded-md w-full"
					bind:value={height}
					min="0"
				/>

				<input
					type="number"
					placeholder="Largura (Centímetros)"
					class="border p-2 rounded-md w-full"
					bind:value={width}
					min="0"
				/>

				<input
					type="number"
					placeholder="Comprimento (Centímetros)"
					class="border p-2 rounded-md w-full"
					bind:value={length}
					min="0"
				/>
			</div>

			<!-- Botão calcular -->
			<div class="text-right">
				<button
					on:click={calculateFreight}
					class="bg-indigo-600 text-white px-6 py-2 rounded-md hover:bg-indigo-700 transition flex items-center justify-center ml-auto"
					disabled={isLoading}
				>
					{#if isLoading}
						<svg
							class="animate-spin -ml-1 mr-2 h-4 w-4 text-white"
							xmlns="http://www.w3.org/2000/svg"
							fill="none"
							viewBox="0 0 24 24"
						>
							<circle
								class="opacity-25"
								cx="12"
								cy="12"
								r="10"
								stroke="currentColor"
								stroke-width="4"
							></circle>
							<path
								class="opacity-75"
								fill="currentColor"
								d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
							></path>
						</svg>
					{/if}
					Calcular Frete
				</button>
			</div>
		</div>
	</div>

	<!-- Resultado -->
	<div class="flex flex-col items-center justify-center mt-10">
		<div class="flex gap-8 items-center flex-wrap justify-center">
			<img
				src="/assets/Rectangle 1314.png"
				alt="Mulher entregando caixas"
				class="w-64 rounded-xl shadow-md"
			/>

			{#if freightResult}
				<div
					class="border-fix border rounded-xl p-6 shadow-md min-w-[300px]"
				>
					<h2 class="text-lg text-gray-700 font-medium mb-2">
						Valor do Frete
					</h2>
					<p class="text-3xl text-pink-500 font-bold mb-1">
						{formatCurrency(freightResult.value)}
					</p>
					<p class="text-gray-600 text-sm">
						Chegará em: {freightResult.estimatedDelivery}
					</p>
				</div>
			{:else}
				<div
					class="border-fix border rounded-xl p-6 shadow-md min-w-[300px] bg-gray-50 text-center"
				>
					<p class="text-gray-500">
						Insira os dados e clique em "Calcular Frete"
					</p>
				</div>
			{/if}
		</div>
	</div>
</section>

<!-- Modal de Erro -->
{#if showErrorModal && error}
	<div
		class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50"
	>
		<div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
			<div class="flex justify-between items-center mb-4">
				<h3 class="text-xl font-bold text-red-600">
					Erro {error.status}
				</h3>
				<button
					on:click={closeModal}
					class="text-gray-500 hover:text-gray-700"
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
				<p><strong>Tipo:</strong> {error.error}</p>
				<p><strong>Mensagem:</strong> {error.message}</p>
				{#if error.path}
					<p><strong>Endpoint:</strong> {error.path}</p>
				{/if}
				{#if error.timestamp}
					<p>
						<strong>Horário:</strong>
						{new Date(error.timestamp).toLocaleString("pt-BR")}
					</p>
				{/if}
			</div>

			<div class="mt-6 flex justify-end">
				<button
					on:click={closeModal}
					class="bg-pink-500 text-white px-4 py-2 rounded-md hover:bg-pink-600 transition"
				>
					Fechar
				</button>
			</div>
		</div>
	</div>
{/if}

<style>
	.border-fix {
		scroll-margin-top: 100px; /* Compensa altura do header fixo */
	}
</style>
