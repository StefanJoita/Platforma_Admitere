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

function clickFunc(em,raspuns) {
    const bearer = `Bearer ${jwt}`;

    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Authorization", bearer);

    var raw = JSON.stringify({
      validare : raspuns
    });

    const requestOptions = {
      method: "PUT",
      headers: myHeaders,
      body: raw,
      redirect: "follow",
    };
    fetch("http://localhost:8081/candidati-inrolati/"+ em , requestOptions)
      .then((response) => {
        const status = response.status;
        if (status == 200) {
          alert(em + " a fost " + raspuns);
          window.location = "http://localhost:8000/validare_documente.html?email=" + encodedEmail;
          return null;
        }
        alert("Something went wrong!\n");
        //window.location = "http://localhost:8000/validare_documente.html?email=" + encodedEmail;
        return response.text();
      })
      .catch((error) => console.log("error", error));
}



function printTable() {
    const tableBody = document.querySelector('tbody');

    const resArray = JSON.parse(res);
    //alert( resArray[0]["email"]);

    for (let i = 0; i < resArray.length; i++) {
      const row = document.createElement('tr');

        //EMAIL
      const cell1 = document.createElement('td');
      cell1.textContent = resArray[i]["email"];
      row.appendChild(cell1);

        //LINK
      const cell2 = document.createElement('td');
      const link = document.createElement('a');
      link.href = resArray[i]["documente"];
      link.target = '_blank';
      link.textContent = "download";
      cell2.appendChild(link);
      row.appendChild(cell2);

      tableBody.appendChild(row);
      const emailCan = resArray[i]["email"];
        //Butoane
      const cell3 = document.createElement('td');
        cell3.innerHTML = `
          <button
              type="button"
              onclick = clickFunc("${emailCan}","VALIDAT")
              class="btn"
          >VALIDEAZA
          <button/>
          <button
              type="button"
              onclick = clickFunc("${emailCan}","RESPINS")
              class="btn"
          >RESPINGE
          <button/>
        `;
      row.appendChild(cell3);
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
    fetch("http://localhost:8081/get-arhive-candidati", requestOptions)
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
            console.log(res);
            if (res == "[]")
            {
            var container = document.getElementById('container');
            var my_html = '';
            my_html += '</br></br>'
            my_html += '<h3 align=center>Nu există candidati ce necesită validare!</h3>'
            container.innerHTML = my_html;
            }
            else
                printTable();

          })
}


