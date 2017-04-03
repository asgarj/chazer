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

(function init() {
	generate_table(5, 8);
    var queryStringIndex = window.location.href.lastIndexOf('?');
    if (queryStringIndex > -1) {
        queries = window.location.href.slice(queryStringIndex).split('&');
        var gameId;
        for(var query in queries) {
            var hash = query.split('=');
            if (hash[0] === 'gameid') {
                gameId = hash[1];
            }
        }
        
    }
})();