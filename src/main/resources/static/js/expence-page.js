function homepage() {
    window.location.href = "index.html";
}
function loadPage() {
    const id = localStorage.getItem("expenceId");
    const descriptionHTML_Header = document.querySelector("h1");
    const costHTML_Header = document.querySelector("h2");
    const dateHTML_Header = document.querySelector("h3");

    var url = "http://172.31.6.4:8080/api/v1/expences/" + id;
    const myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');
    myHeaders.append("Authorization", "Bearer " + localStorage.getItem("token"));

    fetch(url, {
        method: "GET",
        headers: myHeaders
    })
        .then((response) => {
            if (response.status == 200) {
                return response.json();
            }
            else if (response.status == 403){
                alert("please log in again");
                window.location.href = "auth.html";
            }
            else{
                alert("please try again, modify something in the form");
            }
        }).then(expence =>{
            
            descriptionHTML_Header.innerHTML = expence.description;
            costHTML_Header.innerHTML = expence.cost;
            dateHTML_Header.innerHTML = expence.date;


            const map = new Map(Object.entries(expence.payingMembers));
            const iterator = map.keys();
            for (const member of iterator) {
                payingMembersTable(member, map.get(member));
            }

            if(expence.debts != null){
            const debts = expence.debts;
            const debtList_length = debts.length;
            for (i=0; i<debtList_length; i++) {
                debtsTable(debts[i]);
            }
        }

            if(expence.owners != null){
            const mapOwners = new Map(Object.entries(expence.owners));
            const ownersIterator = mapOwners.keys();
            for (const owner of ownersIterator) {
                ownersTable(owner, map.get(owner));
            }
        }

            if (expence.chest == null) {
                var img = document.getElementById("chestimg");
                img.setAttribute("src", "../immagini/empty-chest.png")
                document.getElementById("message").innerHTML = "Tutti hanno pagato e prelevato";
                document.querySelector("progress").hidden = true;
                document.getElementById("withdraw_button").hidden = true;
                document.getElementById("deposit_button").hidden = true;
            } else {
                const chestMap = new Map(Object.entries(expence.chest));
                const currentValue = chestMap.get("currentValue");
                const isOpen = chestMap.get("isOpen");
                const max_amount = chestMap.get("max_amount");
                const percentage = chestMap.get("percentage");

                const progressBar = document.querySelector("progress");
                /* progressBar.setAttribute("value", percentage); */
                progressBar.value = percentage
                if (isOpen == "Y") {
                    document.getElementById("message").innerHTML = "La cassa è aperta";
                } else {
                    var message = document.getElementById("message");
                    let diff = max_amount - currentValue;
                    message.innerHTML += diff;
                }
            }
        });
}

function payingMembersTable(member, cost) {
    var table = document.getElementById('myTable');

    var nextIndex = table.childNodes.length;

    var newRow = document.createElement("tr");
    var firstCell = document.createElement("td");
    var firstCellTextarea = document.createElement("input");
    var secondCell = document.createElement("td");
    var secondCellTextarea = document.createElement("input");

    firstCellTextarea.setAttribute("id", "member" + nextIndex);
    firstCellTextarea.setAttribute("name", "Member" + nextIndex);
    firstCellTextarea.setAttribute("placeholder", "Member" + nextIndex);
    firstCellTextarea.setAttribute("th:field", "${questionAnswerSet.member}");
    firstCellTextarea.setAttribute("style", "resize: none; width: 100%;");
    firstCellTextarea.setAttribute("type", "text");
    firstCellTextarea.value = member;
    firstCellTextarea.setAttribute("readonly", true);

    secondCellTextarea.setAttribute("id", "cost" + nextIndex);
    secondCellTextarea.setAttribute("name", "Cost" + nextIndex);
    secondCellTextarea.setAttribute("placeholder", "Cost" + nextIndex);
    secondCellTextarea.setAttribute("th:field", "${questionAnswerSet.cost}");
    secondCellTextarea.setAttribute("style", "resize: none; width: 100%;");
    secondCellTextarea.setAttribute("type", "text");
    secondCellTextarea.value = cost;
    secondCellTextarea.setAttribute("readonly", true);


    firstCell.appendChild(firstCellTextarea);
    secondCell.appendChild(secondCellTextarea);

    newRow.appendChild(firstCell);
    newRow.appendChild(secondCell);

    table.appendChild(newRow);
}

