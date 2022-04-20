$(function (){
    writeReviewEvent();
    hideWriting();
});

function writeReviewEvent(){
    $("#reviewForm").hide();
    $("#writeReview").click(function (){
        $("#writeReview").hide();
        $("#reviewForm").fadeIn();
    });
}

function hideWriting(){
    $("#hideWrite").click(function (){
        $("#reviewForm").fadeOut();
        $("#writeReview").show();
    })
}