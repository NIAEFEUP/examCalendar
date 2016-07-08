module.exports = {
  success: function (res) {
    res.json({"msg":"Success"});
  },
  error: function (res) {
    res.json({"msg":"Error"});
  },
  arrayToJSON: function (arrayArg) {
    var response = {};
    arrayArg.forEach(function(element, index, array) {
      response[index] = element;
    });
    return response;
  }
};
