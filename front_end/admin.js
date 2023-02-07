const sendI = document.querySelector("#sendInfo");
const resetI = document.querySelector("#resetInfo");

const url = window.location.href;
const searchParams = new URLSearchParams(url.substring(url.indexOf('?')));
const email = searchParams.get('email');
const encodedEmail = encodeURIComponent(email);
document.getElementById("myEmail").innerHTML = email.substring(0, email.indexOf("@"));

if (resetI) {
  resetI.addEventListener("click", (e) => {
    e.preventDefault();
    window.location = "http://localhost:8000/admin.html?email=" + encodedEmail;
  });
}

function getCookie(name) {
  const cookieString = document.cookie;
  const cookieRegex = new RegExp(`${name}=([^;]+)`);
  const cookieMatch = cookieString.match(cookieRegex);
  return cookieMatch ? cookieMatch[1] : null;
}

const jwt = getCookie("jwt");

if (sendI) {
  sendI.addEventListener("click", (e) => {
    e.preventDefault();

    const bearer = `Bearer ${jwt}`;

    var myHeaders = new Headers();
    myHeaders.append("Authorization", bearer);
    myHeaders.append("Content-Type", "application/json");

    var options = document.getElementsByName("roles")[0].value;

    //console.log(options);
    if (options == 1) {
      var email = document.getElementsByName("email")[0].value;
      var phone = document.getElementsByName("telefon")[0].value;
      var cnp = document.getElementsByName("CNP")[0].value;
      var name = document.getElementsByName("nume")[0].value;

      if (email == "") {
        alert("Email is missing!");
        return;
      }
      if (phone == "") {
        alert("Phone number is missing!");
        return;
      }
      if (cnp == "") {
        alert("CNP is missing!");
        return;
      }
      if (name == "") {
        alert("Name is missing!");
        return;
      }

      var raw = JSON.stringify({
        email: email,
        name: name,
        phone: phone,
        cnp: cnp,
      });

      var requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: raw,
        redirect: "follow",
      };

      fetch("http://localhost:8081/create-rcz", requestOptions)
        .then((response) => {
          const status = response.status;
          if (status != 200) {
            alert(
              "Account was not created!\nMaybe an account with this email already exists!"
            );
            window.location = "http://localhost:8000/admin.html?email=" + encodedEmail;
            return null;
          }
          alert("Cont rcz creat!");

          return response.text();
        })
        .then((result) => console.log(result))
        .catch((error) => console.log("error", error));
    } else if (options == 2) {
      var cnp = document.getElementsByName("CNP")[0].value;
      var phone = document.getElementsByName("telefon")[0].value;
      var email = document.getElementsByName("email")[0].value;
      var name = document.getElementsByName("nume")[0].value;

      if (email == "") {
        alert("Email is missing!");
        return;
      }
      if (phone == "") {
        alert("Phone number is missing!");
        return;
      }
      if (cnp == "") {
        alert("CNP is missing!");
        return;
      }
      if (name == "") {
        alert("Name is missing!");
        return;
      }

      var raw = JSON.stringify({
        email: email,
        name: name,
        phone: phone,
        cnp: cnp,
      });

      var requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: raw,
        redirect: "follow",
      };

      fetch("http://localhost:8081/create-secretar", requestOptions)
        .then((response) => {
          const status = response.status;
          if (status != 200) {
            alert(
              "Account was not created!\nMaybe an account with this email already exists!"
            );
            window.location = "http://localhost:8000/admin.html?email=" + encodedEmail;
            return null;
          }
          alert("Cont secretar creat!");

          return response.text();
        })
        .then((result) => console.log(result))
        .catch((error) => console.log("error", error));
    }
  });
}
