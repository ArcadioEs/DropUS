var app = angular.module('users', []);

app.controller('userList', function ($scope, $http) {

    $scope.users = [];
    $scope.addUserMessage = '';
    $scope.deleteUserMessage = '';

    $scope.getAllUsers = function() {
        $http.get("user/all").success(function (model) {
            console.log(model);
            $scope.users = model;
        });
    };

    $scope.addUser = function() {
        $http.put("user/add/" + $scope.usernameToAdd).success(function (model) {
            console.log(model);
            $scope.addUserMessage = model.message;
            $scope.getAllUsers();
        });
    };

    $scope.deleteUser = function() {
        if( !isNaN($scope.userIdToDelete) && angular.isNumber(+$scope.userIdToDelete)) {
            $http.delete("user/delete/" + $scope.userIdToDelete).success(function (model) {
                console.log(model);
                $scope.deleteUserMessage = model.message;
                $scope.getAllUsers();
            });
        } else {
            $scope.deleteUserMessage = 'Please provide user id, not a name';
        }
    };
});