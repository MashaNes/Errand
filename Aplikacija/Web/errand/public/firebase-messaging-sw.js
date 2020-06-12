/* eslint-disable no-undef */
/* eslint-disable no-unused-vars */
importScripts('https://www.gstatic.com/firebasejs/3.9.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/3.9.0/firebase-messaging.js');

var config = {
    apiKey: "AIzaSyAC5XfZ2fgQ2TLPTrrmrvkKRhfd0DWondI",
    authDomain: "swe-errand.firebaseapp.com",
    databaseURL: "https://swe-errand.firebaseio.com",
    projectId: "swe-errand",
    storageBucket: "swe-errand.appspot.com",
    messagingSenderId: "1000133791657",
    appId: "1:1000133791657:web:3145b2aaee12cf1e4fbeb8",
    measurementId: "G-NY85XKGTMN"

};
firebase.initializeApp(config);

let messaging = firebase.messaging();