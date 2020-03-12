
var endpoint='';
(function() {
    /*
     http://www.html5rocks.com/ja/tutorials/file/dndfiles/
     http://www.pori2.net/html5/File/040.html
     */
    var print_img_id = 'print_img';
    var print_DataURL_id = 'print_DataURL';
    if (checkFileApi()){
        
        var dropZone = document.getElementById('drop-zone');
        dropZone.addEventListener('dragover', handleDragOver, false);
        dropZone.addEventListener('drop', handleDragDropFile, false);
    }
    
    function checkFileApi() {
        // Check for the various File API support.
        if (window.File && window.FileReader && window.FileList && window.Blob) {
            // Great success! All the File APIs are supported.
            return true;
        }
        alert('The File APIs are not fully supported in this browser.');
        return false;
    }
    
    function selectReadfile(e) {
        var file = e.target.files;
        var reader = new FileReader();
       
        reader.readAsDataURL(file[0]);
      
        reader.onload = function(){
            readImage(reader, print_img_id, print_DataURL_id);
        }
    }
   
    function handleDragOver(e) {
        e.stopPropagation();
        e.preventDefault();
        e.dataTransfer.dropEffect = 'copy'; // Explicitly show this is a copy.
    }
    function handleDragDropFile(e) {
        e.stopPropagation();
        e.preventDefault();
        var files = e.dataTransfer.files; // FileList object.
        var file = files[0];
        var reader = new FileReader();
        
        reader.readAsDataURL(file);
       
        reader.onload = function(){
            readImage(reader, print_img_id, print_DataURL_id);
        }
    }
   
    function readImage(reader, print_image_id, print_DataURL_id ){
        
        var result_DataURL = reader.result;
       
        var img = document.getElementById('image');
        var src = document.createAttribute('src');
        src.value = result_DataURL;
        img.setAttributeNode(src);
        document.getElementById(print_DataURL_id).value = result_DataURL;
        printWidthHeight('image', 'width-height');
        var jsn={'base64':result_DataURL.replace('data:image/jpeg;base64,',"")};
        $.ajax({
            url: endpoint+'getAA',
            dataType: 'application/json',
            data: jsn,
            type: 'POST',
            success: function(str){
                $('#editor').text(str);
            }
        });


    }
   
    function printWidthHeight( img_id, width_height_id ) {
        var img = document.getElementById(img_id);
        var w = img.naturalWidth;
        var h = img.naturalHeight;
        document.getElementById(width_height_id).innerHTML = 'width:' + w + ' height:' + h;
    }
})();