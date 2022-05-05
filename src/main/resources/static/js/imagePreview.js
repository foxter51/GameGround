$(document).ready(()=>{
    let currentHref = $(location).attr('href');
    $('#previewPhotoCreate').hide();
    $('#photoUpload').change(function(){
        const file = this.files[0];
        if (file){
            let reader = new FileReader();
            reader.onload = function(event){
                if(currentHref.includes('profile')) {$('#previewPhotoCreate').show().attr('src', event.target.result);}
                else $('#previewPhoto').attr('src', event.target.result);
            }
            reader.readAsDataURL(file);
        }
    });
});