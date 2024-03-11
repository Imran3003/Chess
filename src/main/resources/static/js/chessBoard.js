var previousSquareId = null;
var previousSquareIdRed = null;

function handleCoinClick(i, j, id) {
    console.log(id);

    let currentSquare = document.getElementById(id);

    // Revert the background color of the previous square, if any
    if (previousSquareId !== null) {
        let previousSquare = document.getElementById(previousSquareId);
        previousSquare.style.backgroundColor = ""; // Set to the default background color
    }

    // Set the background color of the current square
    currentSquare.style.backgroundColor = "#053100";

    window.location.href = "/getPossibleMoves?x=" + i + "&y=" + j;
    // Update the previousSquareId for the next click
    previousSquareId = id;
}

// Add this function to display the promotion popup
function displayPromotionPopup() {
    console.log("inside displayPromotionPopup")
    var popup = document.querySelector('.popup-container');
    popup.style.display = 'block';
}

function pawnPromotion(i,j,coin)
{
    console.log("inside sendToBackend coin = ",coin)
    window.location.href = "/promotingPawn?x=" + i + "&y=" + j +"&coinName=" + coin;
}
function handleSquareClick(i, j, id) {
    console.log(id);

    let currentSquare = document.getElementById(id);

    // Revert the background color of the previous square, if any
    if (previousSquareIdRed !== null && currentSquare.style.backgroundColor !== "#053100") {
        let previousSquare = document.getElementById(previousSquareIdRed);
        previousSquare.style.backgroundColor = ""; // Set to the default background color
    }

    // Set the background color of the current square
    currentSquare.style.backgroundColor = "greenyellow";

    window.location.href = "/moveCoin?x=" + i + "&y=" + j;
    // Update the previousSquareId for the next click
    previousSquareIdRed = id;
}

function emptySquare(i, j, id) {
    console.log(id);

    let currentSquare = document.getElementById(id);

    // Revert the background color of the previous square, if any
    if (previousSquareIdRed !== null && currentSquare.style.backgroundColor !== "#053100") {
        let previousSquare = document.getElementById(previousSquareIdRed);
        previousSquare.style.backgroundColor = ""; // Set to the default background color
    }

    // Set the background color of the current square
    currentSquare.style.backgroundColor = "red";

    window.location.href = "/moveCoin?x=" + i + "&y=" + j;
    // Update the previousSquareId for the next click
    previousSquareIdRed = id;
}
