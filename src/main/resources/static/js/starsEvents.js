$(function (){
    starsHoverEvent();
});

function starsHoverEvent(){
    $("#rateReview").hover(function (){
        $("#rStar1").hover(function (){
            fillStars(this.id)
        }, function (){
            $("#rStar1").removeClass("fa-solid").addClass("fa-regular");
        });
        $("#rStar2").hover(function (){
            fillStars(this.id);
        }, function (){
            $("#rStar2").removeClass("fa-solid").addClass("fa-regular");
        });
        $("#rStar3").hover(function (){
            fillStars(this.id);
        }, function (){
            $("#rStar3").removeClass("fa-solid").addClass("fa-regular");
        });
        $("#rStar4").hover(function (){
            fillStars(this.id);
        }, function (){
            $("#rStar4").removeClass("fa-solid").addClass("fa-regular");
        });
        $("#rStar5").hover(function (){
            fillStars(this.id);
        }, function (){
            $("#rStar5").removeClass("fa-solid").addClass("fa-regular");
        });
    }, function (){
        emptyStars();
    });
}

function fillStars(starID){
    switch (starID){
        case 'rStar5':
            $("#rStar5").removeClass("fa-regular").addClass("fa-solid");
        case 'rStar4':
            $("#rStar4").removeClass("fa-regular").addClass("fa-solid");
        case 'rStar3':
            $("#rStar3").removeClass("fa-regular").addClass("fa-solid");
        case 'rStar2':
            $("#rStar2").removeClass("fa-regular").addClass("fa-solid");
        case 'rStar1':
            $("#rStar1").removeClass("fa-regular").addClass("fa-solid");
            break;
    }
}

function emptyStars(){
    $("#rStar1").removeClass("fa-solid").addClass("fa-regular");
    $("#rStar2").removeClass("fa-solid").addClass("fa-regular");
    $("#rStar3").removeClass("fa-solid").addClass("fa-regular");
    $("#rStar4").removeClass("fa-solid").addClass("fa-regular");
    $("#rStar5").removeClass("fa-solid").addClass("fa-regular");
}