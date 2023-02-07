
var cookieValue = getCookie("jwt");
if (cookieValue == "") {
  window.stop();
  alert("Trebuie sa fii autentificat pentru a accesa aceasta pagina!");
  window.location.href = "http://localhost:8000/login.html";
}

if (navigator.cookieEnabled == false) {
  window.stop();
  alert("Trebuie sa fii autentificat pentru a accesa aceasta pagina!");
  window.location.href = "http://localhost:8000/login.html";
}

function logout() {
  document.cookie = "jwt=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";
  window.location.href = "http://localhost:8000/login.html";
}

function getCookie(name) {
  var value = "; " + document.cookie;
  var parts = value.split("; " + name + "=");
  if (parts.length == 2) return parts.pop().split(";").shift();
  return "";
}
