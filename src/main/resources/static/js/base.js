$(function () {
  // 设置jQuery Ajax全局的参数
  $.ajaxSetup({
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      top.layer.msg(XMLHttpRequest.responseJSON.msg);
    },
    complete: function (XMLHttpRequest, textStatus) {
      if ("true" === XMLHttpRequest.getResponseHeader("noAuthentication") && textStatus === 401) {
        //清除cookie
        CookieFunc.clear("user");
        CookieFunc.clear("token");
        //跳转到登陆页面
        window.top.location = ctx + "login/toLogin";
      }
    }
  });
});

//cookie相关的方法
const CookieFunc = {
  set: function (name, value, expires, path, domain) {
    if (typeof expires == "undefined") {
      expires = new Date(new Date().getTime() + 1000 * 3600 * 24 * 365);
    }
    document.cookie = name + "=" + escape(value) + ((expires) ? "; expires=" + expires.toGMTString() : "") + ((path) ? "; path=" + path : "; path=/") + ((domain) ? ";domain=" + domain : "");
  },
  get: function (name) {
    let arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
    if (arr != null) {
      return unescape(arr[2]);
    }
    return null;
  },
  clear: function (name, path, domain) {
    if (this.get(name)) {
      document.cookie = name + "=" + ((path) ? "; path=" + path : "; path=/") + ((domain) ? "; domain=" + domain : "") + ";expires=Fri, 02-Jan-1970 00:00:00 GMT";
    }
  }
};

//iframe相关的方法
const IframeFunc = {
  autoHeight: function (iframeObj) {
    let that = $(iframeObj)[0];
    let bHeight = that.contentWindow.document.body.scrollHeight;
    let dHeight = that.contentWindow.document.documentElement.scrollHeight;
    let height = Math.max(bHeight, dHeight);
    /*console.log("bHeight = " + bHeight);
    console.log("dHeight = " + dHeight);
    console.log("height = " + height);*/
    $(that).height(height);
  }
};

//通用上传下载方法
const CommonDownloadUpload = {
  download: function (fileName, del) {
    let url = ctx + "common/download?fileName=" + encodeURI(fileName) + "&delete=" + (del ? del : false);
    top.window.location.href = url;
  }
}


const STRING = {
  //字符串格式化(%s )
  sprintf: function (str) {
    let args = arguments, flag = true, i = 1;
    str = str.replace(/%s/g, function () {
      let arg = args[i++];
      if (typeof arg === 'undefined') {
        flag = false;
        return '';
      }
      return arg;
    });
    return flag ? str : '';
  },
  praseStrEmpty: function (str) {
    if (!str || str == "undefined" || str == "null") {
      return "";
    }
    return str;
  }
}

const DRAWER = {
  //构造字符串
  buildContent: function (workHistoryList) {
    if(!workHistoryList || workHistoryList.length <= 0) {
      return "<div>无数据</div>";
    }
    let content = "<div style='padding: 10px;'><ul class=\"layui-timeline\">";
    for (let index in workHistoryList) {
      let workHistory = workHistoryList[index];
      content += "<li class=\"layui-timeline-item\">";
      content += "<i class=\"layui-icon layui-timeline-axis\"></i>";
      content += "<div class=\"layui-timeline-content layui-text\">";
      content += "<h3 class=\"layui-timeline-title\">" + dayjs(workHistory.spsj).format("YYYY-MM-DD HH:mm:ss") + "&nbsp;&nbsp;&nbsp;&nbsp;" + workHistory.czmc + "</h3>";
      content += "<p>";
      content += "操作人: " + workHistory.user.username;
      content += "</p>";
      content += "<p>";
      content += "意见: " + workHistory.yj;
      content += "</p>";
      content += "</div>";
      content += "</li>";
    }
    content += "</ul></div>";
    return content;
  }
}

//编号
const SERIAL_NUMBER = {
  get: function(callBackFunc) {
    //提交数据
    $.ajax({
      url: ctx + "common/getRandomNo",
      type: "GET",
      dataType: "json",
      async: false,
      contentType: "application/json;charset=utf-8",
      success: function (data) {
        let no = data.data;
        callBackFunc(no);
        return false;
      },
      error: function (XMLHttpRequest, textStatus, errorThrown) {
        top.layer.msg("获取编号失败");
        return false;
      }
    });
  }
}

//选项卡操作
const TAB = {
  open: function(obj) {
    let admin = top.layui.admin;
    admin.openTabsPage(obj);
    //定位当前tabs
    top.layui.element.tabChange('menuTab', obj["data-id"]);
    admin.tabsBodyChange(obj["data-id"], {});
  },
  //刷新layer父页面
  refreshLayerOpen: function() {
    let ifr = $("div.layui-body div.layui-tabsbody-item.layui-show iframe", window.top.document)[0];
    $(ifr).attr('src', $(ifr).attr('src'));
  }
}

//单据打印操作
const Receipt = {
  print: function(url, data) {
    let loadi = top.layer.load();
    //提交数据
    $.ajax({
      url: url,
      type: "GET",
      xhrFields: {
        responseType: 'blob'
      },
      data: data,
      success: function (data) {
        top.layer.close(loadi);
        const blob = new Blob([data], { type: 'application/pdf' });
        const url1 = URL.createObjectURL(blob);
        printJS({
          printable: url1,
          type: 'pdf'
        });
      },
      error: function (XMLHttpRequest, textStatus, errorThrown) {
        top.layer.close(loadi);
        top.layer.msg("打印失败");
        return false;
      }
    });
  }
}


