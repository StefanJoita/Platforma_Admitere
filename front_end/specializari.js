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

var res;
window.onload = async () => {
    await getList();
}

function printTable() {
    const tableBody = document.querySelector('tbody');

    const resArray = JSON.parse(res);

    for (let i = 0; i < resArray.length; i++) {
      const item = resArray[i];
      const row = document.createElement('tr');

      const cell1 = document.createElement('td');
      cell1.textContent = item[0];
      row.appendChild(cell1);

      const cell2 = document.createElement('td');
      cell2.textContent = item[1];
      cell2.setAttribute('width', '140px');
      row.appendChild(cell2);

      tableBody.appendChild(row);
    }
}

function getList()
{
    const jwt = getCookie("jwt");
    const bearer = `Bearer ${jwt}`;
    var myHeaders = new Headers();
    myHeaders.append("Authorization", bearer);
    myHeaders.append("Content-Type", "application/json");

    var requestOptions = {
        method: "GET",
        headers: myHeaders,
        redirect: "follow"
    };
    fetch("http://localhost:8081/afisare-specializari", requestOptions)
          .then((response) => {
            const status = response.status;
            if (status != 200) {
              alert("EROARE la primire! Code: " + status);
              return null;
            }
            return response.text();
          })
          .then((result) => {
            //console.log(result);
            res = result;
            printTable()
          })
}