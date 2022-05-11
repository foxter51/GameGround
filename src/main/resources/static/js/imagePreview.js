$(document).ready(()=>{
    let file;
    let reader = new FileReader();

    let nativeReviewPhoto = $('#previewPhotoEdit').attr('src');
    let nativeProfilePicture = $('#profilePicture').attr('src');

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
    $("#cancelPictureEdit").click(function (){
        $("#previewPhotoEdit").attr('src', nativeReviewPhoto);
        $('#photoUploadEdit').val('');
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
        $("#profilePicture").attr('src', nativeProfilePicture);
        $('#changeProfilePicture').val('');
    })
});