var app = angular.module('myApp', []);
app.controller('Hello', function($scope, $http) {
        
	$scope.checkUrl = function() {
		if (!$scope.destUrl)
    		$scope.destUrl = "http://localhost:9000/greeting";
            
    	$http({
			method : "GET",
			url : $scope.destUrl
		}).then(function mySuccess(response) {
            $scope.greeting = response.data;
            $scope.statuscode = response.status;
            $scope.statustext = response.statusText; 
        }, function myError(response) {
			$scope.greeting = "";
			$scope.statuscode = response.status;
			$scope.statustext = response.statusText;
        });
	};
    
    $scope.checkUrl();
});
