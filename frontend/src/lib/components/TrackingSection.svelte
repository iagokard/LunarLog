<script>

</script>

<section id="rastreamento" class="bg-white py-16 px-6 md:px-20">
	<div class="max-w-4xl mx-auto text-center">
		<h2 class="text-3xl font-bold text-indigo-600 mb-4">Rastreamento</h2>

		<!-- Formulário -->
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

	<!-- Resultado do rastreamento -->
	<div class="border-fix max-w-4xl mx-auto space-y-6">
		<h3 class="text-2xl font-semibold text-center text-indigo-600">
			Status do Pacote
		</h3>

		<div
			class="border rounded-lg p-6 bg-gray-50 shadow-sm text-left text-sm text-gray-700"
		>
			<p class="text-gray-400 mb-2">
				Pedido <span class="font-semibold">AA123456789BR</span> – 26/04/2025
			</p>
			<p>
				<strong>Status:</strong>
				<span class="text-indigo-600"
					>Em separação no Centro de Distribuição de Cajamar - SP.</span
				>
			</p>
			<p>
				<strong>Destino Atual:</strong>
				<span class="text-blue-600">Cajamar - SP.</span>
			</p>
			<p>
				<strong>Próximo Destino:</strong>
				<span class="text-pink-500">Camaçari - BA.</span>
			</p>
			<div class="mt-2">
				<p><strong>Altura:</strong> 40cm</p>
				<p><strong>Largura:</strong> 60cm</p>
				<p><strong>Comprimento:</strong> 80cm</p>
				<p><strong>Peso:</strong> 650g</p>
			</div>
			<p class="text-blue-500 mt-2">
				<strong>Obs.:</strong> O pedido deve ser manuseado com cuidado, objeto
				frágil.
			</p>

			<hr class="my-4" />

			<p><strong>Remetente:</strong> Michellly de Oliveira</p>
			<p>Email: michellly_lol.oliveira@hotmail.com</p>
			<p>Celular: +55 (71) 91234-5678</p>
			<p>Endereço: Rua Águas de Sentina, BA</p>

			<hr class="my-4" />

			<p><strong>Destinatário:</strong> Carimbo Miguel</p>
			<p>Email: shaolimartadocorpo@gmail.com</p>
			<p>Celular: +55 (71) 97070-7070</p>
			<p>Endereço: Xique-Xique - BA</p>
		</div>
	</div>
</section>
