function rate(rate, reviewId) {
    let url = "/reviews/" + reviewId + "/rate";
    $.post(url, {rate : rate})
        .done(function(msg){
            showRating(msg, reviewId);
            changeRatingNumbers(msg, reviewId);
        })
        .fail(function(xhr, status, error) {
            // error handling
        });;
}


function changeRatingNumbers(rating, id) {
    let card_id = "card" + id;
    let preStarsRating = document.getElementById(card_id).getElementsByClassName("rating-number")[0];
    preStarsRating.textContent = rating;

    let preTitleRating = document.getElementById(card_id).getElementsByClassName("title-rating")[0];
    preTitleRating.textContent = rating;
}

function showRating(rating, id) {
    let card_id = "card" + id;
    var stars_in = document.getElementById(card_id).getElementsByClassName("stars__in");
    var stars = document.getElementById(card_id).getElementsByClassName("star");

    let full = Math.trunc(rating);
    let partly = rating - full;
    for (let i = 4; i > -1; i--) {
        let star_in = stars_in[i];
        let star = stars[i];

        star.classList.remove('star')
        star_in.style.width = i > (4 - full) ? '100%' : i === (4 - full) ? (partly * 100 + '%') : 0 + '%';
    }
}

