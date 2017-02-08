app.factory('chooseExams', ['$http', 'backendURL', function($http, backendURL) {
	this.getTopics = function(){
      return $http.get(backendURL + '/importDatabase/topics');
    };
	return this;
}]);