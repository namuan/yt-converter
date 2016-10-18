$(document).ready(function () {

    $(document).ajaxStart(function () {
        $("#ytform").hide();
        $("#spinner").show();
    });

    $(document).ajaxStop(function () {
        $("#ytform").show();
        $("#spinner").hide();
    });

    $("#ytform").submit(function (event) {

        event.preventDefault();

        $("#videoLinkContainer").hide();

        var $form = $(this),
            url = $form.attr('action');

        var posting = $.post(url, {videoUrl: $('#videoUrl').val()});

        posting.done(function (data) {
            $("#videoLinkContainer").show();
            $("#videoName").attr("href", "/files/" + data);
        });
    });
});
