var http = require('http');
var formidable = require('formidable');
var fs = require('fs');

http.createServer(function (req, res) {
  if (req.url == '/fileupload') {
    var form = new formidable.IncomingForm();
    form.parse(req, function (err, fields, files) {
	  // old path would be like 'C:\Users\mzhou\AppData\Local\Temp\upload_88ad51f481cd75720772f496b39843af'
      var oldpath = files.filetoupload.path;
      var newpath = 'C:/Users/mzhou/' + files.filetoupload.name;
      fs.rename(oldpath, newpath, function (err) {
        if (err) throw err;
        res.write('<br/>File uploaded and moved!');
        res.write('<br/>old path:' + oldpath);
        res.write('<br/>new path:' + newpath);
        res.end();
      });
 });
  } else {
    res.writeHead(200, {'Content-Type': 'text/html'});
    res.write('<form action="fileupload" method="post" enctype="multipart/form-data">');
    res.write('<input type="file" name="filetoupload"><br>');
    res.write('<input type="submit">');
    res.write('</form>');
    return res.end();
  }
}).listen(8080);