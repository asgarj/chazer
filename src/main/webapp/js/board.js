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
        cell.addEventListener('click', clickMethod);
	}
}

function clickMethod(event) {
    var me = (myId == player1.id) ? player1 : player2;
    var row = parseInt(event.srcElement.id.split(":")[0]);
    var col = parseInt(event.srcElement.id.split(":")[1]);
    if (me.chaser) {
        if ((Math.abs(row - me.row) <=1) && (Math.abs(col - me.column) <= 1)) {
        } else {
            alert("Invalid move. The one who's chasing can only move to the 8 neighbouring cells. (4 sides + 4 diagonally)");
            return;
        }
    } else if (Math.abs(row - me.row) + Math.abs(col - me.column) > 1) {
            alert("Invalid move. The one who's being chased can only move to the 4 neighbouring cells.");
         return;
    }
               
    console.log(event);
    console.log(event.srcElement.id);
    var cellLocation = {row: parseInt(event.srcElement.id.split(":")[0]), col: parseInt(event.srcElement.id.split(":")[1])};
    console.log("cellLocation: " + JSON.stringify(cellLocation));
            jQuery.ajax('/games/move/'+gameId + '/'+myId, {
                type: 'POST',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(cellLocation),
                success: function(result) {
                    removeAttributeOfPlayer(player1);
                    removeAttributeOfPlayer(player2);
                    if (result.message) {
                        showLast(event.srcElement);
                        alert(result.message);
                    } else {
                        player1 = result.player1;
                        player2 = result.player2;
                        showPlayers();
                    }
                },
                error: function(err) {
                    console.log("invalid request");
                    console.log(err.status);
                }
            });
}

var gameId;
var myId;
var player1;
var player2;

(function() {
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
            } else if (hash[0] === 'myId') {
                myId = hash[1];
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
                setlabel();
	            generate_table(5, 8);
                showPlayer(player1);
                showPlayer(player2);
            }
        });
    }
})();

function setlabel() {
    var nodeMe = document.createElement('span');
    var me = {}, opponent = {};
    if (myId == player1.id) {
        me.player = player1;
        opponent.player = player2;
        me.color = 'red';
        opponent.color = 'blue;'
    } else {
        me.player = player2;
        opponent.player = player1;
        me.color = 'blue';
        opponent.color = 'red;'
    }
    nodeMe.setAttribute('style', 'color:' + me.color);
    nodeMe.textContent = 'You:  ' + me.player.name;
    var nodeOpponent = document.createElement('span');
    nodeOpponent.setAttribute('style', 'color:' + opponent.color);
    nodeOpponent.textContent = '    Opponent: ' + opponent.player.name;
    var label = document.getElementById("info");
    label.appendChild(nodeMe);
    label.appendChild(nodeOpponent);
}

var setplayer = function(id, player) {
    if (id === 1)
        player1 = player;
    else
        player2 = player;
}

function showPlayers() {
    showPlayer(player1);
    showPlayer(player2);
}

function showLast(e) {
    e.setAttribute('style', 'border: 5px solid blue; border-left-color: red; border-right-color: red;');
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

function removeAttributeOfPlayer(player) {
    var row = player.row;
    var col = player.column;
    var cellid = document.getElementById(row + ":" + col);
    cellid.removeAttribute('style');
}
                  
                  
                  
                  