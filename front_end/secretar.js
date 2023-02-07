const url = window.location.href;
const searchParams = new URLSearchParams(url.substring(url.indexOf('?')));
const email = searchParams.get('email');
const encodedEmail = encodeURIComponent(email);
document.getElementById("myEmail").innerHTML = email.substring(0, email.indexOf("@"));

function getCookie(name) {
  const cookieString = document.cookie;
  const cookieRegex = new RegExp(`${name}=([^;]+)`);
  const cookieMatch = cookieString.match(cookieRegex);
  return cookieMatch ? cookieMatch[1] : null;
}
const jwt = getCookie("jwt");

const sendN = document.querySelector("#sendNota");
const reset = document.querySelector("#reset");

if (sendN) {
  sendN.addEventListener("click", (e) => {
    e.preventDefault();
    const bearer = `Bearer ${jwt}`;
    var myHeaders = new Headers();
    myHeaders.append("Authorization", bearer);
    myHeaders.append("Content-Type", "application/json");

    var cnp = document.getElementsByName("cnp")[0].value;
    var nota = document.getElementsByName("medie")[0].value;

    var raw = JSON.stringify({
      "cnp": cnp,
      "nota": nota
    });
    console.log(raw);

    var requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: raw,
      redirect: 'follow'
    };

    if(parseInt(nota) > 10 || parseInt(nota) < 0)
        alert("Nota trebuie sa fie cuprinsa intre 0 si 10!")
    else{
    const encodedEmail = encodeURIComponent(email);
    fetch("http://localhost:8081/adauga-nota", requestOptions)
      .then((response) => {
        const status = response.status;
        if (status == 200) {
          alert("Nota actualizata cu succes!");
          window.location = "http://localhost:8000/secretar.html?email=" + email;
          return null;
        }
        alert("Something went wrong!\nVerificati CNP!");
        window.location = "http://localhost:8000/secretar.html?email=" + email;
        return response.text();
      })
      .catch((error) => console.log("error", error));

    }
  });
}

if (reset) {
  reset.addEventListener("click", (e) => {
    e.preventDefault();
    window.location = "http://localhost:8000/secretar.html?email=" + encodedEmail;
  });
}