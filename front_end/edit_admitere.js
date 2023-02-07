var resetI1 = document.querySelector("#resetInfo");
var resetI2 = document.querySelector("#resetInfo2");
var stergeSpec = document.querySelector("#stergeSpec");
var sendI = document.querySelector("#sendInfo");

var jwt = getCookie("jwt");

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

if (resetI1) {
  resetI1.addEventListener("click", (e) => {
    e.preventDefault();
    window.location = "http://localhost:8000/edit_admitere.html?email=" + encodedEmail;
  });
}


document.querySelector("#sendInfo").addEventListener("click", function(event) {
    event.preventDefault();

    const bearer = `Bearer ${jwt}`;

    var myHeaders = new Headers();
    myHeaders.append("Authorization", bearer);
    myHeaders.append("Content-Type", "application/json");


    var nr_locuri = document.getElementsByName("nr_locuri")[0].value;
    var special = document.getElementsByName("specializare")[0].value;


    if (special == "" ) {
      alert("Specialisation is missing!");
      return;
    }
    if (nr_locuri == "") {
      alert("Numar locuri is missing!" + special);
      return;
    }


    var raw = JSON.stringify({
      denumire: special,
      nr_locuri: nr_locuri,
    });

    var requestOptions = {
      method: "POST",
      headers: myHeaders,
      body: raw,
      redirect: "follow",
    };

    fetch("http://localhost:8081/adauga-specializare", requestOptions)
      .then((response) => {
        const status = response.status;
        if (status != 200) {
          alert(
            "Specialisation was not created!"
          );
          window.location = "http://localhost:8000/edit_admitere.html?email=" + encodedEmail;
          return null;
        }
        alert("Specialisation added succesfully");
        window.location = "http://localhost:8000/edit_admitere.html?email=" + encodedEmail;
        return response.text();
      })
      .then((result) => console.log(result))
      .catch((error) => console.log("error", error));

  });




if (resetI2) {
  resetI2.addEventListener("click", (e) => {
    e.preventDefault();
    window.location = "http://localhost:8000/edit_admitere.html?email=" + encodedEmail;
  });
}


document.querySelector("#stergeSpec").addEventListener("click", function(event) {
    event.preventDefault();
    // do something for this submit button
       const bearer = `Bearer ${jwt}`;

       var myHeaders = new Headers();
       myHeaders.append("Authorization", bearer);
       myHeaders.append("Content-Type", "application/json");

       var select = document.getElementById("specs");
       var selectedOption = select.options[select.selectedIndex];
       var specializationName = selectedOption.text;


       if (specializationName == "" ) {
         alert("Specialisation is missing!");
         return;
       }


       var requestOptions = {
         method: "DELETE",
         headers: myHeaders,
         redirect: 'follow',
       };

       fetch(`http://localhost:8081/sterge-specializare/${specializationName}`, requestOptions)
         .then((response) => {
           const status = response.status;
           if (status != 200) {
             alert(
               "Specialisation was not deleted!"
             );
             window.location = "http://localhost:8000/edit_admitere.html?email=" + encodedEmail;
             return null;
           }
           alert("Specialisation deleted succesfully");
           window.location = "http://localhost:8000/edit_admitere.html?email=" + encodedEmail;
           return response.text();
         })
         .then((result) => console.log(result))
         .catch((error) => console.log("error", error));

     });


/*
if(stergeSpec){
        var select = document.getElementById("specs");
        var selectedOption = select.options[select.selectedIndex];
        var specializationName = selectedOption.text;

        var bearer = "Bearer ${jwt}";
        var xhr = new XMLHttpRequest();
        xhr.open("DELETE", "http://localhost:8081/sterge-specializare/${specializationName}", true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.setRequestHeader("Authorization", bearer);
        xhr.onreadystatechange = function () {
           if (xhr.readyState === 4 && xhr.status === 200) {
                alert(xhr.responseText);
                if (select.options.length > 0) {
                    select.remove(select.selectedIndex);
                    select.selectedIndex = -1;
                }
           } else if (xhr.readyState === 4) {
                alert(xhr.responseText);
           }

        };
        xhr.send();
}

*/

function createSelect() {
    var select = document.createElement("select");
    select.id = "specs";
    document.body.appendChild(select);
    getList();
}

function printTable() {
    var list = JSON.parse(res);
    var select = document.getElementById("specs");
    for (var i = 0; i < list.length; i++) {
        var option = document.createElement("option");
        option.value = i;
        option.text = list[i][0];
        select.appendChild(option);
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

