$(document).ready(function() {
    var token = $('meta[name=_csrf]').attr('content');

    $('.btn-trigger').on('click', function () {
        var trigger = $(this);
        var triggerId = trigger.parent().prop('id');

        $.ajax({
            type: 'POST',
            url: '/update/likes',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({
                'postID': triggerId,
                'like': trigger.data('like')
            }),
            headers: {
                'X-CSRF-TOKEN': token
            },
            success: function(response) {
                $('.btn-like-' + triggerId).text('Likes ' + response.likes.length);
                $('.btn-dislike-' + triggerId).text('Dislikes ' + response.dislikes.length);
            }
        });
    });
});
