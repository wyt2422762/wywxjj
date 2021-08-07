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
    $(that).height(height);
  }
};

//通用上传下载方法
const CommonDownloadUpload = {
  download: function download(fileName, del) {
    let url = ctx + "common/download?fileName=" + encodeURI(fileName) + "&delete=" + (del ? del : false);
    top.window.location.href = url;
  }
}

//日期相关方法
const TIME = {
  parseTime: function parseTime(time, pattern) {
    if (arguments.length === 0 || !time) {
      return null;
    }
    const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
    let date;
    if (typeof time === 'object') {
      date = time;
    } else {
      if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
        time = parseInt(time);
      } else if (typeof time === 'string') {
        time = time.replace(new RegExp(/-/gm), '/');
      }
      if ((typeof time === 'number') && (time.toString().length === 10)) {
        time = time * 1000;
      }
      date = new Date(time)
    }
    const formatObj = {
      y: date.getFullYear(),
      m: date.getMonth() + 1,
      d: date.getDate(),
      h: date.getHours(),
      i: date.getMinutes(),
      s: date.getSeconds(),
      a: date.getDay()
    }
    const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
      let value = formatObj[key];
      // Note: getDay() returns 0 on Sunday
      if (key === 'a') {
        return ['日', '一', '二', '三', '四', '五', '六'][value];
      }
      if (result.length > 0 && value < 10) {
        value = '0' + value;
      }
      return value || 0;
    })
    return time_str;
  }
}

const STRING = {
  //字符串格式化(%s )
  sprintf: function sprintf(str) {
    var args = arguments, flag = true, i = 1;
    str = str.replace(/%s/g, function () {
      var arg = args[i++];
      if (typeof arg === 'undefined') {
        flag = false;
        return '';
      }
      return arg;
    });
    return flag ? str : '';
  },
  praseStrEmpty: function praseStrEmpty(str) {
    if (!str || str == "undefined" || str == "null") {
      return "";
    }
    return str;
  }
}
