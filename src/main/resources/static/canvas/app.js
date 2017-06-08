function draw(pixels){
    var canvas = document.getElementById('canvas');
    var ctx = canvas.getContext('2d');

    var gridNum = 32;
    var gridWidth = canvas.width / gridNum;

    console.log("canvas.width="+canvas.width)
    console.log("grid.width="+gridWidth)

    for (var i = 0; i < gridNum; i++) {
        for (var j = 0; j < gridNum; j++) {

            var color = "#ffffff";

            if (pixels && pixels[j][i]){
                color = pixels[j][i].color;
            }

            ctx.fillStyle = color;

            ctx.fillRect(j * gridWidth, i * gridWidth, gridWidth, gridWidth);
        }
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
    var gridNum = 4;
    var gridWidth = canvas.width / gridNum;

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
    var gridNum = 32;
    var gridWidth = canvas.width / gridNum;

    var i = Math.floor(x/gridWidth);
    var j = Math.floor(y/gridWidth);

    return {i:i, j:j};
}

function onColorChanged(pixels, i, j){
    draw(pixels);
}



function onLoad(){
        var gridNum = 32;
        var pixels = [[]];
        for(var i = 0; i < gridNum; ++i) {
            pixels[i] = [];
        }

        var currentUid = "test";
        var currentColor = "#000000";



        document.getElementById('color-picker').addEventListener('click', function(e){
            console.debug('click: ' + e.offsetX + e.offsetY);
            var pixel = this.getContext("2d").getImageData(e.offsetX, e.offsetY, 1, 1).data;
            var hex = rgbToHexString(pixel[0], pixel[1], pixel[2]);
            currentColor = hex;

            document.body.style = "background:" + hex;
            e.stopPropagation();
        });

        document.getElementById('canvas').addEventListener('click', function(e){
            var out = calcIndexFromPosition(e.offsetX, e.offsetY);
            pixels[out.i][out.j] = {color: currentColor, uid: currentUid};
            onColorChanged(pixels, out.i, out.j);
            e.stopPropagation();
        });

        document.body.addEventListener('click', function(){
            currentColor = "#ffffff";
            document.body.style = "background:" + currentColor;
        }, false);

        draw();
        drawPicker();
}


function rgbToHex(r, g, b) {
    if (r > 255 || g > 255 || b > 255)
        throw "Invalid color component";
    return ((r << 16) | (g << 8) | b).toString(16);
}

function rgbToHexString(r, g, b){
    return "#" + ("000000" + rgbToHex(r, g, b)).slice(-6);
}