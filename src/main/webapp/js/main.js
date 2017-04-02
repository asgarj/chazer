'use strict';

function generate_table(nRows, nCols) {
	console.log(nRows + " " + nCols);
	var board = document.getElementById("board");
	console.log(board);
	for (var i = 1; i<= nRows; i++) {
		var row = board.insertRow(i-1);
		generateRow(row, i, nCols);
		console.log(row);
	}
}

function generateRow(row, rowId, nCols) {
	row.className = "height";
	for (var i=0; i<nCols; i++) {
		var cell = row.insertCell(i);
		cell.id = rowId + ":" + (i+1);
	}
}

(function init() {
	generate_table(5, 8);
})();