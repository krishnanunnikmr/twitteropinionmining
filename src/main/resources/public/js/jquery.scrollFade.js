
(function ($) {
    $.fn.scrollFade = function (config) {
        var defaults = {
            out: 0,	// フェードアウト前の透明度
            duration: 350	// フェードにかける時間（ms）
        };
        var config = $.extend(defaults, config);
        var target = this;

        function fade() {
            target.each(function (i, e) {
                var in_position = $(e).offset().top + $(window).height() / 5;
                var window_bottom_position = $(window).scrollTop() + $(window).height();
                if (in_position < window_bottom_position) {
                    $(e).animate({
                        opacity: 1
                    }, {
                        queue: false,
                        duration: config.duration,
                        easing: 'easeOutQuad'
                    });
                } else {
                    if ($(e).css('opacity') > config.out) {
                        $(e).animate({
                            opacity: config.out
                        }, {
                            queue: false,
                            duration: config.duration,
                            easing: 'easeOutQuad'
                        });
                    }
                }
            });
        }

        fade();

        $(window).scroll(function () {
            fade();
        });
        return target;
    };
})(jQuery);