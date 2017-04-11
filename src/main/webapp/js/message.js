/**
 * Created by ajavadov on 4/3/2017.
 */
'use strict';

var checkInbox = function () {
    if (myPlayerId) {
        console.log("checkInbox");
        jQuery.ajax({
            url: "/messages/inbox/" + myPlayerId,
            type: "GET",
            dataType: "json",
            success: function (messages) {
                clearInbox();
                messages.forEach(function(message) {
                    addMessage(message);
                });
            }
        });
    }
};

setInterval(checkInbox, 1000);

function clearInbox() {
    document.getElementById("inbox").innerHTML = '';
}

var addMessage = function(message) {
    var listItem = '<li id="' + message.messageId + ' class="row list-group-item">' + 
        '<span class="col-md-12" style="padding-top: 5px; padding-bottom: 8px"><strong>' + message.requestor.name + '</strong> wants to play with you</span>' + 
        '<button type="button" class="col-md-4 col-md-offset-1 btn-success" onclick=acceptRequest(' + message.messageId + ')>YES</button>' + 
        '<button type="button" class="col-md-4 col-md-offset-2 btn-danger">No thanks</button>'+
        '</li>';
    $('#inbox').append(listItem);
}

var acceptRequest = function(messageId) {
    jQuery.ajax('/messages/respond/' + messageId + '/' + myPlayerId, {
        type: 'POST',
        dataType: 'text',
        success: function(gameId) {
            window.location = '/games/index.html?gameId=' + gameId +'&myId='+myPlayerId;
        },
        error: function(xhr) {
            console.log("error occurred in accepting request; status: " + xhr.status);
        }
    });
}

var rejectRequest = function(messageId) {
    // not implemented yet
    console.log("reject method not implemented yet");
}

var requestGame = function (opponentId) {
    if (opponentId == myPlayerId) {
        alert("You can't play with yourself");
        return;
    }

    $("#" + opponentId).find("button").attr("class", "col-md-2 btn-sm btn-default pull-right");
    $("#" + opponentId).find("button").text("requesting...");

    $.ajax('/messages/request/' + myPlayerId + '/' + opponentId, {
        type: 'GET',
        dataType: 'json',
        success: function(data, status) {
            console.log(data.id);
            window.location = '/games/index.html?gameId=' + data.id +'&myId='+myPlayerId;
        },
        error: function(xhr, status, errorThrown) {
            console.log("xhr.status: " + xhr.status);
            $("#" + opponentId).find("button").attr("class", "col-md-2 btn-sm btn-success pull-right");
            $("#" + opponentId).find("button").text("Play!");
            console.log("opponent refused to play");
        },
        statusCode: {
            403: function() {
                //alert("403 is caught.");
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

