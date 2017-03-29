var express = require('express');
var app = express();
app.use(express.bodyParser());

var recipes = require("./data.js").recipes;
app.use('/assets',express.static('public'));

app.get('/recipes/count', (req, res) => {
    res.json({
      count:recipes.length
    });
});
app.get('/recipes/all', (req, res) => {
    res.json(recipes);
});
app.get('/recipes/:id', function(req, res) {
  res.json(recipes[req.params.id]);
});
app.get('/recipes/from/:id', function(req, res) {
  var index = req.params.id;
  if (index > -1 && index<recipes.length) {
    res.json(recipes.slice(index));
  }else
    res.json({
      message:"oob"
    });
});
app.post('/recipes/add', function(req, res) {
  recipes[recipes.length] = req.body;
  res.json({message:"ok",id:recipes.length});
});
app.delete('/recipes/:id', function(req, res) {
  var index = req.params.id;
  if (index > -1 && index<recipes.length) {
    recipes.splice(req.params.id, 1);
    res.json({
      message:"ok",
      count:recipes.length
    });
  } else
    res.json({
      message:"oob"
    });
});
app.put('/recipes/:id', function(req, res) {
  var index = req.params.id;
  if (index > -1 && index<recipes.length) {
    recipes[index] = req.body;
  }
  res.json({message:"ok",id:recipes.length});
});

app.listen(process.env.PORT, () => {
  console.log("Listening at port "+process.env.PORT);
});