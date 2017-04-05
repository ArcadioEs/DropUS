var app = angular.module('DropUS', []);

app.controller('userList', function ($scope, $http) {

    $scope.users = [];

    $scope.getAllUsers = function() {
        $http({
            method: 'GET',
            url: 'user/all'
        }).then(function(obj) {
            $scope.users = obj.data;
            console.log(obj)
        });
    }

});
