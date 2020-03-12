
    $("#input-1").fileinput({
        uploadUrl:'/files/icon/upload',
        maxFileSize:5000,
        maxFilesNum:1,
        browseClass: "btn btn-primary btn-block",
        showCaption: false,
        showRemove: false,
        showUpload: true,
        autoReplace: true,
        allowedFileExtensions:['jpg','jpeg','png','gif']
    });
