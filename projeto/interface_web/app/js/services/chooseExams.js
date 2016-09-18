app.factory('chooseExams', ['$http', function($http) {
	this.getTopics = function(){
      return $http.get('http://localhost:8080/importDatabase/topics');
    };
	return this;
}]);