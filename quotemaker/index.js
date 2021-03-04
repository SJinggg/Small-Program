const quoteBtn = document.getElementById('get-quote');
quoteBtn.addEventListener('click', getQuote);

const endpoint = 'https://goquotes-api.herokuapp.com/api/v1/random?count=1';

async function getQuote() {
  document.getElementById("loader").style.display = "block";
  try {
    const response = await fetch(endpoint)
    if (!response.ok) {
      throw Error(response.statusText)
    }
    const json = await response.json();
    displayQuote(json.quotes[0]);
  } catch (err) {
    console.log(err)
    alert('Failed to fetch new quote');
  }
}

function displayQuote(quote) {
  const quoteText = document.querySelector('#quote-text');
  const quoteAuthor = document.querySelector('#quote-author');
  document.getElementById("loader").style.display = "none";
  quoteAuthor.textContent = quote.author;
  quoteText.textContent = quote.text;
}