let cancelEmptyStars = true;

$(function (){
    starsClickEvent();
    starsHoverEvent();
});

function starsHoverEvent(){
    $("#rateReview").hover(function (){
        cancelEmptyStars = true;
        $("#rStar1").hover(function (){
            fillStars(this.id)
        }, function (){
            if(cancelEmptyStars) $("#rStar1").removeClass("fa-solid").addClass("fa-regular");
        });
        $("#rStar2").hover(function (){
            fillStars(this.id);
        }, function (){
            if(cancelEmptyStars) $("#rStar2").removeClass("fa-solid").addClass("fa-regular");
        });
        $("#rStar3").hover(function (){
            fillStars(this.id);
        }, function (){
            if(cancelEmptyStars) $("#rStar3").removeClass("fa-solid").addClass("fa-regular");
        });
        $("#rStar4").hover(function (){
            fillStars(this.id);
        }, function (){
            if(cancelEmptyStars) $("#rStar4").removeClass("fa-solid").addClass("fa-regular");
        });
        $("#rStar5").hover(function (){
            fillStars(this.id);
        }, function (){
            if(cancelEmptyStars) $("#rStar5").removeClass("fa-solid").addClass("fa-regular");
        });
    }, function (){
        if(cancelEmptyStars){
            emptyStars();
        }
    });
}

function starsClickEvent(){
    $("#rateReview").click(function(){
        cancelEmptyStars=false;
        $("#rStar1").click(function (){
            fillStars(this.id)
        });
        $("#rStar2").click(function (){
            fillStars(this.id);
        });
        $("#rStar3").click(function (){
            fillStars(this.id);
        });
        $("#rStar4").click(function (){
            fillStars(this.id);
        });
        $("#rStar5").click(function (){
            fillStars(this.id);
        });
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