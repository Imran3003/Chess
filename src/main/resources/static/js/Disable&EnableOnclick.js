function disableOnClickEvents() {
    console.log("Disable Event clicked");

    var chessboard = document.querySelector('.chessboard');
    var squares = chessboard.getElementsByClassName('square'); // Select all square elements within chessboard

    // Convert HTMLCollection to array for easier manipulation
    var elements = Array.from(squares);

    elements.forEach(function(element) {
        // Remove onclick attribute
        element.removeAttribute('onclick');
    });
}

function validateMateORpromotionAndDisableOnclick() {
    console.log("Inside validateMateORpromotionAndDisableOnclick");

    var popup = document.querySelector('.popup-container');
    var mateContainer = document.querySelector('.mate-container');

    // Check if popup is empty or not
    if (popup.innerHTML.trim() === '') {
        console.log("Popup is empty");
    } else {
        console.log("Popup is not empty");
        disableOnClickEvents();
    }

    // Check if mateContainer is empty or not
    if (mateContainer.innerHTML.trim() === '') {
        console.log("Mate container is empty");
    } else {
        console.log("Mate container is not empty");
        disableOnClickEvents();
    }
}

validateMateORpromotionAndDisableOnclick();