var http = require('http');
var url = require('url');
var uc = require('upper-case');

var dt = require('./myfirstmodule');

http.createServer(function (req, res) {
  res.writeHead(200, {'Content-Type': 'text/html'});
  res.write("The date and time is currently: " + dt.myDateTime());
  res.write("<br/>" + req.url);
  res.write(uc.upperCase("Hello World!"));
  
  // test with http://localhost:8080/?year=1987&month=11
  var q = url.parse(req.url, true).query;
  var txt = q.year + " " + q.month;  
  
  res.end('<br/>' + txt);
}).listen(8080);
