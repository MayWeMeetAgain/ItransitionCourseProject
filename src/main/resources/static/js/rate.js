function rate(rate, reviewId) {
    let url = "/reviews/" + reviewId + "/rate";
    $.post(url, {rate : rate}, function (data) {});
    // $.ajax({
    //     type: "POST",
    //     url: url,
    //     data: JSON.stringify(params),
    //     success: function(answer) { },
    //     error: function(jqXHR, timeout, message) {
    //         if (jqXHR.status === 401)
    //             location.href="/login";
    //     }
    // });

}


function showRating(rating, id) {
    let card_id = "card" + id
    var stars = document.getElementById(card_id).getElementsByClassName("stars__in")
    let full = Math.trunc(rating);
    let partly = rating - full;
    for (let i = 4; i > 4 - full; i--) {
        let star = stars[i];
        star.style.width = '100%';
    }
    stars[4 - full].style.width = (partly * 100 + "%");
}

