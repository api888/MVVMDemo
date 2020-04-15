package com.codepig.common.example.viewmodle;

import com.codepig.common.example.callback.ExampleCB;
import com.codepig.common.rxbus.EventInfo;
import com.codepig.common.rxbus.RxBus;
import com.codepig.common.rxbus.RxEvent;
import com.codepig.common.viewmodel.BaseVM;

public class ExampleVM extends BaseVM {
    private ExampleCB callback;

    /**
     * 不需要callback可以传null
     *
     * @param callBack
     */
    public ExampleVM(ExampleCB callBack) {
        super(callBack);
        this.callback = callBack;
        getData();
    }

    public void getData() {
//        HttpUtils.doHttp(subscription,
//                RxRequest.create(MainApi.class, HttpVersionConfig.API_MESSAGE_UNREAD).getMessageUnread(),
//                new ServiceCallback<BaseData<MessageUnread>>() {
//            @Override
//            public void onError(Throwable error) {
//
//            }
//
//            @Override
//            public void onSuccess(BaseData<MessageUnread> messageUnreadBaseData) {
//                callback.showData();
//            }
//
//            @Override
//            public void onDefeated(BaseData<MessageUnread> messageUnreadBaseData) {
//
//            }
//        });
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        RxBus.getInstance().register(RxEvent.PersonInfoEvent.SYSTEM_NOTIFICATION_READED, this);
    }

    @Override
    public void unRegisterRxBus() {
        super.unRegisterRxBus();
        RxBus.getInstance().unregister(RxEvent.PersonInfoEvent.SYSTEM_NOTIFICATION_READED, this);
    }

    public void onRxEvent(RxEvent event, EventInfo info) {
        switch (event.getType()) {
            case RxEvent.PersonInfoEvent.SYSTEM_NOTIFICATION_READED:
                break;
        }
    }
}
