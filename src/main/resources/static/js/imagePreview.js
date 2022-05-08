$(document).ready(()=>{
    let file;
    let reader = new FileReader();

    $('#previewPhotoCreate').hide();

    $('#photoUploadCreate').change(function(){
        file = this.files[0];
        if (file){
            reader.onload = function(event){
                $('#previewPhotoCreate').show().attr('src', event.target.result);
            }
        }
        reader.readAsDataURL(file);
    });
    $('#photoUploadEdit').change(function (){
        file = this.files[0];
        if (file){
            reader.onload = function(event){
                $('#previewPhotoEdit').attr('src', event.target.result);
            }
        }
        reader.readAsDataURL(file);
    });
    $('#changeProfilePicture').change(function (){
        file = this.files[0];
        if (file){
            reader.onload = function(event){
                $('#profilePicture').attr('src', event.target.result);
            }
        }
        reader.readAsDataURL(file);
    });
    $("#cancelPicture").click(function (){
        location.reload();
    })
});