function likeAction(reviewId) {
    likeButton = document.getElementById('like-button' + reviewId);
    likesAmount = document.getElementById('likes-amount' + reviewId);
    let url = "/reviews/" + reviewId + "/like";
    let type = likeButton.classList.contains("fa-regular") ? "POST" : "DELETE";
    sendLikeRequest(type, url, likesAmount, likeButton);
}

function sendLikeRequest(type, url, likesAmount, likeButton) {
    $.ajax({
        type: type,
        url: url,
        success: function(answer) { successHandler(answer, likesAmount, likeButton); },
        error: function(jqXHR, timeout, message) {
            if (jqXHR.status === 401)
                location.href="/login";
        }
    });
}

function successHandler(answer, likeAmount, likeButton) {
    likesAmount.textContent = answer;
    likeButton.classList.toggle("fa-regular");
    likeButton.classList.toggle("fa-solid");
}