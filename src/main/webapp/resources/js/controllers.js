auctionApp.controller('auction', function ($scope, $uibModal, tradeService, $filter) {
    $scope.notifications = [];
    $scope.auctions = {};

    var updateQuotes = function (quote) {
        var auction = $filter('filter')($scope.auctions, {id: quote.auctionId})[0];
        var oldQuote = $filter('filter')(auction.quotes, {price: quote.price})[0];
        if (oldQuote != null) {
            oldQuote.qty = quote.action == 'ADD' ? (oldQuote.qty + quote.qty) : (oldQuote.qty - quote.qty);
            if (oldQuote.qty <= 0) {
                auction.quotes.splice(auction.quotes.indexOf(oldQuote), 1);
            }
        } else {
            auction.quotes.push(quote);
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
                type: function () {
                    return {name: type}
                },
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

    tradeService.connect("/auction-endpoint")
        .then(function () {
                return tradeService.loadAuctions();
            },
            function (error) {
                pushNotification(error);
            })
        .then(function (auctions) {
            $scope.auctions = auctions;
            tradeService.fetchQuotes().then(null, null,
                function (quote) {
                    updateQuotes(quote);
                }
            );
            tradeService.fetchErrors().then(null, null,
                function (error) {
                    pushNotification(error);
                }
            );
        });
});

auctionApp.controller('orders', function ($scope, $http) {
    $http.get(ordersListUrl).success(function (response) {
        $scope.orders = response.data;
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

auctionApp.controller('newAuction', function ($scope, $http, $uibModal) {

    $scope.statuses = [{name:'ACTIVE', text: 'Active'}, {name:'INACTIVE', text: 'Inactive'}];
    $scope.status = $scope.statuses[0];

    $http.get(productListUrl).success(function (response) {
        $scope.products = response.data;
        $scope.product = $scope.products[0];
    })
    
    $scope.createAuction = function () {
        $scope.auction = { productName: $scope.product.name,
                           startDate: $scope.startDate,
                           endDate: $scope.endDate,
                           status: $scope.status.name };

        $http.post(
            "/auctions/newAuction", $scope.auction
        ).success(function () {
            console.log("New auction created successfully");
            $scope.showModal("New auction created successfully");
            $scope.clearForm();
        }).error(function () {
            console.log("Error creating new auction");
            $scope.showModal("Error creating new auction");
        });
    }

    $scope.showModal = function (message) {
        var modalInstance = $uibModal.open({
            templateUrl: 'newAuctionModal.html',
            controller: 'newAuctionModalController',
            resolve: {
                message: function () {
                return {message: message}
            }
            }
        });
    }
    $scope.clearForm = function () {
        $scope.productName = null;
        $scope.startDate = null;
        $scope.endDate = null;
        $scope.status = null;
    }
});

auctionApp.controller('newAuctionModalController', function ($scope, $uibModalInstance, message) {
    $scope.message = message.message;

    $scope.ok = function () {
        $uibModalInstance.close();
    };

});