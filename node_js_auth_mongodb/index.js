//import
var mongodb = require('mongodb');
var ObjectID = mongodb.ObjectId;
var crypto = require('crypto');
var express = require('express');
var bodyParser = require('body-parser');
const { request } = require('http');
const { response } = require('express');
const res = require('express/lib/response');
const { route } = require('express/lib/application');
const router = express.Router();

//password utils
//create fuction to random salt
var genRandomString = function(length){
    return crypto.randomBytes(Math.ceil(length/2))
    .toString('hex')
    .slice(0,length);
};

var sha512 = function(password,salt){
    var hash = crypto.createHmac('sha512',salt);
    hash.update(password);
    var value = hash.digest('hex');
    return{
        salt:salt,
        passwordHash:value
    };
};

function saltHashPassword(userPassword){
    var salt = genRandomString(16);
    var passwordData = sha512(userPassword,salt);
    return passwordData;
}

function checkHashPassword(userPassword,salt){
    var passwordData = sha512(userPassword,salt);
    return passwordData;
}

//create express service
var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

//create mongodb client
var MongoClient = mongodb.MongoClient;

//conect url
var url = 'mongodb://localhost:27017'

MongoClient.connect(url,{useNewUrlParser: true},function(err,client){
    if(err){
        console.log('Unable to connect to the mongoDB server.Error',err);
    }
    else{

        //register
        app.post('/register',(request,response,next)=>{
            var post_data =request.body;

            var plaint_password = post_data.password;
            var hash_data = saltHashPassword(plaint_password);

            var password = hash_data.passwordHash; //save passwordahash
            var salt = hash_data.salt; //save salt
            
            var name = post_data.name;
            var email = post_data.email;

            var insertJson ={
                'email': email,
                'password': password,
                'salt': salt,
                'name': name
            };
            var db = client.db('mobileproject');
            
            //check exits email
            db.collection('user')
                .find({'email':email}).count(function(err,number){
                    if(number != 0){
                        response.json('Email already exists');
                        console.log('Email already exists');
                    }
                    else{
                        //insert data
                        db.collection('user')
                            .insertOne(insertJson,function(error,res){
                                response.json('Register Success');
                                console.log('Register Success');                               
                            })   
                    }
            })
        });
        app.post('/login',(request,response,next)=>{
            var post_data =request.body;

            var email = post_data.email;
            var userPassword = post_data.password;

            
            var db = client.db('mobileproject');
            
            //check exits email
            db.collection('user')
                .find({'email':email}).count(function(err,number){
                    if(number == 0){
                        response.json('Email not exists');
                        console.log('Email not exists');
                    }
                    else{
                        //insert data
                        db.collection('user')
                            .findOne({'email':email},function(err,user){
                                var salt = user.salt; // get salt from user
                                var hashed_password = checkHashPassword(userPassword,salt).passwordHash;// hash paswword with salt
                                var encrypted_password = user.password; // get password from user
                                if(hashed_password == encrypted_password){
                                    response.json('Login Success');
                                    console.log('Login Success');
                                }
                                else{
                                    response.json('Wrong password');
                                    console.log('Wrong password');                                    
                                }
                            })   
                    }
            })
        });
        // app.get('/:_id',(req,res,next)=>{
        //     //const get_data =req.body;
        //     const get_id =req.params._id;
        //     //const email = get_id.email;
        //     const db = client.db('mobileproject');
        //     db.collection('user')
        //         .findOne({"email" : email },function(err,user){
        //             if(err){  
        //                 //response.send('false');
        //                 console.log('false');
        //             }
        //             else{
        //                 res.send(user.email);
        //             }
        //     });
        // });
        //start web sever
        app.listen(3001,()=>{
            console.log('Connected to mongoDB server, WebService running on port 3001');
        })
    }
})

