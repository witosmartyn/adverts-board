function test(msg) {
    alert(msg);
}

function topNotyfy(msg) {
    $.notify(msg, {
        color: "#fff",
        background: "#D44950",
        animationType: "fade",
        delay: 2500,
        verticalAlign: "top"
    });
}
// function exclude_csrf(){
//     $('input[name="_csrf"]').prop('disabled', true);
// };

$('#myForm').submit(function(){
    $('input[name="_csrf"]').prop('disabled', true);
});

function submitExclude_csrf(myForm){
    $(myForm).submit(function(){
        $('input[name="_csrf"]').prop('disabled', true);
    });
};
