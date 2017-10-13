'use strict';
define(['angular'], function(angular) {
/* Services */
angular.module('app.services', [])
  .factory('Config', [function() {
    var Config = {
        url : "http://localhost:9000/api/"
    };
    return Config;
  }])

});