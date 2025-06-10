<script>
	import AboutSection from "$lib/components/AboutSection.svelte";
	import Footer from "$lib/components/Footer.svelte";
	import FreightCalculator from "$lib/components/FreightCalculator.svelte";
	import Header from "$lib/components/Header.svelte";
	import Hero from "$lib/components/Hero.svelte";
	import TrackingSection from "$lib/components/TrackingSection.svelte";
</script>

<main class="font-sans">
	<Hero />
	<TrackingSection />
	<FreightCalculator />
	<AboutSection />
	<Footer />
</main>
