//向后台发送请求(添加、修改楼栋)
function aeLd(data) {
  $.ajax({
    url: ctx + "/CZF/LDGL/aeLd",
    type: "POST",
    dataType: "json",
    contentType: "application/json;charset=utf-8",
    data: JSON.stringify(data),
    success: function (data) {
      top.layer.msg(data.msg);
      window.location.reload();
    },
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      top.layer.msg(XMLHttpRequest.responseJSON.msg?XMLHttpRequest.responseJSON.msg:"操作失败!");
      return false;
    }
  });
}