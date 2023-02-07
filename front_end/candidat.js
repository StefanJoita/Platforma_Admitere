const resetI = document.querySelector("#resetInfo");
const sendF = document.querySelector("#sendFiles");
const jwt = getCookie("jwt");

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

var alreadyVal;
window.onload = async () => {
    await getStat(email);
}

function getStat(em)
{
    const bearer = `Bearer ${jwt}`;
    const myHeaders = new Headers();
    myHeaders.append("Authorization", bearer);

    const requestOptions = {
      method: "GET",
      headers: myHeaders,
      redirect: "follow"
    };
    fetch("http://localhost:8081/get-status-documente/" + encodedEmail , requestOptions)
      .then((response) => {
        const status = response.status;
        if (status != 200) {
            alert("Something went wrong!\n");
        }
        return response.text();
      })
      .then((result) => {
           var container = document.getElementById('container');
           var my_html = '';
           switch (result){
                case "VALIDAT":
                   my_html += '<h3 align=center>Documentele au fost validate cu succes!</h3>'
                   my_html += '</br>'
                   container.innerHTML = my_html;
                   alreadyVal = 1;
                   break;
                case "RESPINS":
                   my_html += '<h3 align=center>Documentele au fost respinse!</h3>'
                   my_html += '<h3 align=center>Încărcați arhiva zip!</h3>'
                   my_html += '</br>'
                   container.innerHTML = my_html;
                   alreadyVal = 0;
                   break;
                default:
                   my_html += '<h3 align=center>Încă nu ai primit un status de validare!</h3>'
                   my_html += '<h3 align=center>Încărcați arhiva zip!</h3>'
                   my_html += '</br>'
                   container.innerHTML = my_html;
                   alreadyVal = 0;
           }
      });
}

if (resetI) {
  resetI.addEventListener("click", (e) => {
    e.preventDefault();
    window.location = "http://localhost:8000/candidat.html?email=" + encodedEmail;
  });
}
if (sendF) {
    sendF.addEventListener("click", (e) => {
        e.preventDefault();
        if(alreadyVal == 1)
            alert("Documentele au fost deja validate");
        else if(alreadyVal == 0)
        {
            const bearer = `Bearer ${jwt}`;

            const url = window.location.href;
            const searchParams = new URLSearchParams(url.substring(url.indexOf('?')));
            const email = searchParams.get('email');

            var myHeaders = new Headers();
            myHeaders.append("Authorization", bearer);

            var arhiva = document.getElementById("arhiva").files[0];

            if (typeof arhiva === "undefined") {
              alert("Arhiva lipseste!");
              return;
            }

            const formData = new FormData();
            formData.append("arhiva", arhiva, "arhiva.zip");
            formData.append("email", JSON.stringify(email));

            var requestOptions = {
              method: "POST",
              headers: myHeaders,
              body: formData,
              redirect: 'follow'
            };

            fetch("http://localhost:8081/add-files", requestOptions)
              .then((response) => {
                const status = response.status;
                if (status != 200) {
                  alert("EROARE la trimiterea fisierelor! Code: " + status);
                  window.location = "http://localhost:8000/candidat.html?email=" + encodedEmail;
                  return null;
                }
                alert("Fisiere adaugate!");

                return response.text();
              })
              .then((result) => console.log(result))
              .catch((error) => console.log("error", error));
        }
    });
}


