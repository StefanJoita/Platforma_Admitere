const form = document.querySelector(".form");
window.onload = function () {
  document.cookie = "jwt=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";
};

if (form) {
  form.addEventListener("submit", (e) => {
    e.preventDefault();

    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    const email = document.getElementsByName("email")[0].value;
    const password = document.getElementsByName("password")[0].value;

    const raw = JSON.stringify({
      email: email,
      password: password,
    });

    const requestOptions = {
      method: "POST",
      headers: myHeaders,
      body: raw,
      redirect: "follow",
    };
    const encodedEmail = encodeURIComponent(email);
    fetch("http://localhost:8081/login", requestOptions)
      .then((response) => {
        const status = response.status;
        if (status == 200) {
          window.location = "http://localhost:8000/2fa.html?email=" + email;
          return null;
        }
        alert("Something went wrong!\nUser or password is incorrect!");
        window.location = "http://localhost:8000/login.html";
        return response.text();
      })
      .catch((error) => console.log("error", error));
  });
}
