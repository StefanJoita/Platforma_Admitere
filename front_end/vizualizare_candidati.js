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
window.onload = async () => {
    await getList();
}

function getList()
{
    const bearer = `Bearer ${jwt}`;
    var myHeaders = new Headers();
    myHeaders.append("Authorization", bearer);
    myHeaders.append("Content-Type", "application/json");

    var requestOptions = {
        method: "GET",
        headers: myHeaders,
        redirect: "follow"
    };
    fetch("http://localhost:8081/vizualizare-candidati", requestOptions)
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
//            console.log(res);
            if (res == "[]")
            {
            var container = document.getElementById('container');
            var my_html = '';
            my_html += '</br></br>'
            my_html += '<h3 align=center>Nu există candidati inscrisi in examen!</h3>'
            container.innerHTML = my_html;
            }
            else
                printTable();

          })
}

function printTable() {
    const tableBody = document.querySelector('tbody');

    const resArray = JSON.parse(res);


    for (let i = 0; i < resArray.length; i++) {
      const row = document.createElement('tr');

        //EMAIL
      const cell1 = document.createElement('td');
      cell1.textContent = resArray[i]["email"];
      row.appendChild(cell1);
//      console.log(resArray[i]["email"]);

        //CNP
      const cell2 = document.createElement('td');
      cell2.textContent = resArray[i]["cnp"];
      row.appendChild(cell2);
//      console.log(resArray[i]["cnp"]);

        //note
      const cell3 = document.createElement('td');
      cell3.textContent = resArray[i]["medie"];
      row.appendChild(cell3);
//      console.log(resArray[i]["medie"]);

      tableBody.appendChild(row);
    }
}

