/*global require, requirejs */

'use strict';

requirejs.config({
  paths: {
      'angular': ['../lib/angularjs/angular'],
      'angular-route': ['../lib/angularjs/angular-route'],
      'angular-animate' : ['../lib/angularjs/angular-animate'],
      'angular-aria' : ['../lib/angularjs/angular-aria.min'],
      'bootstrap' : ['../lib/bootstrap/js/bootstrap.min'],
      'ui-bootstrap' : ['../lib/angular-ui-bootstrap/ui-bootstrap.min'],
      'ui-bootstrap-tpls' : ['../lib/angular-ui-bootstrap/ui-bootstrap-tpls.min'],
      'jquery' : ['http://libs.baidu.com/jquery/2.0.0/jquery.min']
  },
  shim: {
      'angular': {
          exports : 'angular'
      },
      'angular-route': {
          deps: ['angular'],
          exports : 'angular'
      },
      'angular-animate': {
          deps: ['angular']
      },
      'angular-aria': {
          deps: ['angular']
      },
      "ui-bootstrap-tpls": {
          deps: ['angular']
      }
  }
});

require(['angular', './controllers', './directives', './filters', './services', 'angular-route','angular-animate', 'angular-aria', 'ui-bootstrap-tpls'],
  function(angular, controllers) {
    angular.module('app', ['app.filters', 'app.services', 'app.directives', 'ngRoute', 'ui.bootstrap'])
        .config(['$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
            $httpProvider.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
            $routeProvider.when('/main', {templateUrl: 'partials/main.html', controller: controllers.MainCtrl})
            $routeProvider.when('/explore', {templateUrl: 'partials/explore.html', controller: controllers.ExploreCtrl})
            $routeProvider.otherwise({redirectTo: '/main'});
        }])
        .controller('mainController', ['$scope','$rootScope','$q','$location', '$uibModal', function($scope, $rootScope, $q, $location, $uibModal){
            $scope.login = function(size) {
                $uibModal.open({
                    templateUrl: 'partials/login.html',
                    controller: controllers.LoginCtrl,
                    size: size,
                    resolve:{
                        info : function(){
                            return "login";
                        }
                    }
                });
            }
        }]);
    angular.bootstrap(document, ['app']);
  });
