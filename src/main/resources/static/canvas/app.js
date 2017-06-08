var colSize = 32;
var pixels = [];

function draw(){
    var canvas = document.getElementById('canvas');
    var ctx = canvas.getContext('2d');

    var colSize = 32;
    var gridWidth = canvas.width / colSize;

    console.log("canvas.width="+canvas.width)
    console.log("grid.width="+gridWidth)

    for (var i = 0; i < colSize * colSize; i++) {

            var color = "#ffffff";

            if (pixels[i]){
                color = pixels[i].color;
            }

            ctx.fillStyle = color;

            var x = Math.floor(i % colSize);
            var y = Math.floor(i / colSize);

            ctx.fillRect(x * gridWidth, y * gridWidth, gridWidth, gridWidth);
    }
}

function drawPicker(){
    var canvas = document.getElementById('color-picker');
    var ctx = canvas.getContext('2d');

    var colors = ["#f6402e", "#eb2761", "#9c20b1", "#6633B9", "#3E4EB7",
    "#47af4b", "#009587", "#01bad5", "#00a6f6", "#1192f5",
    "#87c440", "#ccdd2a", "#ffeb2e", "#ffc024", "#ff981a",
    "#424242", "#5d7c8a", "#9d9d9d", "#7a5548", "#ff550c",
    "#000000", "#ffffff"
    ];

    var colorsIndex = 0;
    var colSize = 4;
    var gridWidth = canvas.width / colSize;

    for (var i = 0; i < 4; i++) {
      for (var j = 0; j < 4; j++) {
          if(++colorsIndex <= colors.length){
            ctx.fillStyle = colors[colorsIndex];
            ctx.fillRect(j * gridWidth, i * gridWidth, gridWidth, gridWidth);
          }
      }
    }

}

function calcIndexFromPosition(x, y){
    var canvas = document.getElementById('canvas');
    var gridWidth = canvas.width / colSize;

    var i = Math.floor(x/gridWidth);
    var j = Math.floor(y/gridWidth);

    return {i:i, j:j};
}

function onColorChanged(pixel){
    draw();
    sendPixel(pixel);
}

function onLoad(){

        var currentUid = "test";
        var currentColor = "#424242";

        document.getElementById('color-picker').addEventListener('click', function(e){
            console.debug('click: ' + e.offsetX + e.offsetY);
            var pixel = this.getContext("2d").getImageData(e.offsetX, e.offsetY, 1, 1).data;
            var hex = rgbToHexString(pixel[0], pixel[1], pixel[2]);
            currentColor = hex;

            $("body").css("background", hex);
            e.stopPropagation();
        });

        document.getElementById('canvas').addEventListener('click', function(e){
            var out = calcIndexFromPosition(e.offsetX, e.offsetY);
            var x = out.i;
            var y = out.j;
            var pixel = {color: currentColor, uid: currentUid, x: x, y: y, time: new Date().getTime()};
            pixels[y * colSize + x] = pixel;
            onColorChanged(pixel);
            e.stopPropagation();
        });

        document.body.addEventListener('click', function(){
            currentColor = "#ffffff";
            document.body.style = "background:" + currentColor;
        }, false);

        draw();
        drawPicker();


        // start connection
        connect();
}


function rgbToHex(r, g, b) {
    if (r > 255 || g > 255 || b > 255)
        throw "Invalid color component";
    return ((r << 16) | (g << 8) | b).toString(16);
}

function rgbToHexString(r, g, b){
    return "#" + ("000000" + rgbToHex(r, g, b)).slice(-6);
}











/////////// module

function newPixelArrived(pixel){
    if(pixel){
        pixels[pixel.x + colSize * pixel.y] = pixel;
        draw();
    }
}

function sendPixel(pixel) {
    stompClient.send("/app/pixel", {}, JSON.stringify(pixel));
}


function connect() {
    var socket = new SockJS('/greetings-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        //setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/pixels', function (greeting) {
            newPixelArrived(JSON.parse(greeting.body));
        });

        // fetch offline greetings
      $.get("/pixels", function(result){
            pixels = result;
            draw();
      });
    });
}