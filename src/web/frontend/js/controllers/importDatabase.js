app.controller('ImportDatabaseController', ['$scope', 'chooseExams', 'backendURL', function($scope, chooseExams, backendURL) {
	$scope.normalExams = {};
	$scope.appealExams = {};
	$scope.fillChooseExamsTable = function() {
		chooseExams.getTopics().then(
			function (response) {
				$scope.topics = response.data;
			},
			function (response) {
				console.error(response);
		});
	};
	$scope.backendURL = backendURL;
}]);