$(function (){
    writeReviewEvent();
    hideWriting();
});

function writeReviewEvent(){
    $("#reviewForm").hide();
    $("#writeReview").click(function (){
        $("#writeReview").hide();
        $("#reviewForm").show();
    });
}

function hideWriting(){
    $("#hideWrite").click(function (){
        $("#reviewForm").hide();
        $("#writeReview").show();
    })
}