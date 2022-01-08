# Metube
Clone of youtube 

# Intro #
A Video streaming site where user can
- [x] Register/Login(Used JWT)
- [x] Upload/Watch Video(Used S3 for file storage)
- [x] Like/Dislike video
- [x] Add comment to video
- [x] Pagination in spring API(Used MongoRepository)
- [ ] Add Projection in API
- [x] Subscriber module
- [x] History videos
- [ ] Playlist module
- [ ] Video Recommendation



# Technologies used #

* Java 17 
* Mongodb
* Angular 11
* AWS S3-For storing images,videos

# Steps to install #
* 1-Download project,open your IDE and install necessary packages
* 2-Install Mongodb and create database named **metube**
* 3-Install Angular 11 and run command in your root project directory **npm i**
* 4-Create AWS account and create bucket in S3.Copy bucket id and secret and place them in your environment variable named **s3keyid** and **s3keysecret**
* 5-Run Spring and angular project
