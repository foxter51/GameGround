function displayRate(quantity, size){
    quantity = Math.floor(quantity);

    $("#reviewRate").append(function (){
        let stars = "";
        for (let i = 0; i < 5; i++) {
            i < quantity ? stars += '<i class="fa-solid fa-star '+size+'"></i>' : stars += '<i class="fa-regular fa-star '+size+'"></i>';
        }
        return stars;
    }).removeAttr('id');
}