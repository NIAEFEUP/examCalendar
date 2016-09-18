app.controller('ImportDatabaseController', ['$scope', 'chooseExams', function($scope, chooseExams) {
	$scope.fillChooseExamsTable = function() {
		chooseExams.getTopics().then(
			function (response) {
				console.log("SUCCESS");
				$scope.topics = response.data;
			},
			function (response) {
				console.error(response);
		});
	};
}]);