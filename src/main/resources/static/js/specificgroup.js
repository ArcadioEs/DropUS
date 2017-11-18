$(document).ready(function() {

    $('.btn-trigger').on('click', function () {
        var trigger = $(this);
        var triggerId = trigger.parent().prop('id');

        $.ajax({
            type: 'POST',
            url: '/update/likes',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({
                'postID': trigger.data('post-id'),
                'like': trigger.data('like')
            }),
            success: function(response) {
                $('.btn-like-' + triggerId).text('Likes ' + response.likes.length);
                $('.btn-dislike-' + triggerId).text('Dislikes ' + response.dislikes.length);
            }
        });
    });
});
