/*global define */

'use strict';

define(function() {
    var controllers = {};

    controllers.LoginCtrl = function($scope, $rootScope, $uibModalInstance, Config, UserService) {
        $scope.ok = function () {
            console.log($scope.info);
            $uibModalInstance.close(true);
        };
        $scope.cancel = function () {
            $uibModalInstance.dismiss(0);
        };
        $scope.login = function() {
            UserService.login($scope.username, $scope.password, function(status){
                alert(status)
            })
        }
    }
    controllers.LoginCtrl.$inject = ['$scope', '$rootScope', '$uibModalInstance', 'Config','UserService'];

    controllers.MainCtrl = function($scope, $rootScope, Config) {

    }
    controllers.MainCtrl.$inject = ['$scope', '$rootScope', 'Config'];


    controllers.ExploreCtrl = function($scope, $rootScope, Config, ProductService) {

    }
    controllers.ExploreCtrl.$inject = ['$scope', '$rootScope', 'Config', 'ProductService'];

    return controllers;
});