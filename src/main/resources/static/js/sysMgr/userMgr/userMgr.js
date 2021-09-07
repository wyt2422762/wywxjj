//向后台发送请求(添加、修改用户)
function aeUser(data, optType, indexout) {
  let url = null;
  if ('add' === optType) {
    url = ctx + 'GYFW/YHGL/addUser';
  } else if ('edit' === optType) {
    url = ctx + 'GYFW/YHGL/editUser';
  }

  if (!url) {
    layer.msg("error");
    return false;
  }

  let loadi = top.layer.load();

  $.ajax({
    url: url,
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
      top.layer.msg(XMLHttpRequest.responseJSON.msg);
      return false;
    }
  });
}