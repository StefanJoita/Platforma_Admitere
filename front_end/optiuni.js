const sendS = document.querySelector("#sendList");

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

var row;
function start()
{
  row = event.target;
}
function dragover(){
  var e = event;
  e.preventDefault();

  let children= Array.from(e.target.parentNode.parentNode.children);
  if(children.indexOf(e.target.parentNode)>children.indexOf(row))
    e.target.parentNode.after(row);
  else
    e.target.parentNode.before(row);
}
//

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
      row.setAttribute('draggable', 'true');
      row.setAttribute('ondragstart', 'start()');
      row.setAttribute('ondragover', 'dragover()');

      const cell1 = document.createElement('td');
      cell1.textContent = item[0];
      row.appendChild(cell1);

      tableBody.appendChild(row);
    }
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

var isValid = "";
var statOpt = "";
getStat(email);
getStatOpt(email);

if (sendS) {
  sendS.addEventListener("click", (e) => {
    e.preventDefault();
    getStat(email);
    getStatOpt(email);
    switch (isValid){
        case "VALIDAT":
            switch (statOpt){
                case "EXISTA":
                    alert("Nu poti alege!\nSpecializarile au fost deja alese!");
                    break;
                case "NU EXISTA":
                    putOpt();
                    break;
                case "NOT VALID":
                    alert("Nu poti alege!\nNu ai fost inca validat!");
                    break;
                default:
                    alert("Nu gasim candidatul!");
            }
            break;
        case "RESPINS":
            alert("Documentele au fost respinse!\nNu poti alege optiunile!");
           break;
        default:
            alert("Documentele nu au fost inca verificate!\nNu poti alege optiunile!");
    }

  });
}

function putOpt()
{
    const bearer = `Bearer ${jwt}`;

    const url = window.location.href;
    const searchParams = new URLSearchParams(url.substring(url.indexOf('?')));
    const email = searchParams.get('email');

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Authorization", bearer);

    var jsonData = [];
    var specializare,locuri,id;
    const resArray = JSON.parse(res);
    var rows = document.querySelectorAll('tr');
    for (let i = 0; i < rows.length; i++) {
      const cells = rows[i].querySelectorAll('td');
      specializare = cells[0].textContent;

      for (let j = 0; j < resArray.length; j++) {
           const item = resArray[j];
           if(item[0] == specializare){
                locuri = item[1];
                id = item[2];
            }
      }
      jsonData.push({
          "specializare_id": id,
          "denumire": specializare,
          "nr_locuri": locuri,
      });
    }

    var raw = JSON.stringify(jsonData);
    console.log(raw);

    var requestOptions = {
      method: "PUT",
      headers: myHeaders,
      body: raw,
      redirect: 'follow'
    };

  fetch("http://localhost:8081/adaugare-optiuni/" + email , requestOptions)
      .then((response) => {
        const status = response.status;
        if (status != 200) {
          alert("EROARE la trimiterea specializarilor! Code: " + status);
          window.location = "http://localhost:8000/optiuni.html?email=" + encodedEmail;
          return null;
        }
        alert("Specializari trimise!");

        return response.text();
      })
      .then((result) => console.log(result))
      .catch((error) => console.log("error", error));
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
            alert("Something went wrong! Nu s-a putut determina statusul candidatului\n");
        }
        return response.text();
      })
      .then((result) => {
        isValid = result;
      });
}

function getStatOpt(em)
{
    const bearer = `Bearer ${jwt}`;
    const myHeaders = new Headers();
    myHeaders.append("Authorization", bearer);

    const requestOptions = {
      method: "GET",
      headers: myHeaders,
      redirect: "follow"
    };
    fetch("http://localhost:8081/get-status-optiuni/" + encodedEmail , requestOptions)
      .then((response) => {
        const status = response.status;
        if (status != 200) {
            alert("Something went wrong! Nu s-a putut determina statusul alegerii optiunilor\n");
        }
        return response.text();
      })
      .then((result) => {
        statOpt = result;
      });
}