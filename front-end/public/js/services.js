'use strict';
define(['angular'], function(angular) {
/* Services */
angular.module('app.services', [])
  .factory('Config', [function() {
    var Config = {
        url : "http://localhost:9010/api/"
    };
    return Config;
  }])
  // with users
  .factory('UserService', ['$http', 'Config', function($http, Config) {
        var User = {
            type : 'users',
            userOBJ : {},
            username : "",
            password : "",
            setUsername : function(inputUsername) {
                User.username = inputUsername;
            },

            getUserName : function() {
                return this.username;
            },
            getLoggedUserName : function () {
                return localStorage.getItem("username");
            },

            setPassword : function(inputPassword) {
                User.password = inputPassword;
            },
            getPassword : function() {
              if (this.password != "") {
                  return this.password;
              }
              else {
                  return localStorage.getItem("password");
              }
            },
            storeUserLocally : function(obj) {
                if (typeof(Storage) !== "undefined") {
                    localStorage.setItem("username", this.getUserName());
                    localStorage.setItem("password", this.getPassword());
                } else {
                    console.log('no local storage available');
                }
            },

            getUserOBJ : function() {
                return $this.userOBJ;
            },
            login : function(inputUsername, inputPassword, callback) {
                var jsonObject = angular.toJson({"username" : inputUsername, "password" : inputPassword});
                $http.post(Config.url + this.type + '/login', jsonObject ,{'Content-Type': 'application/x-www-form-urlencoded'})
                  .then(function successCallback(response) {
                    User.setUsername(response.data.userId);
                    User.setPassword(response.data.password);
                    User.storeUserLocally(response.data);

                    callback(response.status);
                  }, function errorCallback(response) {
                    callback(response.data.detail);
                });
            },
            register : function(inputUsername, inputPassword, callback) {

            },
            logout : function() {

            }
        }
        return User;
  }])
    // with products
  .factory('ProductService', ['$http', 'Config', function($http, Config) {
    var Product = {

    }
    return Product
  }])

});