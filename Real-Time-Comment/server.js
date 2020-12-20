var express = require('express');
var app = express();
var mongoose = require('mongoose');
var http = require('http').createServer(app);
var io = require('socket.io')(http);
var { dbPw, dbUsername } = require('./dbUrl.js');

http.listen(3000, () => {
  console.log('Listening on ' + http.address().port);
});

var Message = mongoose.model('Message',{ name : String, message : String})

app.use(express.static(__dirname));
app.use(express.json());
app.use(express.urlencoded());

//connect to db
var dbUrl = `mongodb+srv://${dbUsername}:${dbPw}@cluster0.03dsp.mongodb.net/live-comment?retryWrites=true&w=majority`;
mongoose.connect(String(dbUrl) , (err) => {
  if(err){
    console.log("Error connecting with db: " + err);
  }
  else 
    console.log('Mongodb connected');
});

app.get('/messages', (req, res) => {
  Message.find({},(err, messages)=> {
    res.send(messages);
  })
});

app.post('/messages', (req, res) => {
  var message = new Message(req.body);
  message.save((err) =>{
    if(err)
      sendStatus(500);
    io.emit('message', req.body);
    res.sendStatus(200);
  })
});

io.on('connection', () => {
  console.log('User is connected');
});