<!-- src/routes/+page.svelte -->
<script>
	import Header from "$lib/components/Header.svelte";
	import Hero from "$lib/components/Hero.svelte";
	import TrackingSection from "$lib/components/TrackingSection.svelte";
	import HistorySection from "$lib/components/HistorySection.svelte";
	import FreightCalculator from "$lib/components/FreightCalculator.svelte";
	import AboutSection from "$lib/components/AboutSection.svelte";
	import Footer from "$lib/components/Footer.svelte";
</script>

<main class="bg-white">
	<Header />
	<Hero />
	<TrackingSection />
	<HistorySection />
	<FreightCalculator />
	<AboutSection />
	<Footer />
</main>
