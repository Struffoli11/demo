function homepage() {
    window.location.href = "index.html";
}


function loadPage() {
    const queryString = window.location.search;
    console.log(queryString);
    // ?groupName=groupName

    const urlParams = new URLSearchParams(queryString);
    const groupName = urlParams.get('groupName')
    var url = "http://172.31.6.4:8080/api/v1/groups/" + groupName;
    const myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');
    myHeaders.append("Authorization", "Bearer " + localStorage.getItem("token"));

    const result = fetch(url, {
        method: "GET",
        headers: myHeaders,
    })
        .then(response => {
            if(response.status == 403 || response.status == 401 ){
                alert("please log in again");
                window.location.href = "auth.html";
            }
            else if (response.status == 200) {
                return response = response.json();
            }
            else{
                alert("please try again, something went wrong. Try to modify the form");
            }
        }).then(response =>{
            /* the application either returns a GroupInfoDto 
                *    or a GroupPageDto
                */
            const groupName = document.querySelector("h1");
            groupName.innerHTML = "Group Name => " + response.groupName + " -  Admin Username=> " + response.groupOwner;


            var membersArray = JSON.stringify(response.members);
            localStorage.setItem("members", membersArray);
            for (i = 0; i < response.members.length; i++) {
                add_fieldsMembers(response.members[i]);
            }


            if (response.idGroup != null) {
                alert(true);//this user is a member of this group
                localStorage.setItem("idGroup", response.idGroup);
                /* so that it can be retrieved when clicked in html*/

                localStorage.setItem("groupName", response.groupName);
                /**we need it later to create a new expence */

                var membersArray = JSON.stringify(response.members);
                localStorage.setItem("members", membersArray);
                for (i = 0; i < response.members.length; i++) {
                    add_fieldsPayingMembers(response.members[i]);
                }
                /* so that they can be used to create the table */


                var expence_lenght = response.expences.length;
                for (i = 0; i < expence_lenght; i++) {
                    add_fieldsExpence(response.expences[i]);
                }

                document.getElementById("become_member").hidden = true;
                if (localStorage.getItem("username") != response.groupOwner) {
                    document.getElementById("delete_group").hidden = true;
                }
                return;
            }else{
            document.getElementById("expences_table").hidden = true;
            document.getElementById("secret").hidden = true;
            document.getElementById("create_expence_div").hidden = true;
            document.getElementById("delete_group").hidden = true;
            document.getElementById("become_member").show = true;
            }
        });
}




function add_fieldsPayingMembers(member) {
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
    firstCellTextarea.setAttribute("type", "text");


    firstCell.appendChild(firstCellTextarea);
    secondCell.appendChild(secondCellTextarea);

    newRow.appendChild(firstCell);
    newRow.appendChild(secondCell);

    table.appendChild(newRow);
}

function add_fieldsMembers(member) {
    var table = document.getElementById('members_table');

    var nextIndex = table.childNodes.length;


    var newlabel = document.createElement("label");
    newlabel.innerHTML = "Members";
    var newRow = document.createElement("tr");
    var firstCell = document.createElement("td");
    var firstCellTextarea = document.createElement("input");

    firstCellTextarea.setAttribute("id", "member" + nextIndex);
    firstCellTextarea.setAttribute("name", "Member" + nextIndex);
    firstCellTextarea.setAttribute("placeholder", "Member" + nextIndex);
    firstCellTextarea.setAttribute("th:field", "${questionAnswerSet.member}");
    firstCellTextarea.setAttribute("style", "resize: none; width: 100%;");
    firstCellTextarea.setAttribute("type", "text");
    firstCellTextarea.value = member;
    firstCellTextarea.setAttribute("readonly", true);

    firstCell.appendChild(firstCellTextarea);

    newRow.appendChild(firstCell);

    table.appendChild(newRow);
}

function add_fieldsExpence(expence) {
    var table = document.getElementById('expences_table');

    var nextIndex = table.childNodes.length;
    var newlabel = document.createElement("label");
    newlabel.innerHTML = "Expences";
    var newRow = document.createElement("tr");
    var firstCell = document.createElement("td");
    var firstCellTextarea = document.createElement("input");
    var secondCell = document.createElement("td");
    var secondCellTextarea = document.createElement("input");
    var thirdCell = document.createElement("td");
    var thirdCellButton = document.createElement("input");

    firstCellTextarea.setAttribute("id", "Expence" + nextIndex);
    firstCellTextarea.setAttribute("name", "Expence" + nextIndex);
    firstCellTextarea.setAttribute("placeholder", "Expence" + nextIndex);
    firstCellTextarea.setAttribute("th:field", "${questionAnswerSet.expence}");
    firstCellTextarea.setAttribute("style", "resize: none; width: 100%;");
    firstCellTextarea.setAttribute("type", "text");
    firstCellTextarea.value = expence.description;
    firstCellTextarea.setAttribute("readonly", true);

    secondCellTextarea.setAttribute("id", "expenceId" + nextIndex);
    secondCellTextarea.setAttribute("name", "expenceId" + nextIndex);
    secondCellTextarea.setAttribute("placeholder", "expenceId" + nextIndex);
    secondCellTextarea.setAttribute("th:field", "${questionAnswerSet.expenceId}");
    secondCellTextarea.setAttribute("style", "resize: none; width: 100%;");
    secondCellTextarea.setAttribute("type", "text");
    secondCellTextarea.value = expence.id;
    secondCellTextarea.setAttribute("readonly", true);


    thirdCellButton.setAttribute("id", "buttonId" + nextIndex);
    thirdCellButton.setAttribute("name", "button" + nextIndex);
    thirdCellButton.setAttribute("type", "button");
    thirdCellButton.setAttribute("value", "Expence-Page");
    thirdCellButton.addEventListener('click', () => toExpence(expence.id));


    firstCell.appendChild(firstCellTextarea);
    secondCell.appendChild(secondCellTextarea);
    thirdCell.appendChild(thirdCellButton);

    newRow.appendChild(firstCell);
    newRow.appendChild(secondCell);
    newRow.appendChild(thirdCell);

    table.appendChild(newRow);
}





/* function toExpence(ev) {
    const target = ev.target.id;

    const myTable = document.getElementsByTagName("expences_table");
    var tableLength = myTable[0].childNodes.length;
    for (i = 2; i < tableLength; i++) {
        const tr = myTable[0].childNodes[i];
        const descriptionColumn = tr.childNodes[0];
        const description = descriptionColumn.childNodes[0].value;
        const idColumn = tr.childNodes[1];
        const id = idColumn.childNodes[0].value;
        localStorage.setItem("expenceId", id);
        window.location.href = "expence-page.html";
    }
}
 */
function toExpence(id) {
    localStorage.setItem("expenceId", id);
    window.location.href = "expence-page.html";
}


