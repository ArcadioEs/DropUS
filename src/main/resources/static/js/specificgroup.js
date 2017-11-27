$(document).ready(function() {
    var token = $('meta[name=_csrf]').attr('content');
    var remoteUser = $('meta[name=_remoteUser]').attr('content');

    $('.btn-trigger').on('click', function () {
        var trigger = $(this);
        var triggerId = trigger.parent().prop('id');
        var like = trigger.data('like');

        $.ajax({
            type: 'POST',
            url: '/update/likes',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({
                'postID': triggerId,
                'like': like
            }),
            headers: {
                'X-CSRF-TOKEN': token
            },
            success: function(response) {
                $('.btn-like-' + triggerId).text(' Likes ' + response.likes.length).data('likes', response.likes).prepend($('<span>').attr({class: 'glyphicon glyphicon-thumbs-up'}));
                $('.btn-dislike-' + triggerId).text(' Dislikes ' + response.dislikes.length).data('dislikes', response.dislikes).prepend($('<span>').attr({class: 'glyphicon glyphicon-thumbs-down'}));
            },
            complete: function() {
                if ($.inArray(remoteUser, $('.btn-like-' + triggerId).data('likes')) !== -1) {
                    $('.btn-like-' + triggerId).css('color', 'green');
                } else {
                    $('.btn-like-' + triggerId).css('color', 'black');
                }
                if ($.inArray(remoteUser, $('.btn-dislike-' + triggerId).data('dislikes')) !== -1) {
                    $('.btn-dislike-' + triggerId).css('color', 'red');
                } else {
                    $('.btn-dislike-' + triggerId).css('color', 'black');
                }
            }
        });
    });
});
