module.exports = {
  success: function (res) {
    res.end('{"msg":"Success"}');
  },
  error: function (res) {
    res.end('{"msg":"Error"}');
  },
  arrayToJSON: function (arrayArg) {
    var response = {};
    arrayArg.forEach(function(element, index, array) {
      response[index] = element;
    });
    return response;
  }
};
