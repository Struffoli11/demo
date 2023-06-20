
function doSearch() {
    const groupName = document.getElementById("group_searchbar");
    const expenceDescription = document.getElementById("expence_searchbar");

    if (groupName.value != "") {
        /* vai alla pagina di questo gruppo */
        localStorage.setItem("groupName", groupName.value)
        window.location.href = "group-page.html";
    }
    alert("Enter a Group Name");
}

function createGroup() {
    var groupName = prompt("Enter a new group name");

    const groupFormDto = {
        groupName: groupName,
        groupOwner: localStorage.getItem("username")
    };

    var url = "http://172.31.6.4:8080/api/v1/groups";
    const result = fetch(url, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        body: JSON.stringify(groupFormDto)
    })
        .then(response => {
            if (response.status == 200) {
                response = response.json();
                localStorage.setItem("groupName", groupName);
                window.location.href = "group-page.html";
                /* window.location.href = "homepage.html"; */
            }else if (response.status == 401){
                alert("please log in again");
                window.location.href = "auth.html";
            }else if (response.status == 403){
                alert("please log in again");
                window.location.href = "auth.html";
            }else{
                alert("please try again");
            }
        });
}