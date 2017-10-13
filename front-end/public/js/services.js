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
            logIn : function(inputUsername, inputPassword, callback) {

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