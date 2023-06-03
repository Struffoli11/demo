
var url = "http://172.31.6.4:8080/CKexpences";

function createExpence() {
    try {
        var payingMembersMap = new Map();
        var table = document.getElementById('dataTable');
        try {
            var rowCount = table.rows.length;
            for (var i = 0; i < rowCount; i++) {
                var row = table.rows[i];
                var username = row.cells[1].childNodes[0].value;
                var value = row.cells[2].childNodes[0].value;
                if (null != username && null != value) {
                    /* add new member to payingMembers */
                    payingMembersMap.set(username, value);
                    /*  alert("member: "+username+" added to map."); */
                }
            }
        } catch (e) {
            alert(e);
        }/*purtoppo nei dati non possiamo aggiungere direttamente come "parametro" una Map
                    poichè questa non è compatibile con il metodo JSON.stringify(data)
                    che usiamo per trasformare i dati che vogliamo passare alla api
                    dal file html. Per questo payingMembers viene trasformato in un oggetto che poi
                    all'interno della api viene convertito in una HashMap<String, String>.
                    Problemi simili si hanno per quanto riguarda i numeri. In javascript
                    a quanto pare non si fa distinzione tra interi float e double
                    ossia 1 = 1.00 senza distinzione tra i due
                    Di conseguenza non posso passare alla api dei double (come facevamo da postman)
                    ma siamo costretti a passare delle stringhe, e successivamente all'interno della api
                    queste devono essere convertite in valori in virgola mobile.
                    document.getElementById("inp_ExpenceCost").value restituisce una stringa infatti
                     */
        let
            data = {
                cost: document.getElementById("inp_ExpenceCost").value,
                description: document.getElementById("inp_ExpenceName").value,
                date: document.getElementById("inp_ExpenceDate").value,
                payingMembers: Object.assign({}, ...Array.from(payingMembersMap.entries()).map(([k, v]) => ({ [k]: v }))),
                groupName: "MyGroup"
            };


        fetch(url, {
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        }).then(res => {
            console.log("Request complete! response:", res);
        });
    } catch (e) {
        alert(e);
    }

}

//questo metodo aggiunge una riga alla tabella per
//aggiungere le quote dei membri
function addRow(tableID) {

    var table = document.getElementById(tableID);

    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);

    var colCount = table.rows[0].cells.length;

    for (var i = 0; i < colCount; i++) {

        var newcell = row.insertCell(i);

        newcell.innerHTML = table.rows[0].cells[i].innerHTML;
        //alert(newcell.childNodes);
        switch (newcell.childNodes[0].type) {
            case "text":
                newcell.childNodes[0].value = "";
                break;
            case "checkbox":
                newcell.childNodes[0].checked = false;
                break;
            case "number":
                newcell.childNodes[0].value = 0.00;
                break;
        }
    }
}

//questo metodo cancella tutte le righe spuntate dalla tabella
//che raccoglie le quote dei membri
function deleteRow(tableID) {
    try {
        var table = document.getElementById(tableID);
        var rowCount = table.rows.length;

        for (var i = 0; i < rowCount; i++) {
            var row = table.rows[i];
            var chkbox = row.cells[0].childNodes[0];
            if (null != chkbox && true == chkbox.checked) {
                if (rowCount <= 1) {
                    alert("Cannot delete all the rows.");
                    break;
                }
                table.deleteRow(i);
                rowCount--;
                i--;
            }


        }
    } catch (e) {
        alert(e);
    }
}