ajaxRequest = function (type, url, params) {
    var data = null;

    $.ajax({
        type: type,
        url: url,
        data: params,
        async:false,
        dataType: "json",
        contentType:"application/json;charset=UTF-8",
        success: function (result) {
            data = result;
        }
    });

    return data;

}