var auctionApp = angular.module('auction', ['ui.bootstrap']);

var sockJsProtocols = [];  
// var sockJsProtocols =  ["xhr-streaming", "xhr-polling"];  

auctionApp.filter('percent', function ($filter) {
    return function (input, total) {
        return $filter('number')(input / total * 100, 1) + '%';
    };
})
    .filter('totalPortfolioShares', function () {
        return function (positions) {
            var total = 0;
            for (var ticker in positions) {
                total += positions[ticker].shares;
            }
            return total;
        };
    })
    .filter('totalPortfolioValue', function () {
        return function (positions) {
            var total = 0;
            for (var ticker in positions) {
                total += positions[ticker].price * positions[ticker].shares;
            }
            return total;
        };
    });