/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./**/*.{html,js}"],
  theme: {
    extend: {
      backgroundImage: {
        casa: "url('/assets/bg.png')",
      },
    },
  },
  plugins: [],
}

