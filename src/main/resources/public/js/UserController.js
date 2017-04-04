var app = angular.module('users', []);

app.controller('userList', function ($scope, $http) {

    $scope.users = [];

    $scope.getAllUsers = function() {
        $http.get("user/all").success(function(data) {
            $scope.users = data;
        });
    }

});
