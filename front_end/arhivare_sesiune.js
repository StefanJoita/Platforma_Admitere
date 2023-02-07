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
var res;

const download = document.querySelector("#arhivare-sesiune");
document.getElementById("ierarhizare-candidati").addEventListener("click", genereazaIerarhizare);


if (download) {
    download.addEventListener("click", (e) => {
        e.preventDefault();
        alert("Se genereaza arhivele!");
        const bearer = `Bearer ${jwt}`;
        var myHeaders = new Headers();
        myHeaders.append("Authorization", bearer);
        myHeaders.append("Content-Type", "application/json");

        var requestOptions = {
            method: "GET",
            headers: myHeaders,
            redirect: "follow"
        };
        fetch("http://localhost:8081/arhivare-sesiune", requestOptions)
          .then((response) => {
            const status = response.status;
            if (status != 200) {
              alert("EROARE la download! Code: " + status);
              return null;
            }
            else
            {
            alert("A fost creat un folder cu arhivele specifice!");
            }
            return response.text();
          })
          .then((result) => {
            //console.log(result);
            //res = result;
            //alert(res);
          })
    });
}


function genereazaIerarhizare() {
  alert("Se genereaza ierarhizarea!");
  const bearer = `Bearer ${jwt}`;
  var myHeaders = new Headers();
  myHeaders.append("Authorization", bearer);
  myHeaders.append("Content-Type", "application/json");

  fetch("http://localhost:8081/generare-ierarhizare", {
    method: "GET",
    headers: myHeaders,
    redirect: "follow"
  })
    .then((response) => {
      if (response.ok) {
        return response.blob();
      }
      throw new Error("Request failed");
    })
    .then((blob) => {
      const url = URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = "ierarhizare.pdf";
      a.click();
      URL.revokeObjectURL(url);
    })
    .catch((error) => {
      console.error(error);
    });
}





