auctionApp.run(function (authService) {
    $.ajax(principalUrl, {async: false}).success(function (data) {
        authService.setPrincipal(data);
    });
});

auctionApp.service('authService', function ($rootScope) {
    var principal = {};

    this.setPrincipal = function (principal1) {
        principal = principal1;
        $rootScope.principal = principal;
    };
    this.getPrincipal = function () {
        return principal;
    };
});

auctionApp.directive('restrict', function (authService, accessService) {
    return {
        restrict: 'A',
        scope: false,
        compile: function (element, attr) {
            if (!accessService.check(attr.access)) {
                element.children().remove();
                element.remove();
            }
        }
    }
});

auctionApp.service('accessService', function (authService) {
    this.check = function (accessList) {
        var hasAccess = false;
        var user = authService.getPrincipal();
        if (typeof user.authorities !== "undefined") {
            var role = user.authorities[0].authority;
            var attributes = accessList.split(" ");
            for (var i in attributes) {
                if (role == attributes[i]) {
                    hasAccess = true;
                }
            }
        }
        return hasAccess;
    };
});

auctionApp.factory("sessionInjector", function ($q, $window, $location) {
    return {
        'responseError': function (rejection) {
            if (rejection.status === 401) {
                $location.path('/auth');
                $window.location.reload();
            } else {
                return $q.reject(rejection);
            }
        }
    };
});


auctionApp.factory('stompClient', function ($q) {
    var stompClient;
    return {
        init: function (url) {
            if (sockJsProtocols.length > 0) {
                stompClient = Stomp.over(new SockJS(url, null, {transports: sockJsProtocols}));
            }
            else {
                stompClient = Stomp.over(new SockJS(url));
            }
            stompClient.debug = null
            console.log("connected")
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
            stompClient.init(url);
            return stompClient.connect();
        },
        disconnect: function () {
            stompClient.disconnect();
        },
        loadAuctions: function () {
            return stompClient.subscribeSingle("/app/auctions");
        },
        fetchQuotes: function () {
            return stompClient.subscribe("/topic/quote");
        },
        fetchErrors: function () {
            return stompClient.subscribe("/user/topic/errors");
        },
        sendQuote: function (quote) {
            return stompClient.send("/app/quote", {}, JSON.stringify(quote));
        }
    };
});
