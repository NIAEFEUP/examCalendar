app.controller('ImportDatabaseController', ['$scope', 'chooseExams', function($scope, chooseExams) {
	chooseExams.then(
		function (response) {
			console.log("SUCCESS");
			$scope.topics = response.data;
		},
		function (response) {
			console.error(response);
	});
}]);