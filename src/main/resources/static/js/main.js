'use strict';
$(document).ready(function () {
    const HEIGHT = $(window).height();
    const WIDTH = $(window).width();
    // fetch('/weight-app2')
    //     .then(function(response) {
    //         if (response.ok) {
    //             // Создаем объект URL с текущим адресом страницы
    //             const url = new URL(window.location.href);
    //             // Проверяем, является ли текущий URL адресом '/weight-app'
    //             if (url.pathname === '/weight-app') {
    //                 // Получаем значение параметра 'width'
    //                 const widthParam = url.searchParams.get('width');
    //
    //                 // Проверяем, есть ли параметр 'width' и соответствует ли его значение ширине окна браузера
    //                 if (!widthParam || parseInt(widthParam) !== window.innerWidth) {
    //                     // Получаем новое значение параметра 'width' равное ширине окна браузера
    //                     const newWidthParam = window.innerWidth;
    //
    //                     // Устанавливаем новое значение параметра 'width'
    //                     url.searchParams.set('width', newWidthParam);
    //
    //                     // Получаем новый URL с параметрами
    //                     const redirectUrl = url.toString();
    //
    //                     // Перенаправляем на новый URL
    //                     window.location.replace(redirectUrl);
    //                 }
    //             }
    //         } else {
    //             console.log('Ошибка HTTP: ' + response.status);
    //         }
    //     })
    //     .catch(function(error) {
    //         console.log('Произошла ошибка:', error);
    //     });

    $('.mainPageWrapper').css("margin", (HEIGHT - $('.mainPageWrapper').height()) / 2 + "px auto");

    $('.enterButtonWrapper').click(function () {
        window.location.replace("/login");
    });

    $('.registerButtonWrapper').click(function () {
        window.location.replace("/registration");
    });

    $('.regFormWrapper').css("margin", ((HEIGHT - $('.regFormWrapper').height()) / 2 - 50) + "px auto");
    $('.loginFormWrapper').css("margin", ((HEIGHT - $('.loginFormWrapper').height()) / 2 - 50) + "px auto");

    $('.input_div').mouseenter(function () {
        let text = $(this).children('.slide_out_text_reg').stop();
        text.animate({"left": "-" + (text.width() + 15) + "px", "opacity": "1"}, 300);
    });

    $('.input_div').mouseout(function () {
        let text = $(this).children('.slide_out_text_reg').stop();
        text.animate({"left": "10px", "opacity": "0.2"}, 300);
    });

    $('.input_div').mouseenter(function () {
        let text = $(this).children('.slide_out_text_login').stop();
        text.animate({"left": "-" + (text.width() + 15) + "px", "opacity": "1"}, 300);
    });

    $('.input_div').mouseout(function () {
        let text = $(this).children('.slide_out_text_login').stop();
        text.animate({"left": "10px", "opacity": "0.2"}, 300);
    });

    $('.question_icon').mouseenter(function () {
        $('.err_msg').text(this.getAttribute('text'));
        $('.err_msg').css("opacity", "1");
    });

    $('.question_icon').mouseout(function () {
        $('.err_msg').text("");
        $('.err_msg').css("opacity", "0");
    });

    $('.add_weight').click(function () {
        $('.add_weight_wrapper_block').stop().animate({"left": "0"}, 300);
    });

    $('.red_close').click(function () {
        $('.add_weight_wrapper_block').stop().animate({"left": "-110%"}, 300);
        setTimeout(function () {
            $('.weight_inp').css("border", "4px solid #2B6103");
        }, 300);
    });

    const gWeightBlock = $('.g_weight_block');
    const gFrame = $('.g_frame');
    const chartWrapper = $('.chart_wrapper');
    gFrame.css({"height": "350px", "width": (gWeightBlock.width() - 100) + "px"});
    $('.scale_frame').css("height", (gFrame.height() - 30) + "px");
    chartWrapper.css({"width": (gWeightBlock.width() - 120) + "px", "height": (gFrame.height() - 7) + "px"});

const chart_column = $('.chart_column');
    const col = $('.column_in_chart_column');
    $('.column_chart_date_span').css({"left": (chart_column.width() / 2 - 49) + "px"});
    col.css({"height": chartWrapper.height() + "px"});
    //
    // $('.column_in_chart_column').mousemove(function(event) {
    //     const offset = $(this).offset(); // Получаем положение блока .column_in_chart_column относительно страницы
    //     const top = event.pageY - offset.top + 5; // Вычисляем вертикальное смещение от курсора до блока
    //     const left = event.pageX - offset.left + 5; // Вычисляем горизонтальное смещение от курсора до блока
    //     $(this).children('.column_particular_data_wrapper').css({
    //         "top": top - 170 + "px",
    //         "left": left - 10 + "px"
    //     });
    // });

    // $('.column_in_chart_column').hover(
    //     function() {
    //         $(this).children('.column_particular_data_wrapper').css({"opacity" : 1});
    //     },
    //     function() {
    //         $(this).children('.column_particular_data_wrapper').css({"opacity" : 0});
    //     }
    // );
// alert((chart_column.width() + parseInt(chart_column.attr('margin'))) + "px")
    col.css({
        "width": (2 + chart_column.width() + parseInt(chart_column.attr('margin'))) + "px",
        "left" : "-" + (parseInt(chart_column.attr('margin')) / 2) + "px"
    });
    col.mouseenter(function () {
        $('.infoWrapper').css("display", "none");
        $('.chart_column').css("opacity", "0.3");
        $('.' + $(this).attr('id')).css("opacity", "1");
        $('#info' + $(this).attr('id')).css("display", "block");
    })
    col.mouseout(function () {
        $('.chart_column').css("opacity", "1");
        $('.infoWrapper').css("display", "none");
    })

});