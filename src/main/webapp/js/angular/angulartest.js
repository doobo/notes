// var app = angular.module('notes', []);
// app.controller('noteCtrl',function ($scope) {
//     $scope.saveData = function (id) {
//     };
// });


angular.module('test',[])
    .factory('Data',function () {
        return {
          time : new Date(),
          name : 'Beijing Time'
        }
    })
    //隐式依赖注入
    // .controller('firstController',function ($scope,$filter,Data) {
    //     console.log($filter('date')(Data.time));
    // })
    //显示依赖注入
    .controller('firstController',['$scope','Data',function (a,b) {
        console.log(a);
        console.log(b.time);
    }])

