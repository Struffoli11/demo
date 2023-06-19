function loadProfile(){
    var username = localStorage.getItem("username");
    var url = "http://172.31.6.4:8080/api/v1/users/"+username;
    const result = fetch(url, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
    }).then(response =>{
        if(response.status == 200){
            return response = response.json();
        }else if (response.status == 403){
            alert("please log in again");
            window.location.href = "auth.html";
        }
    }).then(response =>{
        const HTMLusername = document.getElementById("username");
            HTMLusername.innerHTML = username;

            const HTMLemail = document.getElementById("email");
            HTMLemail.innerHTML = response.email;
            
            const groupList = response.groups;
            for(i=0; i< groupList.length; i++){
                addGroupToTable(groupList[i]);
            }
    });
}


function addGroupToTable(group) {
    var table = document.getElementById('groups_table');

    var nextIndex = table.childNodes.length;
    /* var newlabel = document.createElement("label");
    newlabel.innerHTML = "Expences"; */
    var newRow = document.createElement("tr");
    var firstCell = document.createElement("td");
    var firstCellTextarea = document.createElement("input");
    var secondCell = document.createElement("td");
    var secondCellTextarea = document.createElement("input");
    var thirdCell = document.createElement("td");
    var thirdCellButton = document.createElement("input");

    firstCellTextarea.setAttribute("id", "GroupName" + nextIndex);
    firstCellTextarea.setAttribute("name", "GroupName" + nextIndex);
    firstCellTextarea.setAttribute("placeholder", "GroupName" + nextIndex);
    firstCellTextarea.setAttribute("th:field", "${questionAnswerSet.expence}");
    firstCellTextarea.setAttribute("style", "resize: none; width: 100%;");
    firstCellTextarea.setAttribute("type", "text");
    firstCellTextarea.value = group.groupName;
    firstCellTextarea.setAttribute("readonly", true);

    secondCellTextarea.setAttribute("id", "groupAdmin" + nextIndex);
    secondCellTextarea.setAttribute("name", "groupAdmin" + nextIndex);
    secondCellTextarea.setAttribute("placeholder", "groupAdmin" + nextIndex);
    secondCellTextarea.setAttribute("th:field", "${questionAnswerSet.expenceId}");
    secondCellTextarea.setAttribute("style", "resize: none; width: 100%;");
    secondCellTextarea.setAttribute("type", "text");
    secondCellTextarea.value = group.groupOwner;
    secondCellTextarea.setAttribute("readonly", true);


    thirdCellButton.setAttribute("id", "buttonId" + nextIndex);
    thirdCellButton.setAttribute("name", "button" + nextIndex);
    thirdCellButton.setAttribute("type", "button");
    thirdCellButton.setAttribute("value", "Group-Page");
    thirdCellButton.addEventListener('click', () => toGroup(group.groupName));


    firstCell.appendChild(firstCellTextarea);
    secondCell.appendChild(secondCellTextarea);
    thirdCell.appendChild(thirdCellButton);

    newRow.appendChild(firstCell);
    newRow.appendChild(secondCell);
    newRow.appendChild(thirdCell);

    table.appendChild(newRow);
}

function toGroup(groupName) {
    localStorage.setItem("groupName", groupName);
    window.location.href = "group-page.html?groupName="+groupName;
}