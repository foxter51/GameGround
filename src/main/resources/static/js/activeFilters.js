$(function (){
    setActiveFilter();
});

function setActiveFilter(){
    let currentFilter = ($(location).attr('href')).split('/', 4)[3];
    switch (currentFilter){
        case '':
            $("#dateASC").addClass("active");
            break;
        case 'sort=dateDSC':
            $("#dateDSC").addClass("active");
            break;
        case 'sort=ratingASC':
            $("#ratingASC").addClass("active");
            break;
        case 'sort=ratingDSC':
            $("#ratingDSC").addClass("active");
            break;
        case 'filter=rating_GE4':
            $("#ratingGE4").addClass("active");
            break;
    }
}
