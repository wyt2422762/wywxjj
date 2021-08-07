//向后台发送请求(添加、修改角色)
function aeRole(data, index) {
  $.ajax({
    url: ctx + "PTXT/JSGL/aeRole",
    async: false,
    type: "POST",
    dataType: "json",
    contentType: "application/json;charset=utf-8",
    data: JSON.stringify(data),
    success: function (data) {
      top.layer.msg(data.msg);
      top.layer.close(index); //关闭弹出框
      let ifr = $("div.layui-tab div.layui-tab-content div.layui-show iframe", window.top.document)[0];
      $(ifr).attr('src', $(ifr).attr('src'));
    },
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      top.layer.msg(XMLHttpRequest.responseJSON.msg);
    }
  });
}