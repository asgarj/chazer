'use strict';

var myPlayerId;

setInterval(function () {
    jQuery.ajax({
        url: "players",
        dataType: "json",
        success: function (players) {
            if (players.length === 0) {
                $("#game").textContent = "No Players yet...";
            }
            document.getElementById("players").innerHTML='';
            players.forEach(function (player) {
                addplayer(player);
            });
        }
    });
}, 1500);

var formsubmit = function () {
    create();
    return false;
}

var create = function () {
    if (myPlayerId)
        return;
    var playername = $("#name").val();
    console.log("playername: " + playername);
    var ajaxResult = $.ajax({
        type: 'POST',
        url: 'players',
        data: playername,
        contentType: 'text/plain',
        success: function (result) {
            myPlayerId = result.id;
            console.log("may player id set to " + myPlayerId);
            addplayer(result);
            $("#registration").hide();
        }
    });
};

var addplayer = function (player) {
    var listItem = '<li id=' + player.id + ' class="list-group-item col-md-6 col-md-offset-2">' +
        '<span>' + player.name + '</span>' +
        '<button class="col-md-2 btn-sm btn-success pull-right" onclick="requestGame(' + player.id + ')">Play!</button>' +
        '</li>';
    $('#players').append(listItem);
};

(function() {
    var cookie = document.cookie;
    if (cookie != "") {
        cookie.split(";").forEach(function (c) {
            var pos = c.indexOf("playerId");
            if (pos > -1) {
                myPlayerId = c.split("=")[1].trim();
                console.log("myPlayerId read from cookie:_"+myPlayerId+"_");
                $("#registration").hide();
                return;
            }
        });
    }
})();