/**
 * Created by ajavadov on 4/3/2017.
 */
'use strict';

(function () {
    jQuery.ajax({
        url: "players",
        dataType: "json",
        success: function (players) {
            if (players.length === 0) {
                $("#game").textContent = "No Players yet...";
            }
            players.forEach(function (player) {
                addplayer(player.id, player.name);
            });
        }
    });
})();

var myPlayerId = 1;
var haveCreatedPlayer = false;

var addplayer = function (index, playername) {
    var listItem = '<li id=' + index + ' class="list-group-item col-md-6 col-md-offset-3">' + 
        '<span>' + playername + '</span>' + 
        '<button class="col-md-2 btn-sm btn-success pull-right" onclick="requestGame(' + index + ')">Play!</button>' +
        '</li>';
    $('#players').append(listItem);
};

var requestGame = function (opponentId) {
    $.ajax('/messages/request/' + myPlayerId + '/' + opponentId, {
        type: 'GET',
        dataType: 'json',
        success: function(data, status) {
            console.log(data["id"]);
            window.location.href = '/games?gameid=' + data["id"];
        },
        error: function(xhr, status, errorThrown) {
            console.log("xhr.status: " + xhr.status);
            if (xhr.status === 403) {
                alert("Bad request");
            }
        },
        statusCode: {
            403: function() {
                alert("403 is caught.");
            },
            201: function(data, status, jqXHR) {
                console.log(data);
                console.log(data.id);
                console.log(status);
                console.log(jqXHR);
            },
            200: function(data, status, jqxhr) {
                console.log(data);
                console.log(data.id);
                console.log(status);
                console.log(jqXHR);
            }
        }
    });
}

var create = function () {
//    if (haveCreatedPlayer)
//        return;
    var playername = $("#name").val();
    console.log(playername);
    var ajaxResult = $.ajax({
        type: 'POST',
        url: 'players',
        data: playername,
        contentType: 'text/plain',
        success: function (result) {
            addplayer(result.id, result.name);
        }
    });
};