app.controller('ImportDatabaseController', ['$scope', '$sce', 'chooseExams', 'backendURL', function($scope, $sce, chooseExams, backendURL) {
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
	$scope.createCalendarURL = $sce.trustAsResourceUrl(backendURL + "/createCalendar");
	$scope.importDatabaseURL = $sce.trustAsResourceUrl(backendURL + "/database");
	$scope.chooseExamsURL = $sce.trustAsResourceUrl(backendURL + "/importDatabase/topics");
}]);