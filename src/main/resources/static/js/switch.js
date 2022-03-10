$(function (){
    darkModeSwitchEvent();
    languageSwitchEvent();
});

window.addEventListener('load', function(){
    if (localStorage.getItem('darkMode')==='true'){
        setDarkMode();
        $("#modeCheck").prop('checked', true);
    } else{
        removeDarkMode();
        $("#modeCheck").prop('checked', false);
    }
    if(localStorage.getItem('lang')==='true'){
        $("#languageCheck").prop('checked', true);
        $('[name="cardCol"]').removeClass("col-sm-3").addClass("col-sm-4");
    } else{
        $("#languageCheck").prop('false');
        $('[name="cardCol"]').removeClass("col-sm-4").addClass("col-sm-3");
    }
});

function darkModeSwitchEvent(){
    $("#modeCheck").change(function (){
        if($('#modeCheck').prop('checked')) {
            localStorage.setItem('darkMode', 'true');
            setDarkMode();
            transitionAfterChange();
        } else{
            localStorage.setItem('darkMode', 'false');
            removeDarkMode();
            transitionAfterChange();
        }
    });
}

function languageSwitchEvent(){
    $("#languageCheck").change(function (){
        if($('#languageCheck').prop('checked')) {
            localStorage.setItem('lang', 'true');
            setRussian();
        } else{
            localStorage.setItem('lang', 'false');
            setEnglish();
        }
    });
}

function setDarkMode(){
    $('body').addClass('bg-dark text-white');
    $(".card").addClass('bg-dark text-white border border-white');
    $('[name="signs"]').removeClass('text-dark').addClass("text-white");
    $('.list-group a').removeClass('list-group-item-light').addClass('list-group-item-dark');
}

function removeDarkMode(){
    $('body').removeClass('bg-dark text-white');
    $(".card").removeClass('bg-dark text-white border border-white');
    $('[name="signs"]').removeClass('text-white').addClass("text-dark");
    $('.list-group a').removeClass('list-group-item-dark').addClass('list-group-item-light');
}

function transitionAfterChange(){
    $("#viewProfile").css("transition", '1s');
    $(".card").css("transition", '1s');
}

function setRussian(){
    window.location.replace('?lang=ru');
}

function setEnglish(){
    window.location.replace('?lang=en');
}

