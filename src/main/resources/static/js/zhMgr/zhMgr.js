//向后台发送请求(添加、修改账户)
function aeZh(data, index) {
  $.ajax({
    url: ctx + "/CZF/ZHGL/aeZh",
    type: "POST",
    dataType: "json",
    contentType: "application/json;charset=utf-8",
    data: JSON.stringify(data),
    success: function (data) {
      top.layer.msg(data.msg);
      if (!index) {
        window.location.reload();
      } else {
        top.layer.close(index); //关闭弹出框
        let ifr = $("div.layui-tab div.layui-tab-content div.layui-show iframe", window.top.document)[0];
        $(ifr).attr('src', $(ifr).attr('src'));
      }
    },
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      top.layer.msg(XMLHttpRequest.responseJSON.msg ? XMLHttpRequest.responseJSON.msg : "操作失败!");
      return false;
    }
  });
}

//缴费
function jf(id, jfje, index) {
  let data = {"jfje": jfje};
  $.ajax({
    url: ctx + "/CZF/ZHGL/jf/" + id + "?jfje=" + jfje,
    type: "POST",
    dataType: "json",
    contentType: "application/json;charset=utf-8",
    data: JSON.stringify(data),
    success: function (data) {
      top.layer.msg(data.msg);
      if (!index) {
        window.location.reload();
      } else {
        top.layer.close(index); //关闭弹出框
        let ifr = $("div.layui-tab div.layui-tab-content div.layui-show iframe", window.top.document)[0];
        $(ifr).attr('src', $(ifr).attr('src'));
      }
    },
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      top.layer.msg(XMLHttpRequest.responseJSON.msg ? XMLHttpRequest.responseJSON.msg : "操作失败!");
      return false;
    }
  });
}