<!-- src/lib/components/Header.svelte -->
<header class="flex items-center justify-between px-4 py-4 shadow-sm sm:px-10">
	<!-- Logo -->
	<div class="text-2xl font-bold text-indigo-600">
		<span class="text-gray-800">Lunar</span><span class="text-pink-500"
			>Log</span
		>
	</div>

	<!-- Navegação -->
	<nav class="hidden md:flex space-x-8 text-gray-700 font-medium">
		<a href="#rastreamento" class="hover:text-indigo-600">Rastreamento</a>
		<a href="#calculadora" class="hover:text-indigo-600"
			>Calculadora de Frete</a
		>
		<a href="#sobre" class="hover:text-indigo-600">Sobre Nós</a>
	</nav>

	<!-- Ações -->
	<div class="flex items-center space-x-4">
		<a
			href="#"
			class="border border-pink-500 text-pink-500 px-4 py-1.5 rounded-md font-medium hover:bg-pink-50 transition"
		>
			Cadastrar
		</a>
		<a
			href="#"
			class="bg-pink-500 text-white px-4 py-1.5 rounded-md font-medium hover:bg-pink-600 transition"
		>
			Entrar
		</a>
	</div>
</header>
