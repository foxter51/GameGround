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
    } else{
        $("#languageCheck").prop('false');
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
    $("#footerDiv").removeClass('bg-light text-dark').addClass('text-white').css('background-color', 'rgb(37,40,42)');
    $("#headerDiv").removeClass('bg-light text-dark').addClass('text-white').css('background-color', 'rgb(37,40,42)');
    $("#nameLabel").removeClass('text-dark').addClass('text-white');
}

function removeDarkMode(){
    $('body').removeClass('bg-dark text-white');
    $(".card").removeClass('bg-dark text-white border border-white');
    $('[name="signs"]').removeClass('text-white').addClass("text-dark");
    $('.list-group a').removeClass('list-group-item-dark').addClass('list-group-item-light');
    $("#footerDiv").removeClass('text-white').css('background-color', '').addClass('bg-light text-dark');
    $("#headerDiv").removeClass('text-white').css('background-color', '').addClass('bg-light text-dark');
    $("#nameLabel").removeClass('text-white').addClass('text-dark');
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

