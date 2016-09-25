app.controller('ImportDatabaseController', ['$scope', 'chooseExams', function($scope, chooseExams) {
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
}]);