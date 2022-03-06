$(function (){
    $("#reviewForm").hide();
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
    });
    $("#languageCheck").change(function (){
        if($('#languageCheck').prop('checked')) {
            localStorage.setItem('lang', 'true');
            setRussian();
        } else{
            localStorage.setItem('lang', 'false');
            setEnglish();
        }
    });
    $("#writeReview").click(function (){
        $("#writeReview").hide();
        $("#reviewForm").show();
    });
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
        setRussian();
        $("#languageCheck").prop('checked', true);
    } else{
        setEnglish();
        $("#languageCheck").prop('false');
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

function setRussian(){
    $('[name="cardCol"]').removeClass("col-sm-3").addClass("col-sm-4");
    $("#homeLabel").text('Главная');
    $("#reviewsLabel").text('Обзоры');
    $("#regFormLabel").text('Форма регистрации');
    $("#authFirst").text('Имя');
    $("#authLast").text('Фамилия');
    $("#authPass").text('Пароль');
    $("#regButton").text('Регистрация');
    $("#orLabel").text('ИЛИ');
    $("#logFormLabel").text('Форма входа');
    $("#badLogLabel").text('Неверные данные или Вы были заблокированы!');
    $("#logButton").text('Войти');
    $("#logButtonLabel").text('Вход');
    $("#regButtonLabel").text('Регистрация');
    $("#roleLabel").text('Редактор');
    $("#nameLabel").text('Полное имя');
    $("#regLabel").text('Регистрация');
    $("#lastLoginLabel").text('Посл. вход');
    $("#darkModeLabel").text('Темная тема');
    $("#checkOptMode").text('Вкл.');
    $("#langLabel").text('Язык');
    $("#checkOptLang").text('Русский');
    $("#titleLabel").text('Название обзора: ');
    $("#title").attr('placeholder', 'Название');
    $('[name="genre"]').text('Жанр: ');
    $("#genreName").attr('placeholder', 'Жанр');
    $("#tagList").attr('placeholder', 'Теги');
    $('[name="tags"]').text('Теги: ');
    $("#contentLabel").text('Содержимое: ');
    $("#content").attr('placeholder', 'Текст');
    $("#reviewButton").text('Создать');
    $("#reviewUpdateButton").text('Обновить');
    $("#addCommentButton").text('Добавить');
    $("#rate").text('Рейтинг: ');
    $("#author").text('Автор: ');
    $("#footerLabel").text('Все права защищены. 2022');
}

function setEnglish(){
    $('[name="cardCol"]').removeClass("col-sm-4").addClass("col-sm-3");
    $("#homeLabel").text('Home');
    $("#reviewsLabel").text('Reviews');
    $("#regFormLabel").text('Registration form');
    $("#authFirst").text('First name');
    $("#authLast").text('Last name');
    $("#authPass").text('Password');
    $("#regButton").text('Register');
    $("#orLabel").text('OR');
    $("#logFormLabel").text('Login form');
    $("#badLogLabel").text('Bad credentials or you have been blocked!');
    $("#logButton").text('Login');
    $("#logButtonLabel").text('Login');
    $("#regButtonLabel").text('Register');
    $("#roleLabel").text('Editor');
    $("#nameLabel").text('Full name');
    $("#regLabel").text('Registered');
    $("#lastLoginLabel").text('Last login');
    $("#darkModeLabel").text('Dark mode');
    $("#checkOptMode").text('On');
    $("#langLabel").text('Language');
    $("#checkOptLang").text('Russian');
    $("#titleLabel").text('Review name: ');
    $("#title").attr('placeholder', 'Name');
    $('[name="genre"]').text('Genre: ');
    $("#genreName").attr('placeholder', 'Genre');
    $('[name="tags"]').text('Tags: ');
    $("#tagList").attr('placeholder', 'Tags');
    $("#contentLabel").text('Content: ');
    $("#content").attr('placeholder', 'Content');
    $("#reviewButton").text('Create');
    $("#reviewUpdateButton").text('Update');
    $("#addCommentButton").text('Add');
    $("#rate").text('Rate: ');
    $("#author").text('Author: ');
    $("#footerLabel").text('All rights are reserved. 2022');
}

