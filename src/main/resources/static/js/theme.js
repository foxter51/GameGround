$(function (){
    $("#modeCheck").change(function (){
        if($('#modeCheck').prop('checked')) {
            localStorage.setItem('darkMode', 'true');
            setDarkMode();
            transitionAfterChange()
        } else{
            localStorage.setItem('darkMode', 'false');
            removeDarkMode();
            transitionAfterChange()
        }
    })
})

window.addEventListener('load', function(){
    if (localStorage.getItem('darkMode')==='true'){
        setDarkMode();
        $("#modeCheck").prop('checked', true);
    } else{
        removeDarkMode();
        $("#modeCheck").prop('checked', false);
    }
});

function setDarkMode(){
    $('body').addClass('bg-dark text-white');
    $(".card").addClass('bg-dark text-white border border-white');
}

function removeDarkMode(){
    $('body').removeClass('bg-dark text-white');
    $(".card").removeClass('bg-dark text-white border border-white');
}

function transitionAfterChange(){
    $("#viewProfile").css("transition", '1s');
    $(".card").css("transition", '1s');
}