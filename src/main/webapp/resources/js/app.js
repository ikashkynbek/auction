var auctionApp = angular.module('auction', ['ngRoute', 'ui.bootstrap']);

var sockJsProtocols = [];
// var sockJsProtocols =  ["xhr-streaming", "xhr-polling"];
var staticFilesPath = "/pages";
var context = "";

var principalUrl = context + "/principal";

var ordersUrl = "/orders";
var ordersListUrl = ordersUrl + "/list";

var productsUrl = "/products";
var productListUrl = productsUrl + "/list";

var auctionsPage = staticFilesPath + "/auctions.html";
var ordersPage = staticFilesPath + "/orders.html";
var newAuctionPage = staticFilesPath + "/newauction.html";

auctionApp.config(function ($routeProvider, $httpProvider, $locationProvider) {

    $routeProvider.when('/orders', {
        templateUrl: ordersPage,
        controller: 'orders',
        "check": function (accessService, $location) {
            if (!accessService.check("ADMIN")) {
                $location.path('/');
            }
        }
    }).when('/', {
        templateUrl: auctionsPage,
        controller: 'auction'
    }).when('/newauction', {
        templateUrl: newAuctionPage,
        controller: 'newAuction'
    }).otherwise('/');

    $locationProvider.html5Mode(true);
    $httpProvider.interceptors.push("sessionInjector");
});