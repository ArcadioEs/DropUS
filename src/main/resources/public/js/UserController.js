var app = angular.module('DropUS', []);

app.controller('userList', function ($scope, $http) {

    $scope.users = [];
    $scope.addUserMessage = '';
    $scope.deleteUserMessage = '';

    $scope.getAllUsers = function() {
        $http({
            method: 'GET',
            url: 'user/all'
        }).then(function(model) {
            $scope.users = model.data;
        });
    };

    $scope.addUser = function() {
        $http({
            method: 'GET',
            url: 'user/add/' + $scope.usernameToAdd
        }).then(function (model) {
            $scope.addUserMessage = model.data.message;
            $scope.getAllUsers();
        });
    };

    $scope.deleteUser = function() {
        if( !isNaN($scope.userIdToDelete) && angular.isNumber(+$scope.userIdToDelete)) {
            $http({
                method: 'GET',
                url: 'user/delete/' + $scope.userIdToDelete
            }).then(function (model) {
                $scope.deleteUserMessage = model.data.message;
                $scope.getAllUsers();
            });
        } else {
            $scope.deleteUserMessage = 'Please provide user id, not a name';
        }
    };
});
