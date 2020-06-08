package com.yc.emotion.home.index.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.emotion.home.R;
import com.yc.emotion.home.index.domain.bean.ChatItem;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by suns  on 2020/5/30 11:17.
 */
public class ChatAdapter extends BaseMultiItemQuickAdapter<ChatItem, BaseViewHolder> {
    public ChatAdapter(@Nullable List<ChatItem> data) {
        super(data);
        addItemType(ChatItem.TYPE_OTHER, R.layout.chat_item_other_view);
        addItemType(ChatItem.TYPE_ME, R.layout.chat_item_my_view);
        addItemType(ChatItem.TYPE_NOTIFICATION, R.layout.chat_item_notification);
        addItemType(ChatItem.TYPE_COME_CHAT, R.layout.chat_item_come_in);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatItem item) {
        switch (item.getItemType()) {
            case ChatItem.TYPE_ME:
            case ChatItem.TYPE_OTHER:
                helper.setText(R.id.tv_chat_name, item.getUsername())
                        .setText(R.id.tv_chat_message, item.getMessage());
                break;
            case ChatItem.TYPE_NOTIFICATION:
                break;
            case ChatItem.TYPE_COME_CHAT:
                helper.setText(R.id.tv_go_chat, item.getUsername() + "  进入聊天室");
                break;
        }

    }
}
