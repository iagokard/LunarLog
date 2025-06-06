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

<!-- ... código anterior ... -->
<div class="flex space-x-4">
	<div class="bg-white text-pink-500 rounded-full p-2">
		<FontAwesomeIcon icon={faFacebookF} />
	</div>
	<div class="bg-white text-pink-500 rounded-full p-2">
		<FontAwesomeIcon icon={faTwitter} />
	</div>
	<div class="bg-white text-pink-500 rounded-full p-2">
		<FontAwesomeIcon icon={faWhatsapp} />
	</div>
	<div class="bg-white text-pink-500 rounded-full p-2">
		<FontAwesomeIcon icon={faInstagram} />
	</div>
</div>
