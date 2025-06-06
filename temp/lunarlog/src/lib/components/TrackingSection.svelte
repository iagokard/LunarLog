<!-- src/lib/components/TrackingSection.svelte -->
<section id="rastreamento" class="bg-white py-16 px-4 md:px-20">
	<div class="max-w-4xl mx-auto text-center">
		<h2 class="text-3xl font-bold text-indigo-600 mb-4">Rastreamento</h2>

		<form class="flex flex-col sm:flex-row justify-center gap-4 mb-12">
			<input
				type="text"
				placeholder="Ex: AA123456789BR"
				class="px-4 py-2 border border-gray-300 rounded-md w-full sm:w-96 focus:ring-2 focus:ring-pink-500 outline-none"
			/>
			<button
				type="submit"
				class="bg-pink-500 text-white px-6 py-2 rounded-md hover:bg-pink-600 transition"
			>
				Rastrear
			</button>
		</form>
	</div>

	<div class="max-w-4xl mx-auto space-y-6">
		<h3 class="text-2xl font-semibold text-center text-indigo-600">
			Status do Pacote
		</h3>

		<div
			class="border rounded-lg p-6 bg-gray-50 shadow-sm text-left text-sm text-gray-700"
		>
			<!-- Conteúdo do rastreamento -->
		</div>
	</div>
</section>
