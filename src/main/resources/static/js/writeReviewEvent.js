$(function (){
    writeReviewEvent();
    hideWriting();
});

function writeReviewEvent(){
    $("#write").click(function (){
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