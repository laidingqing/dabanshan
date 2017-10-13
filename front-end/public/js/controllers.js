/*global define */

'use strict';

define(function() {
    var controllers = {};
    controllers.LoginCtrl = function($scope, $rootScope, Config, UserService) {

    }
    controllers.LoginCtrl.$inject = ['$scope', '$rootScope', 'Config','UserService'];

    controllers.MainCtrl = function($scope, $rootScope, Config) {

    }
    controllers.MainCtrl.$inject = ['$scope', '$rootScope', 'Config'];


    controllers.ExploreCtrl = function($scope, $rootScope, Config, ProductService) {

    }
    controllers.ExploreCtrl.$inject = ['$scope', '$rootScope', 'Config', 'ProductService'];

    return controllers;
});