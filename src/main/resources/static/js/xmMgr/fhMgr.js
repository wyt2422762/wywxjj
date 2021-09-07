//向后台发送请求(添加、修改房号)
function aeFh(data, indexout) {
  let loadi = top.layer.load();

  $.ajax({
    url: ctx + "/CZF/FHGL/aeFh",
    type: "POST",
    dataType: "json",
    contentType: "application/json;charset=utf-8",
    data: JSON.stringify(data),
    success: function (data) {
      top.layer.close(loadi); //关闭弹出框
      top.layer.msg(data.msg);
      top.layer.close(indexout); //关闭弹出框
      TAB.refreshLayerOpen();
      return false;
    },
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      top.layer.close(loadi); //关闭弹出框
      top.layer.msg(XMLHttpRequest.responseJSON.msg?XMLHttpRequest.responseJSON.msg:"操作失败!");
      return false;
    }
  });
}