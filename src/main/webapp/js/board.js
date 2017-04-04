'use strict';

function generate_table(nRows, nCols) {
	var board = document.getElementById("board");
	for (var i = 1; i<= nRows; i++) {
		var row = board.insertRow(i-1);
		row.className = "height";
		generateRow(row, i, nCols);
	}
}

function generateRow(row, rowId, nCols) {
	for (var i=0; i<nCols; i++) {
		var cell = row.insertCell(i);
		cell.id = rowId + ":" + (i+1);
	}
}

var gameId;
var player1;
var player2;

(function() {
	generate_table(5, 8);
    var queryStringIndex = window.location.href.lastIndexOf('?');
    console.log("querystringIndex: " + queryStringIndex);
    console.log("url: " + window.location.href);
    console.log(window.location);
    if (queryStringIndex > -1) {
        var queries = window.location.href.slice(queryStringIndex + 1).split('&');
        console.log(queries);
        queries.forEach(function(query) {
        	console.log("query:"+query);
            var hash = query.split('=');
            console.log("hash"+hash);
            if (hash[0] === 'gameId') {
                gameId = hash[1];
            }
        });
        console.log("gameId" + gameId);
        $.ajax('' + gameId, {
            type: 'GET',
            dataType: 'json',
            success: function(data) {
                console.log(data);
                setplayer(1, data.player1);
                setplayer(2, data.player2);
                showPlayer(player1);
                showPlayer(player2);
            }
        });
    }
})();

var setplayer = function(id, player) {
    if (id === 1)
        player1 = player;
    else
        player2 = player;
}

function showPlayer(player) {
    var row = player.row;
    var col = player.column;
    var cellid = document.getElementById(row + ":" + col);
    console.log(cellid);
    if (player.chaser)
        cellid.setAttribute('style', 'border: 5px solid red');
    else 
        cellid.setAttribute('style', 'border: 5px solid blue');
}
                  
                  
                  
                  