$(function (){
    setActiveFilter();
});

function setActiveFilter(){
    let currentHref = $(location).attr('href');
    switch (true){
        case currentHref.includes('sort=dateDSC'):
            $("#dateDSC").addClass("active");
            break;
        case currentHref.includes('sort=ratingASC'):
            $("#ratingASC").addClass("active");
            break;
        case currentHref.includes('sort=ratingDSC'):
            $("#ratingDSC").addClass("active");
            break;
        case currentHref.includes('filter=ratingGE4'):
            $("#ratingGE4").addClass("active");
            break;
        default:
            $("#dateASC").addClass("active");
            break;
    }
}
