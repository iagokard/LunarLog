<section id="calculadora">
	<div class="text-center mt-10">
		<h1 class="text-pink-500 text-2xl font-bold underline">
			Calculadora de Frete
		</h1>
	</div>

	<div class="mt-10 flex justify-center">
		<div class="bg-white rounded-xl shadow-md p-6 w-11/12 max-w-6xl">
			<!-- Opções de envio -->
			<div class="mb-6 flex justify-start gap-2">
				<button class="px-4 py-2 rounded-full bg-pink-500 text-white"
					>Express</button
				>
				<button class="px-4 py-2 rounded-full bg-gray-100 text-gray-700"
					>Normal</button
				>
			</div>

			<!-- Campos do formulário -->
			<div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
				<input
					type="text"
					placeholder="Remetente (CEP)"
					class="border p-2 rounded-md w-full"
				/>
				<input
					type="text"
					placeholder="Destinatário (CEP)"
					class="border p-2 rounded-md w-full"
				/>
				<input
					type="text"
					placeholder="Peso (Gramas)"
					class="border p-2 rounded-md w-full"
				/>
				<input
					type="text"
					placeholder="Altura (Centímetros)"
					class="border p-2 rounded-md w-full"
				/>
				<input
					type="text"
					placeholder="Largura (Centímetros)"
					class="border p-2 rounded-md w-full"
				/>
				<input
					type="text"
					placeholder="Comprimento (Centímetros)"
					class="border p-2 rounded-md w-full"
				/>
			</div>

			<!-- Botão calcular -->
			<div class="text-right">
				<button class="bg-indigo-600 text-white px-6 py-2 rounded-md"
					>Calcular Frete</button
				>
			</div>
		</div>
	</div>

	<!-- Resultado -->
	<div class="flex flex-col items-center justify-center mt-10">
		<div class="flex gap-8 items-center">
			<img
				src="assets/Rectangle 1314.png"
				alt="Mulher entregando caixas"
				class="w-64 rounded-xl shadow-md"
			/>

			<div class="border-fix border rounded-xl p-6 shadow-md">
				<h2 class="text-lg text-gray-700 font-medium mb-2">
					Valor do Frete
				</h2>
				<p class="text-3xl text-pink-500 font-bold mb-1">R$ 49,99</p>
				<p class="text-gray-600 text-sm">
					Chegará entre 20 jun - 05 jul.
				</p>
			</div>
		</div>
	</div>
</section>
