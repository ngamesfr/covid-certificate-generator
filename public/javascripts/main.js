var formFields = $('input[name!=csrfToken][name!=signature][type!=submit][name!=reason]');
var lastPos = null;
var drawing = false;
var signatureInput = $('input[name=signature]');
var canvas = document.getElementById('canvas');
var context = canvas.getContext('2d');

var startDrawing = function(pos) {
    drawing = true;
    lastPos = pos;
};
var moveCursor = function(pos) {
    if (drawing) {
        context.beginPath();
        context.moveTo(lastPos.x, lastPos.y);
        context.lineTo(pos.x, pos.y);
        context.lineWidth = 1;
        context.strokeStyle = "rgb(0, 0, 0)";
        context.lineCap = 'round';
        context.stroke();
        lastPos = pos;
    }
}
var stopDrawing = function() {
    drawing = false;
};
canvas.addEventListener('mousedown', function (e) {
    startDrawing({ x: e.offsetX, y: e.offsetY });
});
canvas.addEventListener('mousemove', function (e) {
    moveCursor({ x: e.offsetX, y: e.offsetY });
});
canvas.addEventListener('mouseup', function () {
    stopDrawing();
});

document.body.addEventListener('touchstart', function (e) {
    if (e.target == canvas) {
        e.preventDefault();
        var touch = e.touches[0];
        startDrawing({
            x: touch.pageX - touch.target.offsetLeft,
            y: touch.pageY - touch.target.offsetTop
        });
    }
}, { passive: false });
document.body.addEventListener("touchend", function (e) {
    if (e.target == canvas) {
        e.preventDefault();
        stopDrawing();
    }
}, { passive: false });
document.body.addEventListener("touchmove", function (e) {
    if (e.target == canvas) {
        e.preventDefault();
        var touch = e.touches[0];
        moveCursor({
            x: touch.pageX - touch.target.offsetLeft,
            y: touch.pageY - touch.target.offsetTop
        });
    }
}, { passive: false });

$('#clear-canvas').click(function () {
    context.clearRect(0, 0, canvas.width, canvas.height);
    return false;
});
$('form').submit(function () {
    if (window.localStorage) {
        formFields.each(function () {
            localStorage[$(this).attr('name')] = $(this).val();
        });
    }
    signatureInput.val(canvas.toDataURL("image/png"));
});

if (window.localStorage) {
    formFields.each(function () {
        $(this).val(localStorage[$(this).attr('name')]);
    });
}