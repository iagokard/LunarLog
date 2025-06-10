<!-- Footer.svelte -->
<script>
	import {
		faFacebookF,
		faTwitter,
		faWhatsapp,
		faInstagram,
	} from "@fortawesome/free-brands-svg-icons";
	import { FontAwesomeIcon } from "@fortawesome/svelte-fontawesome";
</script>

<!-- Rodapé -->
<footer class="bg-indigo-700 text-white mt-20 p-10">
	<div class="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-4 gap-8">
		<div>
			<h3 class="text-pink-400 font-bold text-lg mb-2">LunarLog</h3>
			<p class="text-sm">
				A LunarLog é uma empresa comprometida com a qualidade e a
				responsabilidade em todos os seus setores de serviço. Priorizar
				a experiência do consumidor é a nossa principal missão.
			</p>
		</div>

		<div>
			<h4 class="font-semibold mb-2">Informações</h4>
			<ul class="space-y-1 text-sm">
				<li>Sobre Nós</li>
				<li>Nossas Filiais</li>
				<li>Blog</li>
				<li>Notícias e Mídia</li>
			</ul>
		</div>

		<div>
			<h4 class="font-semibold mb-2">Legal</h4>
			<ul class="space-y-1 text-sm">
				<li>Termos</li>
				<li>Privacidade</li>
			</ul>
		</div>

		<div>
			<h4 class="font-semibold mb-2">Aviso</h4>
			<div class="flex space-x-4">
				<div>
					<p>
						Este é um projeto acadêmico, a LunarLog é uma empresa
						ficcional.
					</p>
				</div>
				<!-- <div class="bg-white text-pink-500 rounded-full p-2"> -->
				<!-- 	<FontAwesomeIcon icon={faFacebookF} /> -->
				<!-- </div> -->
				<!-- <div class="bg-white text-pink-500 rounded-full p-2"> -->
				<!-- 	<FontAwesomeIcon icon={faTwitter} /> -->
				<!-- </div> -->
				<!-- <div class="bg-white text-pink-500 rounded-full p-2"> -->
				<!-- 	<FontAwesomeIcon icon={faWhatsapp} /> -->
				<!-- </div> -->
				<!-- <div class="bg-white text-pink-500 rounded-full p-2"> -->
				<!-- 	<FontAwesomeIcon icon={faInstagram} /> -->
				<!-- </div> -->
			</div>
		</div>
	</div>
</footer>