function ownersTable(member, cost) {
    var table = document.getElementById('owners_table');

    var nextIndex = table.childNodes.length;

    var newRow = document.createElement("tr");
    var firstCell = document.createElement("td");
    var firstCellTextarea = document.createElement("input");
    var secondCell = document.createElement("td");
    var secondCellTextarea = document.createElement("input");

    firstCellTextarea.setAttribute("id", "member" + nextIndex);
    firstCellTextarea.setAttribute("name", "Member" + nextIndex);
    firstCellTextarea.setAttribute("placeholder", "Member" + nextIndex);
    firstCellTextarea.setAttribute("th:field", "${questionAnswerSet.member}");
    firstCellTextarea.setAttribute("style", "resize: none; width: 100%;");
    firstCellTextarea.setAttribute("type", "text");
    firstCellTextarea.value = member;
    firstCellTextarea.setAttribute("readonly", true);

    secondCellTextarea.setAttribute("id", "cost" + nextIndex);
    secondCellTextarea.setAttribute("name", "Cost" + nextIndex);
    secondCellTextarea.setAttribute("placeholder", "Cost" + nextIndex);
    secondCellTextarea.setAttribute("th:field", "${questionAnswerSet.cost}");
    secondCellTextarea.setAttribute("style", "resize: none; width: 100%;");
    secondCellTextarea.setAttribute("type", "text");
    secondCellTextarea.value = cost;
    secondCellTextarea.setAttribute("readonly", true);


    firstCell.appendChild(firstCellTextarea);
    secondCell.appendChild(secondCellTextarea);

    newRow.appendChild(firstCell);
    newRow.appendChild(secondCell);

    table.appendChild(newRow);
}

function debtsTable(debt) {
    var table = document.getElementById('debts_table');

    var nextIndex = table.childNodes.length;

    var newRow = document.createElement("tr");
    var firstCell = document.createElement("td");
    var firstCellTextarea = document.createElement("input");
    var secondCell = document.createElement("td");
    var secondCellTextarea = document.createElement("input");

    firstCellTextarea.setAttribute("id", "debtor" + nextIndex);
    firstCellTextarea.setAttribute("name", "debtor" + nextIndex);
    firstCellTextarea.setAttribute("placeholder", "debtor" + nextIndex);
    firstCellTextarea.setAttribute("th:field", "${questionAnswerSet.debtor}");
    firstCellTextarea.setAttribute("style", "resize: none; width: 100%;");
    firstCellTextarea.setAttribute("type", "text");
    firstCellTextarea.value = debt.debtor;
    firstCellTextarea.setAttribute("readonly", true);

    secondCellTextarea.setAttribute("id", "cost" + nextIndex);
    secondCellTextarea.setAttribute("name", "Cost" + nextIndex);
    secondCellTextarea.setAttribute("placeholder", "Cost" + nextIndex);
    secondCellTextarea.setAttribute("th:field", "${questionAnswerSet.cost}");
    secondCellTextarea.setAttribute("style", "resize: none; width: 100%;");
    secondCellTextarea.setAttribute("type", "text");
    secondCellTextarea.value = debt.debt;
    secondCellTextarea.setAttribute("readonly", true);


    firstCell.appendChild(firstCellTextarea);
    secondCell.appendChild(secondCellTextarea);

    newRow.appendChild(firstCell);
    newRow.appendChild(secondCell);

    table.appendChild(newRow);
}

function deposit() {
    const id = localStorage.getItem("expenceId");
    var url = "http://172.31.6.4:8080/api/v1/expences/" + id + "/payment/";
    const myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');
    myHeaders.append("Authorization", "Bearer " + localStorage.getItem("token"));
    fetch(url, {
        method: "POST",
        headers: myHeaders
    }).then(response => response.json())
        .then((expence) => {
            window.location.href = "deposit.html";
        }).catch(error => {
            alert("Non sei un debitore");
            // Gestire l'errore in modo appropriato
        });

}

function withdraw() {
    const id = localStorage.getItem("expenceId");
    var url = "http://172.31.6.4:8080/api/v1/expences/" + id + "/withdraw/";
    const myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');
    myHeaders.append("Authorization", "Bearer " + localStorage.getItem("token"));
    fetch(url, {
        method: "GET",
        headers: myHeaders
    }).then(response => response.json())
        .then((expence) => {
            window.location.href = "withdraw.html";
        }).catch(error => {
            alert("Non hai il permesso di accedere alla cassa");
            // Gestire l'errore in modo appropriato
        });

}

