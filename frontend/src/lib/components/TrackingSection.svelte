<script lang="ts">
	import { onMount } from "svelte";
	import type { Shipment, ApiError, Location } from "$lib/types/shipment";

	const apiAddr = import.meta.env.VITE_API_ADDR;

	// Estados reativos
	let trackingCode = "";
	let shipment: Shipment | null = null;
	let error: ApiError | null = null;
	let isLoading = false;
	let showErrorModal = false;

	// Função para formatar localização
	const formatLocation = (loc: Location | null | undefined): string => {
		if (!loc) return "N/A";
		return `${loc.city} - ${loc.state}`;
	};

	// Função para buscar o rastreamento
	let trackingCodeUsed = "";
	const fetchTracking = async () => {
		if (!trackingCode.trim()) return;

		isLoading = true;
		error = null;
		shipment = null;
		showErrorModal = false;
		trackingCodeUsed = trackingCode.trim();

		try {
			const response = await fetch(
				`${apiAddr}/public/envios/rastreio/${trackingCode.trim()}`,
			);

			if (response.ok) {
				const data = await response.json();

				// Mapeia para o tipo Shipment, extraindo apenas os dados necessários
				shipment = {
					trackingCode: data.trackingCode,
					shipmentType: data.shipmentType,
					currentStatus: data.currentStatus,
					statusHistory: data.statusHistory.map((item: any) => ({
						status: item.status,
						location: {
							city: item.location.city,
							state: item.location.state,
							complement: item.location.complement,
						},
						nextLocation: item.nextLocation
							? {
									city: item.nextLocation.city,
									state: item.nextLocation.state,
									complement: item.nextLocation.complement,
								}
							: null,
						eventTime: item.eventTime,
					})),
				};
			} else {
				error = await response.json();

				if (error?.status === 404) {
					// Erro 404 tratado diretamente no input
				} else {
					showErrorModal = true;
				}
			}
		} catch (err) {
			error = {
				status: 500,
				error: "Erro de conexão",
				message: "Não foi possível conectar ao servidor",
			};
			showErrorModal = true;
		} finally {
			isLoading = false;
		}
	};

	// Função para lidar com o submit do formulário
	const handleSubmit = (e: Event) => {
		e.preventDefault();
		fetchTracking();
	};

	// Fechar modal ao pressionar ESC
	onMount(() => {
		const handleKeyDown = (e: KeyboardEvent) => {
			if (e.key === "Escape") showErrorModal = false;
		};

		window.addEventListener("keydown", handleKeyDown);
		return () => window.removeEventListener("keydown", handleKeyDown);
	});
</script>

<section id="rastreamento" class="bg-white py-16 px-6 md:px-20">
	<div class="max-w-4xl mx-auto text-center">
		<h2 class="text-3xl font-bold text-indigo-600 mb-4">Rastreamento</h2>

		<!-- Formulário -->
		<form
			on:submit={handleSubmit}
			class="flex flex-col sm:flex-row justify-center gap-4 mb-12"
		>
			<input
				type="text"
				placeholder="Ex: AA123456789BR"
				class="px-4 py-2 border border-gray-300 rounded-md w-full sm:w-96 focus:ring-2 focus:ring-pink-500 outline-none transition-colors"
				class:border-red-500={error?.status === 404}
				bind:value={trackingCode}
			/>
			<button
				type="submit"
				class="bg-pink-500 text-white px-6 py-2 rounded-md hover:bg-pink-600 transition flex items-center justify-center"
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
				Rastrear
			</button>
		</form>

		{#if error?.status === 404}
			<p class="text-red-500 text-sm mt-2">
				{error.message} - Código: "{trackingCodeUsed}"
			</p>
		{/if}
	</div>

	<!-- Resultado do rastreamento -->
	<div class="border-fix max-w-4xl mx-auto space-y-6">
		{#if shipment}
			<h3 class="text-2xl font-semibold text-center text-indigo-600">
				Status do Pacote
			</h3>

			<div
				class="border rounded-lg p-6 bg-gray-50 shadow-sm text-left text-sm text-gray-700"
			>
				<p class="text-gray-400 mb-2">
					Pedido <span class="font-semibold"
						>{shipment.trackingCode}</span
					>
				</p>
				<p>
					<strong>Tipo de Envio:</strong>
					<span class="text-indigo-600">
						{shipment.shipmentType}</span
					>
				</p>
				<p>
					<strong>Status Atual:</strong>
					<span class="text-pink-500"> {shipment.currentStatus}</span>
				</p>

				<h4 class="font-semibold mt-6 mb-3 text-indigo-700">
					Histórico de Eventos
				</h4>
				<div class="space-y-4">
					{#each shipment.statusHistory as item}
						<div class="border-l-2 border-indigo-200 pl-4 py-2">
							<p class="font-medium">{item.status}</p>
							<p>
								<strong>Data/Hora:</strong>
								{new Date(item.eventTime).toLocaleString(
									"pt-BR",
								)}
							</p>
							<p>
								<strong>Local:</strong>
								{formatLocation(item.location)}
							</p>
							{#if item.nextLocation}
								<p>
									<strong>Próximo Destino:</strong>
									{formatLocation(item.nextLocation)}
								</p>
							{/if}
						</div>
					{/each}
				</div>
			</div>
		{/if}
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
					on:click={() => (showErrorModal = false)}
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
					on:click={() => (showErrorModal = false)}
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
