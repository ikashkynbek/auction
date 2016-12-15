auctionApp.factory('stompClient', function ($q) {
    var stompClient;
    return {
        init: function (url) {
            if (sockJsProtocols.length > 0) {
                stompClient = Stomp.over(new SockJS(url, null, {transports: sockJsProtocols}));
            }
            else {
                stompClient = Stomp.over(new SockJS(url));
                console.log(stompClient)
            }
        },
        connect: function () {
            return $q(function (resolve, reject) {
                if (!stompClient) {
                    reject("STOMP client not created");
                } else {
                    stompClient.connect({}, function (frame) {
                        resolve(frame);
                    }, function (error) {
                        reject("STOMP protocol error " + error);
                    });
                }
            });
        },
        disconnect: function () {
            stompClient.disconnect();
        },
        subscribe: function (destination) {
            var deferred = $q.defer();
            if (!stompClient) {
                deferred.reject("STOMP client not created");
            } else {
                stompClient.subscribe(destination, function (message) {
                    deferred.notify(JSON.parse(message.body));
                });
            }
            return deferred.promise;
        },
        subscribeSingle: function (destination) {
            return $q(function (resolve, reject) {
                if (!stompClient) {
                    reject("STOMP client not created");
                } else {
                    stompClient.subscribe(destination, function (message) {
                        console.log("message", message);
                        resolve(JSON.parse(message.body));
                    });
                }
            });
        },
        send: function (destination, headers, object) {
            stompClient.send(destination, headers, object);
        }
    };
});

auctionApp.factory('tradeService', function (stompClient, $q) {
    return {
        connect: function (url) {
            console.log("connecting", url)
            stompClient.init(url);
            return stompClient.connect().then(function (frame) {
                return frame.headers['user-name'];
            });
        },
        disconnect: function () {
            stompClient.disconnect();
        },
        loadPositions: function () {
            return stompClient.subscribeSingle("/app/auctions");
        },
        fetchQuoteStream: function () {
            return stompClient.subscribe("/topic/price.stock.*");
        },
        fetchPositionUpdateStream: function () {
            console.log("load fetchPositionUpdateStream")
            return stompClient.subscribe("/user/topic/position-updates");
        },
        fetchErrorStream: function () {
            return stompClient.subscribe("/user/topic/errors");
        },
        sendQuote: function (quote) {
            return stompClient.send("/app/quote", {}, JSON.stringify(quote));
        }
    };
});
