package com.codepig.common.rxbus;

/**
 * @author huscarter@163.com
 * @title RxBus的事件类
 * @description RxEvent为基本事件, 可以自定义其他特定的事件比如OrderEvent.
 * <p>
 * 每种事件都有一个标示type,每种事件可以定义事件行为,比如订单事件(OrderEvent)的改价行为(CHANGE_PRICE).
 * <p>
 * 每种的事件的标示(type)相差100, 意味着每种事件里可以有99种不同的行为,因此也可以通过行为推断出它属于哪种事件.
 * <p>
 * 举例:订单事件OrderEvent的type为100,订单的改价和付款行为标示为101和102,最大的行为标示为199.
 * @date 9/30/16
 */
public class RxEvent {
    /**
     * 事件的标示
     */
    public int type = 0;
    /**
     * 事件的发送者
     */
    public Object sender = null;

    public RxEvent() {
        //
    }

    public RxEvent(int type) {
        this.type = type;
    }

    public RxEvent(int type, Object sender) {
        this.type = type;
        this.sender = sender;
    }
    /**
     * 登录,注册事件(Login and register of user==LRU)
     */
    public static class MainEvent extends RxEvent {
        public static final int VALUE = 100;
        public static final int SHOW_BOTTOM = VALUE + 1;//显示底部导航tab
        public static final int HIDE_BOTTOM = VALUE + 2;//隐藏底部导航tab
        public static final int GO_USER_PAGE = VALUE + 3;//打开个人主页
        public static final int SET_DESTROY = VALUE + 4;//设置自焚时间
        public static final int SET_LINK = VALUE + 5;//设置自焚时间
        public static final int SET_DESCRIPTION = VALUE + 6;
        public static final int SET_NICK = VALUE + 7;
        public static final int POST_GROUP_SUCCESS = VALUE + 8;
        public static final int SHOW_UPLOAD_WINDOW = VALUE + 9;
        public static final int UPLOAD_SUCCESS = VALUE + 10;
        public static final int UPLOAD_FAILED = VALUE + 11;
        public static final int UPLOAD_WINDOW_CLOSE = VALUE + 12;
        public static final int PUSH_NEWS = VALUE + 13;
        public static final int UPDATA_SUB = VALUE + 14;
        public static final int VIP_PAY_FINISH = VALUE + 15;
        public static final int NEW_APK_DOWNLOADED = VALUE + 16;

        public MainEvent() {
            super(VALUE);
        }
    }

    /**
     * 登录,注册事件(Login and register of user==LRU)
     */
    public static class LoginEvent extends RxEvent {
        public static final int VALUE = 100;
        /**
         * 登录成功
         */
        public static final int LOGIN_SUCCESS = VALUE + 1;
        public static final int RE_LOGIN = VALUE + 2;
        public static final int LOGOUT = VALUE + 3;
        public static final int GET_USER_INFO = VALUE + 4;
        public static final int LOGIN_WEXIN = VALUE +4;
        public static final int BIND_WEXIN = VALUE +5;

        public LoginEvent() {
            super(VALUE);
        }
    }

    public static class PersonInfoEvent extends RxEvent {
        public static final int VALUE = 300;
        public static final int SET_NICK = VALUE + 1;
        public static final int SET_DESCRIPTION = VALUE + 2;
        public static final int SET_HEAD = VALUE + 3;
        public static final int SET_COVER = VALUE + 4;
        public static final int VIP_PAYED = VALUE + 5;
        public static final int SYSTEM_NOTIFICATION_READED = VALUE + 8;    //系统消息已读
    }

    public static class GroupEvent extends RxEvent {
        public static final int VALUE = 400;
        public static final int SUB = VALUE + 1;
        public static final int UNSUB = VALUE + 2;
        public static final int UPDATA_GROUP = VALUE + 3;
    }

    public static class VideoEvent extends RxEvent {
        public static final int VALUE = 500;
        public static final int POST_VIDEO = VALUE + 1;
        public static final int DELETE_VIDEO = VALUE + 2;
        public static final int CUT_PREV = VALUE + 3;
        public static final int CUT_FEATURE = VALUE + 4;
        public static final int UPLOAD_PROGRESS = VALUE + 5;
        public static final int CUT_TOPIC = VALUE + 6;
    }

    public static class ChatEvent extends RxEvent {
        public static final int VALUE = 600;
        public static final int GOT_MESSAGE = VALUE + 1;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getSender() {
        return sender;
    }

    public void setSender(Object sender) {
        this.sender = sender;
    }

    /**
     * 重写toString方法:展示为type值.
     *
     * @return
     */
    @Override
    public String toString() {
        return "event type:" + type;
    }

}
