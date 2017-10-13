/*global require, requirejs */

'use strict';

requirejs.config({
  paths: {
      'angular': ['../lib/angularjs/angular'],
      'angular-route': ['../lib/angularjs/angular-route'],
      'angular-animate' : ['../lib/angularjs/angular-animate'],
      'angular-aria' : ['../lib/angularjs/angular-aria.min'],
      'bootstrap' : ['../lib/bootstrap/js/bootstrap.min'],
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
      'bootstrap' : {
          deps : [ 'jquery' ],
          exports : 'bootstrap'
      }
  }
});

require(['angular', './controllers', './directives', './filters', './services', 'angular-route','angular-animate', 'angular-aria', 'bootstrap'],
  function(angular, controllers) {
    angular.module('app', ['app.filters', 'app.services', 'app.directives', 'ngRoute'])
        .config(['$routeProvider', function($routeProvider) {
            $routeProvider.otherwise({redirectTo: '/'});
        }])
        .controller('mainController', ['$scope','$rootScope','$q','$location',
            function($scope, $rootScope, $q, $location){

        }]);
    angular.bootstrap(document, ['app']);
  });
