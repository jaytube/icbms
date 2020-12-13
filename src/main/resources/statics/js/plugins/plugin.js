var util = {
    ajax: function (url, type, callback, isJsonp) {
        var dataType = isJsonp ? "jsonp" : "json";
        $.ajax({
            url: url,
            dataType: dataType,
            type: type,
            async: true,
            success: function (data) {
                callback(data)
            }
        })
    },
    timeFormat: function(timestamp) {
        //timestamp是整数，否则要parseInt转换,不会出现少个0的情况
        var time = new Date(timestamp);
        var year = time.getFullYear();
        var month = time.getMonth() + 1;
        var date = time.getDate()<10?"0"+time.getDate() : time.getDate();
        var hours = time.getHours()<10? "0"+time.getHours() : time.getHours();
        var minutes = time.getMinutes()<10? "0"+time.getMinutes() : time.getMinutes();
        var seconds = time.getSeconds()<10? "0"+time.getSeconds() : time.getSeconds();
        return year + '-' + month + '-' + date + ' ' + hours + ':' + minutes + ':' + seconds;
    }
};
//面向对象封装
(function () {
    var defaultObj = {
        url: "",
        data: {},
        type: "post",
        dataType: "json",
        async: true,
        callback: function () {}
    };
    var getData = function (obj) {

    };
    getData.prototype = {

        ajax: function (url, type, callback, isJsonp) {
            var dataType = isJsonp ? "jsonp" : "json";
            $.ajax({
                url: url,
                dataType: "json",
                type: dataType,
                async: true,
                success: function (data) {
                    callback(data)
                }
            })
        },
        cutRoom: function (arr, size) {
            var arr2 = [];
            for (let i = 0; i < arr.length; i += size) {
                let endIndex = i + size - 1;
                if (endIndex >= arr.length) {
                    endIndex = arr.length - 1;
                }
                let end = arr[Number(endIndex)].name, //每个位置数组最后一个的名称
                    start = arr[i].name; //每个位置数组第一个位置的名称
                let rooms = arr.slice(i, i + size); //从返回的所有位置中选定位置数组的子数组。
                var arr3 = [];
                for ([index, value] of rooms.entries()) {
                    arr3.push({
                        room: value,
                        index: String((i + size) / size) + "-" + (index % size + 1)
                    })
                }
                let roomGroupObj = {
                    rooms: arr3,
                    groupIndex: start + "-" + end,
                    index: String((i + size) / size) //位置小数组的title绑定
                }
                arr2.push(roomGroupObj);
            }
            return arr2;
        }
    }
    window.getData = getData;
})()