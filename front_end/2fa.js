document.getElementById("send-code-button").addEventListener("click", sendCode);
document.getElementById("corect").addEventListener("click", validateCode);

const url = window.location.href;
const searchParams = new URLSearchParams(url.substring(url.indexOf('?')));
const email = searchParams.get('email');
const encodedEmail = encodeURIComponent(email);


function sendCode()
{
  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  alert("Se trimite codul catre " + email + "...Asteptati...");
  var raw = JSON.stringify({
    email: email
    });

    var requestOptions = {
      method: "POST",
      headers: myHeaders,
      body: raw,
      redirect: "follow",
      timeout:15000,
      mode: "no-cors"
    };
    fetch("http://localhost:8081/2fa-send", requestOptions)
      .then((response) => {
        const status = response.status;
        if (status != 200) {
          window.location = "http://localhost:8000/2fa.html?email=" + encodedEmail;
        }
        return response.text();
      })
}
function validateCode()
{
      var myHeaders = new Headers();
      myHeaders.append("Content-Type", "application/json");
      var code = document.getElementsByName("code")[0].value;
      var raw = JSON.stringify({
        "code": code,
      });

      var requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: raw,
        redirect: "follow",
      };

      fetch("http://localhost:8081/2fa-validate", requestOptions)
        .then((response) => {
          const status = response.status;
          if (status != 200) {
            alert("Something went wrong!\nUser or password is incorrect!");
            window.location = "http://localhost:8000/login.html";
          }
          return response.text();
        })
        .then((result) => {
          const jwtPayload = result.split(".")[1];
          //alert(jwtPayload);
          const json = JSON.parse(atob(jwtPayload));
          const expirationDateReadable = new Date(json.exp * 1000);
          document.cookie = `jwt=${result}; expires=${expirationDateReadable}; path=/`;



          switch (json.roles) {
            case "ADMIN":
              // Redirect to the admin dashboard
              window.location = "http://localhost:8000/admin.html?email=" + encodedEmail;
              break;
            case "CANDIDAT":
              // Redirect to the candidat dashboard
              window.location = "http://localhost:8000/candidat.html?email=" + encodedEmail;
              break;
            case "RCZ":
              // Redirect to the reprezentant dashboard
              window.location = "http://localhost:8000/resp_czso.html?email=" + encodedEmail;
              break;
            case "SECRETAR":
              // Redirect to the secretar dashboard
              window.location = "http://localhost:8000/secretar.html?email=" + encodedEmail;
              break;
            default:
              // Redirect to the home page
              alert("Something went wrong!");
              window.location = "http://localhost:8000/login.html";
          }
        })
        .catch((error) => console.log("error", error));
    
  
}
