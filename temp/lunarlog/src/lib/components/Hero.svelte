<!-- src/lib/components/Hero.svelte -->
<section
	class="flex flex-col-reverse md:flex-row items-center justify-between px-4 py-10 bg-white sm:px-10 sm:py-20"
>
	<div class="md:w-1/2 space-y-6 mt-6 md:mt-0">
		<h1 class="text-3xl md:text-5xl font-bold text-indigo-600">
			Excelência em entrega,<br />
			<span class="text-indigo-600">Cuidado com você!</span>
		</h1>
		<p class="text-gray-600">
			Administre as suas encomendas em tempo real com a
			<span class="text-indigo-600 font-medium">qualidade</span> e a
			<span class="text-pink-500 font-medium">atenção</span> que só a LunarLog
			oferece!
		</p>

		<form class="flex flex-col sm:flex-row items-center gap-4 mt-4">
			<input
				type="text"
				placeholder="Ex: AA123456789BR"
				class="w-full px-4 py-2 border border-pink-300 rounded-md focus:outline-none focus:ring-2 focus:ring-pink-500"
			/>
			<button
				type="submit"
				class="w-full sm:w-auto bg-pink-500 text-white px-6 py-2 rounded-md hover:bg-pink-600 transition"
			>
				Rastrear
			</button>
		</form>
	</div>

	<div class="md:w-1/2 mb-6 md:mb-0">
		<img
			src="/assets/91fa59e6781adbdced82e349bb595d99_1.png"
			alt="Entregadora com mapa"
			class="w-full max-w-md mx-auto"
		/>
	</div>
</section>
