function regCar() {
    var xhr = new XMLHttpRequest();

    var form = document.forms.form;
    var formData = {
        "carNumber": form.carNumber.value,
    }
    var json = JSON.stringify(formData);

    var url = document.location.protocol + '//' + document.location.host + '/RoadCameraWebapp/registeredCars';
    console.log(url);
    xhr.open(
        "POST",
        url,
        true
    );
    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    xhr.send(json);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== 4) {
            return
        }
        if (xhr.status === 200) {
            console.log(xhr.responseText);
            document.getElementById("setCar").innerHTML = xhr.responseText;
        } else {
            console.log('err', xhr.responseText);
            document.getElementById("setCar").innerHTML = xhr.responseText;
        }
    }
}

function count() {
    var xhr = new XMLHttpRequest();

    var url = document.location.protocol + '//' + document.location.host + '/RoadCameraWebapp/registeredCars/count';
    console.log(url);
    xhr.open(
        "GET",
        url,
        true
    );

    xhr.send();

    xhr.onreadystatechange = function () {
        if (xhr.readyState !== 4) {
            return
        }
        if (xhr.status === 200) {
            console.log(xhr.responseText);
            var a = [];
            a = JSON.parse(xhr.response);
            document.getElementById("textCount").innerHTML = a['registeredCarsCount'];
        } else {
            console.log('err', xhr.responseText);
        }
    }
}

function tableUp() {
    var xhr = new XMLHttpRequest();

    var form = document.forms.filter;

    var par = '';
    if (form.carNumber.value) {
        par += 'carNumber=' + form.carNumber.value;
    }
    if (par) par += '&';
    if (form.date.value) {
        par += 'date=' + form.date.value;
    }

    var url = document.location.protocol + '//' + document.location.host + '/RoadCameraWebapp/registeredCars?' +  par;
    console.log(url);
    xhr.open(
        'GET',
        url,
        true
    );

    xhr.send();

    xhr.onreadystatechange = function () {
        if (xhr.readyState !== 4) {
            return
        }
        if (xhr.status === 200) {
            console.log(xhr.responseText);
            var a = [];
            a = JSON.parse(xhr.response);
            var htmlData = '<table width="100%"><tbody>';
            htmlData += '<thead><tr><th>Car Number</th><th>Date</th></tr></thead>';
            b = 0;
            a.forEach(function (json) {
                if (b == 1) {
                    b--;
                } else {
                    b++;
                }
                htmlData += ('<tr ' + 'class="tr' + b + '">');
                htmlData += '<td>';
                htmlData += json['carNumber'];
                htmlData += '</td>';
                htmlData += '<td>';
                htmlData += json['date'];
                htmlData += '</td>';
                htmlData += '</tr>';
            });
            htmlData += '</tbody></table>';
            document.getElementById("table").innerHTML = htmlData;
        } else {
            console.log('err', xhr.responseText)
        }
    }
}

