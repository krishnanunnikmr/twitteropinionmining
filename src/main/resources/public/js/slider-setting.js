
$("#slider").slider();
$("#slider").on("slide", function(slideEvt) {
    $("#sliderVal").text(slideEvt.value);
});