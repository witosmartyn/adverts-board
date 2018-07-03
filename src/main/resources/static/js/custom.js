function replaceUrlParam(url, paramName, paramValue){
    console.debug("url = "+url);
    console.debug("paramName = "+paramName);
    console.debug("selectedOption = "+paramValue);

    if (paramValue == null) {
        paramValue = '';
    }
    var pattern = new RegExp('\\b('+paramName+'=).*?(&|$)');
    if (url.search(pattern)>=0) {
        return url.replace(pattern,'$1' + paramValue + '$2');
    }
    url = url.replace(/\?$/,'');
    return url + (url.indexOf('?')>0 ? '&' : '?') + paramName + '=' + paramValue;
}


function topNotyfy(msg) {
    $.notify(msg, {
        color: "#fff",
        background: "#D44950",
        animationType: "fade",
        delay: 2500,
        verticalAlign: "bottom"
    });
}

$('#myForm').submit(function(){
    $('input[name="_csrf"]').prop('disabled', true);
});


// item validation
function validateItemForm(msg) {
    console.log("validate add Form");
    var fr = document.forms["add-form"];
    var name = fr["name"].value;
    var descr = fr["description"].value;

    if ( name.length <1) {
        $("#name").text(msg);
        topNotyfy(msg);
        console.log(msg);
        return false;

    }
    if (descr.length <1) {
        $("#description").text(msg);
        topNotyfy(msg);
        console.log(msg);

        return false;
    }

    console.log("Success");
    return true;
}




// readyPage
$(document).ready(function () {

    $('[data-toggle="tooltip"]').tooltip(
        {
            animation: true,
            delay: {show: 100, hide: 50},
        }
    );

    $("#locales").change(function () {
        var selectedOption = $('#locales').val();
        if (selectedOption != '') {
            var href =window.location.href;

            // window.location.replace('/?lang=' + selectedOption);
            var newURL= replaceUrlParam(href,'lang',selectedOption)
            console.debug(newURL);
            window.location.replace(newURL);

        }





    });
});
