package com.cretin.translatetools.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TranslateDownloadBean {

    private DataBean data;
    private String msg;
    private int code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DataBean {
        private List<ItemsBean> items;
        private int count;

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public static class ItemsBean {
            private String key;
            private String packageName;
            private String content;
            private String translateEn;
            private String translateTw;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getPackageName() {
                return packageName;
            }

            public void setPackageName(String packageName) {
                this.packageName = packageName;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTranslateEn() {
                return translateEn;
            }

            public void setTranslateEn(String translateEn) {
                this.translateEn = translateEn;
            }

            public String getTranslateTw() {
                return translateTw;
            }

            public void setTranslateTw(String translateTw) {
                this.translateTw = translateTw;
            }
        }
    }
}
