document.getElementById("generate-legitimatie").addEventListener("click", genereazaLegitimatie);
const urlpage = window.location.href;
function genereazaLegitimatie() {
  const searchParams = new URLSearchParams(url.substring(urlpage.indexOf('?')));
  const email = searchParams.get('email');
  alert("Se elibereaza legitimatie pentru"+email);  
  fetch("http://localhost:8081/generare-legitimatie", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ email: email }),
  })
    .then((response) => {
      // Check if the response is successful
      if (response.ok) {
        // If the response is successful, get the PDF data
        return response.blob();
      }
      throw new Error("Request failed");
    })
    .then((blob) => {
      // Create a URL for the PDF data
      const url = URL.createObjectURL(blob);
      // Create a link element
      const a = document.createElement("a");
      // Set the link element's href to the URL of the PDF
      a.href = url;
      // Set the link element's download attribute to a file name
      a.download = "legitimatie.pdf";
      // Click the link to download the PDF
      a.click();
      // Revoke the URL to free up memory
      URL.revokeObjectURL(url);
    })
    .catch((error) => {
      console.error(error);
    });
}
