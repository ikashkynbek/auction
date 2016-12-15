auctionApp.controller('auctionController', function ($scope, $uibModal, tradeService) {
    $scope.notifications = [];
    $scope.auctions = {};

    var processQuote = function (quote) {
        var existing = $scope.positions[quote.ticker];
        if (existing) {
            existing.change = quote.price - existing.price;
            existing.price = quote.price;
        }
    };
    var udpatePosition = function (position) {
        var existing = $scope.positions[position.ticker];
        if (existing) {
            existing.shares = position.shares;
        }
    };
    var pushNotification = function (message) {
        $scope.notifications.unshift(message);
    };

    $scope.openTradeModal = function (type, auction) {
        var modalInstance = $uibModal.open({
            templateUrl: 'tradeModal.html',
            controller: 'QuoteModalController',
            size: "sm",
            resolve: {
                type: function () {return {name: type}},
                auction: auction
            }
        });
        modalInstance.result.then(function (result) {
            tradeService.sendQuote(result.quote);
        });
    };

    $scope.logout = function () {
        tradeService.disconnect();
    };

    tradeService.connect("/auctionEndpoint")
        .then(function (username) {
                $scope.username = username;
                pushNotification("Trade results take a 2-3 second simulated delay. Notifications will appear!!!");
                return tradeService.loadPositions();
            },
            function (error) {
                pushNotification(error);
            })
        .then(function (auctions) {
            $scope.auctions = auctions;
            tradeService.fetchQuoteStream().then(null, null,
                function (quote) {
                    processQuote(quote);
                }
            );
            tradeService.fetchPositionUpdateStream().then(null, null,
                function (position) {
                    udpatePosition(position);
                }
            );
            tradeService.fetchErrorStream().then(null, null,
                function (error) {
                    pushNotification(error);
                }
            );
        });

});

auctionApp.controller('QuoteModalController', function ($scope, $uibModalInstance, tradeService, type, auction) {
    $scope.quote = {type: type.name, auctionId: auction.id, qty: 1, price: 85000};
    $scope.title = ($scope.quote.type == 'BID' ? "Buy " : "Sell ") + auction.productName;

    $scope.trade = function () {
        $uibModalInstance.close({
            quote: $scope.quote
        });
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});