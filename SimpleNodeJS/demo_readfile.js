var http = require('http');
var fs = require('fs');

fs.appendFile('mynewfile1.txt', 'Hello content!', function (err) {
  if (err) throw err;
  console.log('mynewfile1 appended!');
});

fs.writeFile('mynewfile3.txt', 'Hello content!', function (err) {
  if (err) throw err;
  console.log('mynewfile3 Saved!');
});

fs.appendFile('mynewfile3.txt', '\n This is my new text.', function (err) {
  if (err) throw err;
  console.log('mynewfile3 Updated!');
});

fs.unlink('mynewfile1.txt', function (err) {
  if (err) throw err;
  console.log('mynewfile1 deleted!');
});

http.createServer(function (req, res) {
  fs.readFile('file1.html', function(err, data) {
    res.writeHead(200, {'Content-Type': 'text/html'});
    res.write(data);
    return res.end();
  });
}).listen(8080);
